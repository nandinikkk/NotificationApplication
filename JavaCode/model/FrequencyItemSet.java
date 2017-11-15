package com.location.reminder.model;


public class FrequencyItemSet {

	private int[] itemset;
	private int support;
	private double minpercent;
	private String placeid;

	public int[] getItemset() {
		return itemset;
	}

	public void setItemset(int[] itemset) {
		this.itemset = itemset;
	}

	public int getSupport() {
		return support;
	}

	public void setSupport(int support) {
		this.support = support;
	}

	public double getMinpercent() {
		return minpercent;
	}

	public void setMinpercent(double minpercent) {
		this.minpercent = minpercent;
	}

}
