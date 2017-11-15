package com.location.reminder.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.location.reminder.dao.UserworklocationsDao;
import com.location.reminder.model.User_registrations;
import com.location.reminder.model.Userworklocations;
import com.location.reminder.util.Util;

@Controller
public class WorkLocationController {

	@Autowired
	UserworklocationsDao userworklocationsDao;

	@Transactional
	@RequestMapping(value = "addWorkLocation", method = RequestMethod.POST)
	public @ResponseBody void updateRegistrationDetails(@RequestBody String params) {

		Map<String, String> paramsmap = Util.splitQuery(params);
		String type = paramsmap.get("type");
		String userid = paramsmap.get("userid");
		String latitude = paramsmap.get("latitude");
		String longitude = paramsmap.get("longitude");
		String remark = paramsmap.get("remark");

		Userworklocations userworklocations = new Userworklocations();
		userworklocations.setType(type);
		userworklocations.setLatitude(Double.parseDouble(latitude));
		userworklocations.setLongitude(Double.parseDouble(longitude));

		User_registrations user_registrations = new User_registrations();
		user_registrations.setId(Integer.parseInt(userid));
		userworklocations.setUserid(user_registrations);

		userworklocations.setRemark(remark);

		update(userworklocations);
	}

	@RequestMapping(value = "getworklocatins", method = RequestMethod.POST)
	public @ResponseBody Map<String, Userworklocations> getworklocatins(@RequestBody String params) {

		Map<String, String> paramsmap = Util.splitQuery(params);
		String userid = paramsmap.get("userid");

		Map<String, Userworklocations> userworklocations = new HashMap<>();
		Userworklocations worklocation = userworklocationsDao.readbytypeanduserid(Integer.parseInt(userid), "work");
		Userworklocations homelocation = userworklocationsDao.readbytypeanduserid(Integer.parseInt(userid), "home");

		userworklocations.put("work", worklocation);
		userworklocations.put("home", homelocation);

		return userworklocations;
	}

	public void update(Userworklocations userworklocations) {
		Userworklocations currentlocation = userworklocationsDao
				.readbytypeanduserid(userworklocations.getUserid().getId(), userworklocations.getType());
		if (currentlocation == null) {

			userworklocationsDao.create(userworklocations);
		} else {

			userworklocations.setId(currentlocation.getId());
			userworklocationsDao.update(userworklocations);
		}

	}
}
