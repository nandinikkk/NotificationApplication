package com.location.reminder.controller;

import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.location.reminder.dao.AlertremindersDao;
import com.location.reminder.model.Alertreminders;
import com.location.reminder.util.Util;

@Controller
public class RemindersController {

	@Autowired
	AlertremindersDao alertremindersDao;

	@RequestMapping(value = "missedreminderstatus", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Boolean> isReminderMissed(@RequestBody String params) {

		Map<String, String> paramsmap = Util.splitQuery(params);
		String reminderid = paramsmap.get("reminderid");
		String userid = paramsmap.get("userid");

		Alertreminders alertreminders = alertremindersDao.getByuseridandreminderid(Integer.parseInt(userid),
				Integer.parseInt(reminderid));

		if (alertreminders != null && alertreminders.getStatus() == 1)
			return Collections.singletonMap("missed", false);

		else
			return Collections.singletonMap("missed", true);
	}

}
