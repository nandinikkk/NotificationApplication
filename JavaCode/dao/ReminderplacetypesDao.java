package com.location.reminder.dao;

import java.util.Calendar;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.location.reminder.model.Reminderplacetypes;

@Repository
public class ReminderplacetypesDao extends GenericDaoJpaImpl<Reminderplacetypes, Integer> {

	public List<Reminderplacetypes> getMissedReminders(int userid) {

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, -1);
		long daysago = cal.getTimeInMillis() / 1000L;

		List<Reminderplacetypes> records = entityManager
				.createQuery(
						"SELECT r from Reminderplacetypes r where r.reminders.remindedtime < :daysago and r.reminders.userid.id = :userid and r.reminders.isreminded=0 ")
				.setParameter("daysago", daysago).setParameter("userid", userid).getResultList();

		return records;
	}

	public List<Reminderplacetypes> getbyreminderid(int reminderid) {

		List<Reminderplacetypes> records = entityManager
				.createQuery("SELECT r from Reminderplacetypes r where  r.reminders.id = :reminderid ")
				.setParameter("reminderid", reminderid).getResultList();

		return records;
	}
}
