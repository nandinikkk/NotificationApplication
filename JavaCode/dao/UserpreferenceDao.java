package com.location.reminder.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.location.reminder.model.Userpreference;

@Repository
public class UserpreferenceDao extends GenericDaoJpaImpl<Userpreference, Integer> {

	public List<Userpreference> getbyuserid(int userid) {

		List<Userpreference> records = entityManager
				.createQuery("SELECT u from Userpreference u where u.userid.id  = :userid")
				.setParameter("userid", userid).getResultList();

		return records;
	}

	public List<Userpreference> getbyplacetypeanduserid(int userid, String placetype) {

		List<Userpreference> records = entityManager
				.createQuery("SELECT u from Userpreference u where u.userid.id  = :userid and u.placetype=:placetype")
				.setParameter("userid", userid).setParameter("placetype", placetype).getResultList();

		return records;
	}

}
