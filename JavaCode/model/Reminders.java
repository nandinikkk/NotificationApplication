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
@Table(name = "reminders")
public class Reminders {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@ManyToOne
	@JoinColumn(name = "userid")
	private User_registrations userid;

	private long remindedtime;

	private short isreminded;

	public short getIsreminded() {
		return isreminded;
	}

	public void setIsreminded(short isreminded) {
		this.isreminded = isreminded;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getRemindedtime() {
		return remindedtime;
	}

	public void setRemindedtime(long remindedtime) {
		this.remindedtime = remindedtime;
	}

	public User_registrations getUserid() {
		return userid;
	}

	public void setUserid(User_registrations userid) {
		this.userid = userid;
	}

}
