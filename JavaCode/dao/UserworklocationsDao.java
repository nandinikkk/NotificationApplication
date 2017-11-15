package com.location.reminder.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.location.reminder.model.Alertreminders;
import com.location.reminder.model.Userworklocations;

@Repository
public class UserworklocationsDao extends GenericDaoJpaImpl<Userworklocations, Integer> {

	public Userworklocations readbytypeanduserid(int userid, String type) {

		List<Userworklocations> records = entityManager
				.createQuery("SELECT u from Userworklocations u where u.userid.id = :userid and u.type =:type")
				.setParameter("userid", userid).setParameter("type", type).getResultList();

		Userworklocations userworklocations = null;
		if (records.size() > 0) {
			userworklocations = records.get(0);
		}
		return userworklocations;
	}

	public List<Userworklocations> readbyuserid(int userid) {

		List<Userworklocations> records = entityManager
				.createQuery("SELECT u from Userworklocations u where u.userid.id = :userid")
				.setParameter("userid", userid).getResultList();

		return records;
	}

}
