package com.location.reminder.model;


public class PreferenceReminder extends ReminderModel {

    private String preferencetype;
    private String image;
    private double rating=0;
    private double price_level;
    private double preferencerating=0;



    public double getPreferencerating() {
        return preferencerating;
    }

    public void setPreferencerating(double preferencerating) {
        this.preferencerating = preferencerating;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getPreferencetype() {
        return preferencetype;
    }

    public void setPreferencetype(String preferencetype) {
        this.preferencetype = preferencetype;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getPrice_level() {
        return price_level;
    }

    public void setPrice_level(double price_level) {
        this.price_level = price_level;
    }
}
