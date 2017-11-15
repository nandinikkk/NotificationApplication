package com.location.reminder.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.location.reminder.dao.LocationhistoryDao;
import com.location.reminder.dao.PlacetypesDao;
import com.location.reminder.model.Locationhistory;
import com.location.reminder.model.Placetypes;
import com.location.reminder.model.User_registrations;
import com.location.reminder.service.UserWorkLocationsService;
import com.location.reminder.util.Util;

@Controller
public class LocationHistoryController {

	@Autowired
	LocationhistoryDao locationhistoryDao;

	@Autowired
	PlacetypesDao placetypesDao;

	@Autowired
	UserWorkLocationsService userWorkLocationsService;

	@Transactional
	@RequestMapping(value = "addlocationhistory", method = RequestMethod.POST)
	public @ResponseBody Locationhistory addlocationhistory(@RequestBody String params) {

		Map<String, String> paramsmap = Util.splitQuery(params);
		String userid = paramsmap.get("userid");
		String placetypes = paramsmap.get("placetypes");

		Locationhistory locationhistory = fetchLocationData(paramsmap);

		if (userWorkLocationsService.isWorkLocation(Integer.parseInt(userid), locationhistory.getLatitude(),
				locationhistory.getLongitude())) {
			String forceadd = paramsmap.get("forceadd");
			if (forceadd != null && forceadd.equals("true")) {

			} else {
				return null;
			}
		}

		User_registrations user_registrations = new User_registrations();
		System.out.println(userid);
		user_registrations.setId(Integer.parseInt(userid));
		locationhistory.setUserid(user_registrations);
		locationhistory = locationhistoryDao.create(locationhistory);

		String[] placestypeslist = placetypes.split(",");
		for (String placestype : placestypeslist) {

			if (!placestype.trim().equals("")) {
				Placetypes placesobj = new Placetypes();
				placesobj.setLocationhistoryid(locationhistory);
				placesobj.setPlacetype(placestype);

				placetypesDao.create(placesobj);
			}

		}

		return locationhistory;

	}

	public Locationhistory fetchLocationData(Map<String, String> paramsmap) {

		String latitude = paramsmap.get("latitude");
		String longitude = paramsmap.get("longitude");
		String fromtime = paramsmap.get("fromtime");
		String totime = paramsmap.get("totime");
		String spenttime = paramsmap.get("spenttime");

		String address = paramsmap.get("address");
		String city = paramsmap.get("city");
		String state = paramsmap.get("state");
		String country = paramsmap.get("country");
		String postalCode = paramsmap.get("postalCode");
		String knownName = paramsmap.get("knownName");
		String addressdetails = paramsmap.get("addressdetails");

		String placeid = paramsmap.get("placeid");

		Locationhistory locationhistory = new Locationhistory();
		locationhistory.setLatitude(Double.parseDouble(latitude));
		locationhistory.setLongitude(Double.parseDouble(longitude));
		locationhistory.setFromtime(Integer.parseInt(fromtime));
		locationhistory.setTotime(Integer.parseInt(totime));
		locationhistory.setSpenttime(Integer.parseInt(spenttime));

		locationhistory.setAddress(address);
		locationhistory.setCity(city);
		locationhistory.setState(state);
		locationhistory.setCountry(country);
		locationhistory.setPostalCode(postalCode);
		locationhistory.setKnownName(knownName);
		locationhistory.setPlaceid(placeid);
		locationhistory.setLocationdetails(addressdetails);

		return locationhistory;
	}
}
