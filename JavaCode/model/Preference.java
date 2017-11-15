package com.location.reminder.model;

import java.io.Serializable;

public class Preference implements Serializable {

	String name = null;
	int count;

	public Preference(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

}