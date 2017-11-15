package com.location.reminder.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.location.reminder.dao.UserpreferenceDao;
import com.location.reminder.model.User_registrations;
import com.location.reminder.model.Userpreference;
import com.location.reminder.util.Util;

@Controller
public class ManagePreferencesController {

	@Autowired
	UserpreferenceDao userpreferenceDao;

	@Transactional
	@RequestMapping(value = "addPreference", method = RequestMethod.POST)
	public void addPreference(@RequestBody String params) {

		Map<String, String> paramsmap = Util.splitQuery(params);
		String placetype = paramsmap.get("placetype");
		String userid = paramsmap.get("userid");

		Userpreference userpreference = new Userpreference();
		userpreference.setPlacetype(placetype);

		User_registrations user_registrations = new User_registrations();
		user_registrations.setId(Integer.parseInt(userid));
		userpreference.setUserid(user_registrations);
		userpreference.setPreferred(true);

		userpreferenceDao.create(userpreference);

	}

	@Transactional
	@RequestMapping(value = "deletePreference", method = RequestMethod.POST)
	public void deletePreference(@RequestBody String params) {

		Map<String, String> paramsmap = Util.splitQuery(params);
		String placetype = paramsmap.get("placetype");
		String userid = paramsmap.get("userid");

		Userpreference userpreference = new Userpreference();
		userpreference.setPlacetype(placetype);
		List<Userpreference> userpreferences = userpreferenceDao.getbyplacetypeanduserid(Integer.parseInt(userid),
				placetype);
		if (userpreferences.size() > 0) {

			userpreference = userpreferences.get(0);
		} else {

			User_registrations user_registrations = new User_registrations();
			user_registrations.setId(Integer.parseInt(userid));
			userpreference.setUserid(user_registrations);
		}

		userpreference.setPreferred(false);

		userpreferenceDao.update(userpreference);

	}

}
