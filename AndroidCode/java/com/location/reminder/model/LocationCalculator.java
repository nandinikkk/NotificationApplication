package com.location.reminder.model;


import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationCalculator {

    public static boolean islocationnearby(double lat1, double lon1, double lat2, double lon2, int meters) {

        boolean nearby = false;
        double distance = distance(lat1, lon1, lat2, lon2);
        if (distance <= meters)
            nearby = true;
        return nearby;

    }

    public static Address getAddress(Context context, double latitude, double longitude) {
        Geocoder gcd = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = gcd.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {

                return addresses.get(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static double distance(double lat1, double lon1, double lat2, double lon2) {

        Location loc1 = new Location("");
        loc1.setLatitude(lat1);
        loc1.setLongitude(lon1);

        Location loc2 = new Location("");
        loc2.setLatitude(lat2);
        loc2.setLongitude(lon2);

        double meters = loc1.distanceTo(loc2);
        return meters;
    }
}
