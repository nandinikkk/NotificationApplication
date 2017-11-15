package com.location.reminder.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.location.reminder.model.Locationhistory;

@Repository
public class LocationhistoryDao extends GenericDaoJpaImpl<Locationhistory, Integer> {

	public List<String> distinctPlaceIds() {

		List<String> placeids = new ArrayList<>();
		List<String> records = entityManager.createQuery("SELECT distinct l.placeid from Locationhistory l")
				.getResultList();
		for (String objects : records) {
			// String placeid = (String) objects[0];
			placeids.add(objects);
		}
		return placeids;
	}

	public List<Locationhistory> getLocationsbyPlacetypes(List<String> placetypes) {

		System.out.println("Place types"+placetypes.size());
		List<Locationhistory> records = entityManager
				.createQuery(
						"SELECT l from Locationhistory l  join l.placetypes placetypes where placetypes.placetype in (:placetypes) ")
				.setParameter("placetypes", placetypes).getResultList();

		return records;
	}
}
