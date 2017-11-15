package com.location.reminder.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.location.reminder.dao.UserRegistrationDao;
import com.location.reminder.model.User_registrations;
import com.location.reminder.model.Userworklocations;
import com.location.reminder.util.GsonUtil;
import com.location.reminder.util.Util;

@Controller
public class UserRegistrationController {

	@Autowired
	UserRegistrationDao userRegistrationDao;

	@Autowired
	WorkLocationController workLocationController;

	@Transactional
	@RequestMapping(value = "updateRegistrationDetails", method = RequestMethod.POST)
	public @ResponseBody User_registrations updateRegistrationDetails(@RequestBody String params) {

		Map<String, String> paramsmap = Util.splitQuery(params);
		String userid = paramsmap.get("userid");
		String country = paramsmap.get("country");
		String gender = paramsmap.get("gender");
		String dob = paramsmap.get("dob");

		User_registrations user_registrations = userRegistrationDao.read(Integer.parseInt(userid));
		user_registrations.setCountry(country);
		user_registrations.setGender(gender);
		user_registrations.setDob(dob);

		userRegistrationDao.update(user_registrations);

		user_registrations.setActivated(true);
		return user_registrations;

	}

	@Transactional
	@RequestMapping(value = "updateRegistration", method = RequestMethod.POST)
	public @ResponseBody User_registrations updateRegistration(@RequestBody String params) {

		Map<String, String> paramsmap = Util.splitQuery(params);
		String name = paramsmap.get("name");
		String email = paramsmap.get("email");
		String userid = paramsmap.get("userid");

		String userhomelocation = paramsmap.get("userhomelocation");
		String userworklocation = paramsmap.get("userworklocation");

		User_registrations user_registrations = userRegistrationDao.read(Integer.parseInt(userid));
		user_registrations.setName(name);
		user_registrations.setEmail(email);

		userRegistrationDao.update(user_registrations);

		GsonUtil<Userworklocations> gsonutil = new GsonUtil<>(Userworklocations.class);
		Userworklocations worklocation = gsonutil.jsonObjectDeocde(userworklocation);
		Userworklocations homelocation = gsonutil.jsonObjectDeocde(userhomelocation);
		worklocation.setUserid(user_registrations);
		homelocation.setUserid(user_registrations);

		workLocationController.update(worklocation);
		workLocationController.update(homelocation);

		return user_registrations;

	}
}
