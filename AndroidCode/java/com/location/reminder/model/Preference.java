package com.location.reminder.model;


import java.io.Serializable;

public class Preference implements Serializable{

    String name = null;
    boolean selected = false;

    public Preference(String name, boolean selected) {
        this.name = name;
        this.selected = selected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
