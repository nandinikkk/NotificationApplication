package com.location.reminder.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.location.reminder.model.PlaceCount;
import com.location.reminder.model.Placetypes;
import com.location.reminder.util.PlaceCountComparator;

@Repository
public class PlacetypesDao extends GenericDaoJpaImpl<Placetypes, Integer> {

	public List<PlaceCount> placesbyUseridNotin(int userid, List<String> placetypes) {

		List<Object[]> records = entityManager
				.createQuery(
						"SELECT p.placetype,COUNT(*) as count from Placetypes p  where p.locationhistoryid.userid.id=:userid and p.placetype not in (:placetypes) group by p.placetype")
				.setParameter("userid", userid).setParameter("placetypes", placetypes).getResultList();

		List<PlaceCount> placeCounts = new ArrayList<>();
		for (Object[] objects : records) {

			PlaceCount placeCount = new PlaceCount();
			long count = (Long) objects[1];

			placeCount.setCount((int) count);

			String type = (String) objects[0];
			Placetypes placetype = new Placetypes();
			placetype.setPlacetype(type);

			placeCount.setPlacetypes(placetype);

			placeCounts.add(placeCount);

		}
		Collections.sort(placeCounts, new PlaceCountComparator());

		return placeCounts;

	}

	

}
