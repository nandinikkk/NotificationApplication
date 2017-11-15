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
@Table(name = "alertreminders")
public class Alertreminders {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@ManyToOne
	@JoinColumn(name = "reminderid")
	private Reminders reminders;

	@ManyToOne
	@JoinColumn(name = "userid")
	private User_registrations user_registrations;

	private int status;
	private String remindedtime;

	public long getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Reminders getReminders() {
		return reminders;
	}

	public void setReminders(Reminders reminders) {
		this.reminders = reminders;
	}

	public User_registrations getUser_registrations() {
		return user_registrations;
	}

	public void setUser_registrations(User_registrations user_registrations) {
		this.user_registrations = user_registrations;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getRemindedtime() {
		return remindedtime;
	}

	public void setRemindedtime(String remindedtime) {
		this.remindedtime = remindedtime;
	}

}
