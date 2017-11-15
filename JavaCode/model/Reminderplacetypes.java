package com.location.reminder.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "reminderplacetypes")
public class Reminderplacetypes {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@ManyToOne
	@JoinColumn(name = "locationhistoryid")
	private Locationhistory locationhistory;

	@ManyToOne
	@JoinColumn(name = "reminderid")
	private Reminders reminders;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Locationhistory getLocationhistory() {
		return locationhistory;
	}

	public void setLocationhistory(Locationhistory locationhistory) {
		this.locationhistory = locationhistory;
	}

	public Reminders getReminders() {
		return reminders;
	}

	public void setReminders(Reminders reminders) {
		this.reminders = reminders;
	}

}
