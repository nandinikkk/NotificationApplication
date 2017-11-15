package com.location.reminder.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.location.reminder.model.Alertreminders;

@Repository
public class AlertremindersDao extends GenericDaoJpaImpl<Alertreminders, Integer> {

	public Alertreminders getByuseridandreminderid(int userid, int reminderid) {

		List<Alertreminders> records = entityManager
				.createQuery(
						"SELECT a from Alertreminders a where a.reminders.id = :userid and a.user_registrations.id =:reminderid")
				.setParameter("userid", userid).setParameter("reminderid", reminderid).getResultList();

		Alertreminders alertreminders = null;
		if (records.size() > 0) {
			alertreminders = records.get(0);
		}
		return alertreminders;
	}
}
