package com.location.reminder.model;


import java.io.Serializable;

public class Placetypes implements Serializable{


	private long id;


	private Locationhistory locationhistoryid;

	private String placetype;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}


	public Locationhistory getLocationhistoryid() {
		return locationhistoryid;
	}

	public void setLocationhistoryid(Locationhistory locationhistoryid) {
		this.locationhistoryid = locationhistoryid;
	}

	public String getPlacetype() {
		return placetype;
	}

	public void setPlacetype(String placetype) {
		this.placetype = placetype;
	}

}
