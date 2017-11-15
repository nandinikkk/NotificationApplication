package com.location.reminder.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.location.reminder.dao.UserworklocationsDao;
import com.location.reminder.model.Userworklocations;
import com.location.reminder.util.Util;

@Service
public class UserWorkLocationsService {

	@Autowired
	UserworklocationsDao userworklocationsDao;

	public boolean isWorkLocation(int userid, double latitude, double longitude) {

		List<Userworklocations> userworklocations = userworklocationsDao.readbyuserid(userid);

		for (Userworklocations userworklocation : userworklocations) {

			double distance = Util.distance(userworklocation.getLatitude(), userworklocation.getLongitude(), latitude,
					longitude);
			if (distance < 40) {
				return true;
			}

		}

		return false;

	}

}
