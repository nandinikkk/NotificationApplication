package com.location.reminder.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.location.reminder.dao.UserRegistrationDao;
import com.location.reminder.dao.UserpreferenceDao;
import com.location.reminder.model.Locationhistory;
import com.location.reminder.model.PlaceCount;
import com.location.reminder.model.Preference;
import com.location.reminder.model.User_registrations;
import com.location.reminder.model.Userpreference;
import com.location.reminder.service.FrequencySetsService;
import com.location.reminder.service.PlaceTypesService;
import com.location.reminder.util.Config;
import com.location.reminder.util.Util;

@Controller
public class MiningFrequencySetsController {

	@Autowired
	PlaceTypesService placeTypesService;

	@Autowired
	FrequencySetsService frequencySetsService;

	@Autowired
	UserRegistrationDao userRegistrationDao;

	@Autowired
	UserpreferenceDao userpreferenceDao;

	@Transactional
	@RequestMapping(value = "frequencysets", method = RequestMethod.POST)
	public @ResponseBody List<Locationhistory> addlocationhistory(@RequestBody String params) {

		System.out.println(params);
		Map<String, String> paramsmap = Util.splitQuery(params);
		String userid = paramsmap.get("userid");
		String currentlatitude = paramsmap.get("currentlatitude");
		String currentlongitude = paramsmap.get("currentlongitude");
		String placeid = paramsmap.get("placeid");

		Locationhistory locationhistory = new Locationhistory();
		System.out.println(currentlatitude);
		locationhistory.setLatitude(Double.parseDouble(currentlatitude));
		locationhistory.setLongitude(Double.parseDouble(currentlongitude));
		locationhistory.setPlaceid(placeid);

		User_registrations user_registrations = userRegistrationDao.read(Integer.parseInt(userid));
		locationhistory.setUserid(user_registrations);

		String querytype = paramsmap.get("querytype");

		String maxdistance = paramsmap.get("maxdistance");
		if (maxdistance == null) {
			maxdistance = "2500";
		}

		frequencySetsService.maxdistance = Integer.parseInt(maxdistance);

		List<Locationhistory> locationhistories;
		if (querytype != null && querytype.equals("nearbyplaces")) {
			locationhistories = frequencySetsService.getNearbyLocations(Integer.parseInt(userid), locationhistory);

		} else if ((querytype != null && querytype.equals("searchplaces"))) {

			String querystring = paramsmap.get("querystring");
			List<String> searchedpreferences = Arrays.asList(querystring.split("\\s*,\\s*"));
			locationhistories = frequencySetsService.getFrequentSets(Integer.parseInt(userid), locationhistory,
					searchedpreferences);
		} else if ((querytype != null && querytype.equals("nearbyplacesbyreminder"))) {

			String reminderid = paramsmap.get("reminderid");
			locationhistories = frequencySetsService.getNearbyLocationsbyreminderId(Integer.parseInt(userid),
					locationhistory, Integer.parseInt(reminderid));
		}

		else {
			locationhistories = frequencySetsService.getSuggestions(Integer.parseInt(userid), locationhistory);
		}
		return locationhistories;
	}

	@RequestMapping(value = "userPreferences", method = RequestMethod.POST)
	public @ResponseBody List<Preference> userPreferences(@RequestBody String params) {

		Map<String, String> paramsmap = Util.splitQuery(params);
		String userid = paramsmap.get("userid");
		List<PlaceCount> userpreferences = placeTypesService.findUserPreferences(Integer.parseInt(userid),
				Config.default_miningpreferences);

		List<Preference> preferences = new ArrayList<>();
		List<String> preferenceslabels = new ArrayList<>();

		for (PlaceCount placeCount : userpreferences) {

			Preference preference = new Preference(placeCount.getPlacetypes().getPlacetype());
			preferenceslabels.add(placeCount.getPlacetypes().getPlacetype());
			preference.setCount(placeCount.getCount());
			preferences.add(preference);
		}

		List<Userpreference> customizedpreferences = userpreferenceDao.getbyuserid(Integer.parseInt(userid));
		for (Userpreference userpreference : customizedpreferences) {

			if (userpreference.isPreferred()) {
				if (!preferenceslabels.contains(userpreference.getPlacetype())) {
					preferences.add(new Preference(userpreference.getPlacetype()));

				}
			} else {

				int index = preferenceslabels.indexOf(userpreference.getPlacetype());
				System.out.println(index);
				if (index >= 0) {
					preferences.remove(index);
				}
			}

		}

		return preferences;
	}

}
