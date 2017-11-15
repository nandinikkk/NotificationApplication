package com.location.reminder.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.location.reminder.aproiri.AproiriAlgorithm;
import com.location.reminder.dao.LocationhistoryDao;
import com.location.reminder.dao.UserpreferenceDao;
import com.location.reminder.dbscan.Dbscan;
import com.location.reminder.model.FrequencyItemSet;
import com.location.reminder.model.Locationhistory;
import com.location.reminder.model.PlaceCount;
import com.location.reminder.model.Placetypes;
import com.location.reminder.model.Preference;
import com.location.reminder.model.Userpreference;
import com.location.reminder.util.Config;
import com.location.reminder.util.FrequentSetSorter;
import com.location.reminder.util.UserUtil;
import com.location.reminder.util.Util;

@Service
public class FrequencySetsService {

	@Autowired
	LocationhistoryDao locationhistoryDao;

	@Autowired
	PlaceTypesService placeTypesService;

	List<String> fetchedplaceids;

	@Autowired
	UserpreferenceDao userpreferenceDao;

	public int maxdistance = 2500;

	public List<Locationhistory> getFrequentSets(int userid, Locationhistory currentLocation,
			List<String> preferences) {

		fetchedplaceids = new ArrayList<>();
		if (preferences.size() == 0) {
			return new ArrayList<>();
		}
		List<Locationhistory> locationhistoriesdata = locationhistoryDao.getLocationsbyPlacetypes(preferences);
		locationhistoriesdata.add(0, currentLocation);

		System.out.println("Max distance" + maxdistance);
		Dbscan dbscan = new Dbscan(maxdistance, 1, new Vector<Locationhistory>(locationhistoriesdata));
		Vector<List<Locationhistory>> dbscanresults = dbscan.applyDbscan();
		for (List<Locationhistory> list : dbscanresults) {

			System.out.println("\n\nNew cluster ");
			for (Locationhistory locationhistory : list) {

				System.out.println("Location " + locationhistory.getLatitude() + " " + locationhistory.getLongitude()
						+ " " + locationhistory.getLocationdetails());
			}
		}

		if (dbscanresults.get(0) == null || dbscanresults.get(0).size() == 1) {
			return new ArrayList<>();
		}

		if (dbscanresults.get(0).size() > 1) {
			if (dbscanresults.get(0).get(0).getLocationdetails() == null) {
				dbscanresults.get(0).remove(0);
			}
		}

		List<String> placeids = distinctPlaceIds(dbscanresults.get(0));

		Map<Integer, String> map = buildInputMap(placeids);
		Map<String, Integer> reversemap = placeIdsreverseMap(placeids);
		Map<String, Locationhistory> locationsbyplaceid = new HashMap<>();

		List<Locationhistory> locationhistories = dbscanresults.get(0);

		List<String> itemsets = new ArrayList<String>();
		for (Locationhistory locationhistory : locationhistories) {

			int[] array = new int[4];
			array[0] = UserUtil.genderRange(locationhistory.getUserid().getGender());
			array[1] = UserUtil.ageRange(locationhistory.getUserid().getDob());
			array[2] = UserUtil.timeRange(locationhistory.getTotime());
			array[3] = reversemap.get(locationhistory.getPlaceid());
			String line = array[0] + " " + array[1] + " " + array[2] + " " + array[3];
			itemsets.add(line);
			locationsbyplaceid.put(locationhistory.getPlaceid(), locationhistory);
		}
		int numItems = map.size();
		int numTransactions = locationhistories.size();

		AproiriAlgorithm algorithm = new AproiriAlgorithm(itemsets, numItems, numTransactions);
		Map<Integer, List<FrequencyItemSet>> frequentsets = algorithm.start();
		List<Locationhistory> resultSet = new ArrayList<>();

		int counter = 4;
		System.out.println("counter" + counter);
		while (resultSet.size() < Config.max_suggestions) {
			if (counter <= 0) {
				break;
			}

			List<FrequencyItemSet> frequentSets = frequentsets.get(counter);
			if (frequentSets != null) {
				Collections.sort(frequentSets, new FrequentSetSorter());
				resultSet.addAll(frequencySetstoLocationHistory(frequentSets, map, locationsbyplaceid, preferences));
			}
			System.out.println("in loop counter" + counter);

			counter--;
		}

		return resultSet;
	}

	private List<Locationhistory> frequencySetstoLocationHistory(List<FrequencyItemSet> frequencyItemSets,
			Map<Integer, String> inputmap, Map<String, Locationhistory> locationsbyplaceid, List<String> preferences) {

		List<Locationhistory> locationhistories = new ArrayList<>();
		for (FrequencyItemSet frequencyItemSet : frequencyItemSets) {
			int[] itemsets = frequencyItemSet.getItemset();
			for (int i : itemsets) {

				String placeid = inputmap.get(i);
				if (i >= Config.placeids_startcounter && !fetchedplaceids.contains(placeid)) {

					List<Placetypes> placetypes = locationsbyplaceid.get(placeid).getPlacetypes();
					List<Placetypes> tempplacetypes = new ArrayList<Placetypes>(placetypes);
					for (Placetypes placetype : tempplacetypes) {

						if (!preferences.contains(placetype.getPlacetype())) {
							placetypes.remove(placetype);
						}
					}
					if (placetypes.size() > 0) {
						locationsbyplaceid.get(placeid).setPlacetypes(placetypes);
						locationhistories.add(locationsbyplaceid.get(placeid));
					}
					fetchedplaceids.add(placeid);

				}
			}
		}

		return locationhistories;

	}

	public List<Locationhistory> getSuggestions(int userid, Locationhistory currentLocation) {

		List<PlaceCount> placeCounts = placeTypesService.findUserPreferences(userid, Config.default_miningpreferences);
		List<String> preferences = new ArrayList<>();
		for (PlaceCount placeCount : placeCounts) {
			preferences.add(placeCount.getPlacetypes().getPlacetype());
		}

		List<Userpreference> customizedpreferences = userpreferenceDao.getbyuserid(userid);
		for (Userpreference userpreference : customizedpreferences) {
			if (userpreference.isPreferred()) {
				if (!preferences.contains(userpreference.getPlacetype())) {
					preferences.add(userpreference.getPlacetype());
				}
			} else {
				int index = preferences.indexOf(userpreference.getPlacetype());
				if (index >= 0) {
					preferences.remove(index);
				}
			}
		}

		System.out.println(preferences);

		return getFrequentSets(userid, currentLocation, preferences);
	}

	public List<Locationhistory> getNearbyLocationsbyreminderId(int userid, Locationhistory currentLocation,
			int reminderid) {

		System.out.println("Fetching.." + reminderid);
		List<String> preferences = placeTypesService.placeTypesbyReminderid(reminderid);

		System.out.println(preferences);
		return getFrequentSets(userid, currentLocation, preferences);
	}

	public List<Locationhistory> getNearbyLocations(int userid, Locationhistory currentLocation) {

		List<String> preferences = placeTypesService.missedRemindersPlacetypes(userid);

		return getFrequentSets(userid, currentLocation, preferences);
	}

	public Map<String, Integer> placeIdsreverseMap(List<String> placeids) {
		Map<String, Integer> map = new HashMap<>();
		int startcounter = Config.placeids_startcounter;
		for (String string : placeids) {
			map.put(string, startcounter);
			startcounter++;
		}
		return map;
	}

	public Map<Integer, String> buildInputMap(List<String> placeids) {

		Map<Integer, String> map = new HashMap<>();

		map.put(0, "Female");
		map.put(1, "Male");
		map.put(2, "age 0-15");
		map.put(3, "age 15-30");
		map.put(4, "age 31-45");
		map.put(5, "age 45+");
		map.put(6, "6 AM -12 PM");
		map.put(7, "12 PM -6 PM");
		map.put(8, "6 PM -12 AM");
		map.put(9, "12 AM - 6 AM");
		int startcounter = Config.placeids_startcounter;
		for (String string : placeids) {
			map.put(startcounter, string);
			startcounter++;
		}

		return map;
	}

	private List<String> distinctPlaceIds(List<Locationhistory> locationhistories) {

		System.out.println("Histories size" + locationhistories.size());

		List<String> placeids = new ArrayList<>();
		for (Locationhistory locationhistory : locationhistories) {

			if (!placeids.contains(locationhistory.getPlaceid())) {
				placeids.add(locationhistory.getPlaceid());
			}
		}

		System.out.println("Place ids size" + placeids.size());
		return placeids;
	}

}
