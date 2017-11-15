package com.location.reminder.model;


import java.io.Serializable;

public class User_registrations implements Serializable{


    private int id;

    private int regtype;
    private String gmailid;
    private String facebookid;
    private String name;
    private String email;
    private String gender;
    private String dob;
    private String country;


    private boolean activated;

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRegtype() {
        return regtype;
    }

    public void setRegtype(int regtype) {
        this.regtype = regtype;
    }

    public String getGmailid() {
        return gmailid;
    }

    public void setGmailid(String gmailid) {
        this.gmailid = gmailid;
    }

    public String getFacebookid() {
        return facebookid;
    }

    public void setFacebookid(String facebookid) {
        this.facebookid = facebookid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

}
