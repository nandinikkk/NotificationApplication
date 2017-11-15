package com.location.reminder.util;

import java.util.Comparator;

import com.location.reminder.model.PlaceCount;

public class PlaceCountComparator implements Comparator<PlaceCount> {

	@Override
	public int compare(PlaceCount p1, PlaceCount p2) {
		return Integer.compare(p2.getCount(), p1.getCount());
	}
}
