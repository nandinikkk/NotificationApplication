package com.location.reminder.model;


import java.io.Serializable;
import java.util.List;

public class Locationhistory implements Serializable {
    private long id;


    private User_registrations userid;

    private double latitude;
    private double longitude;
    private int fromtime;
    private int totime;
    private int spenttime;
    private String locationdetails;

    private String address;
    private String city;
    private String state;
    private String country;
    private String postalCode;
    private String knownName;
    private String placeid;


    private double distance;


    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    private List<Placetypes> placetypes;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User_registrations getUserid() {
        return userid;
    }

    public void setUserid(User_registrations userid) {
        this.userid = userid;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getFromtime() {
        return fromtime;
    }

    public void setFromtime(int fromtime) {
        this.fromtime = fromtime;
    }

    public int getTotime() {
        return totime;
    }

    public void setTotime(int totime) {
        this.totime = totime;
    }

    public int getSpenttime() {
        return spenttime;
    }

    public void setSpenttime(int spenttime) {
        this.spenttime = spenttime;
    }

    public String getLocationdetails() {
        return locationdetails;
    }

    public void setLocationdetails(String locationdetails) {
        this.locationdetails = locationdetails;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getKnownName() {
        return knownName;
    }

    public void setKnownName(String knownName) {
        this.knownName = knownName;
    }

    public String getPlaceid() {
        return placeid;
    }

    public void setPlaceid(String placeid) {
        this.placeid = placeid;
    }


    public List<Placetypes> getPlacetypes() {
        return placetypes;
    }

    public void setPlacetypes(List<Placetypes> placetypes) {
        this.placetypes = placetypes;
    }

}
