package com.location.reminder.util;

import java.util.Comparator;

import com.location.reminder.model.FrequencyItemSet;

public class FrequentSetSorter implements Comparator<FrequencyItemSet> {

	@Override
	public int compare(FrequencyItemSet f1, FrequencyItemSet f2) {
		return Integer.compare(f2.getSupport(), f1.getSupport());
	}
}
