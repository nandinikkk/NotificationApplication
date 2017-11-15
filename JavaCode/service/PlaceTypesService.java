package com.location.reminder.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.location.reminder.dao.PlacetypesDao;
import com.location.reminder.dao.ReminderplacetypesDao;
import com.location.reminder.model.PlaceCount;
import com.location.reminder.model.Placetypes;
import com.location.reminder.model.Reminderplacetypes;
import com.location.reminder.util.PlaceCountComparator;

@Service
public class PlaceTypesService {

	@Autowired
	PlacetypesDao placetypesDao;

	@Autowired
	ReminderplacetypesDao reminderplacetypesDao;

	public List<PlaceCount> findUserPreferences(int userid, int limit) {

		List<String> ignoreplaces = new ArrayList<>();
		ignoreplaces.add("point_of_interest");
		ignoreplaces.add("establishment");
		ignoreplaces.add("other");

		List<PlaceCount> placeCounts = placetypesDao.placesbyUseridNotin(userid, ignoreplaces);
		List<PlaceCount> preferredtypes = new ArrayList<>();
		for (int i = 0; i < placeCounts.size(); i++) {
			if (i == limit) {
				break;
			}
			preferredtypes.add(placeCounts.get(i));
		}

		return preferredtypes;
	}

	public List<String> missedRemindersPlacetypes(int userid) {

		List<String> preferences = new ArrayList<>();
		List<Reminderplacetypes> reminderplacetypes = reminderplacetypesDao.getMissedReminders(userid);
		for (Reminderplacetypes reminderplacetype : reminderplacetypes) {

			List<Placetypes> placetypes = reminderplacetype.getLocationhistory().getPlacetypes();
			for (Placetypes placetype : placetypes) {

				String type = placetype.getPlacetype();
				if (!type.equals("point_of_interest") && !type.equals("establishment") && !type.equals("other")
						&& !preferences.contains(type)) {

					preferences.add(type);
				}
			}
		}
		return preferences;
	}

	public List<String> placeTypesbyReminderid(int reminderid) {

		List<String> preferences = new ArrayList<>();
		List<Reminderplacetypes> reminderplacetypes = reminderplacetypesDao.getbyreminderid(reminderid);
		for (Reminderplacetypes reminderplacetype : reminderplacetypes) {

			List<Placetypes> placetypes = reminderplacetype.getLocationhistory().getPlacetypes();
			for (Placetypes placetype : placetypes) {

				String type = placetype.getPlacetype();
				if (!type.equals("point_of_interest") && !type.equals("establishment") && !type.equals("other")
						&& !preferences.contains(type)) {

					preferences.add(type);
				}
			}
		}
		return preferences;
	}
}
