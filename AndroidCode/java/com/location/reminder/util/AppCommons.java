package com.location.reminder.util;


import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import com.location.reminder.model.LocationAddress;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AppCommons {

    public static int currenttimeinminutes() {

        String timeStamp = new SimpleDateFormat("hh:mm a").format(new Date());

        Calendar cal = Calendar.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        Date date = null;
        try {
            date = sdf.parse(timeStamp);
            System.out.println(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        cal.setTime(date);

        int mins = cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE);

        return mins;
    }


    public static boolean timeinbetween(int starttime, int endtime) {

        boolean answer = false;
        int currenttime = currenttimeinminutes();
        if ((currenttime >= starttime && currenttime <= endtime)) {
            return true;
        } else {
            if (starttime > endtime) {
                if (currenttime > starttime || currenttime >= 0 && currenttime < endtime) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean dateinbetween(long startdate, long enddate) {

        boolean answer = false;
        long currenttime = System.currentTimeMillis() / 1000L;
        if (currenttime >= startdate && currenttime <= enddate) {
            return true;
        } else {
            return false
                    ;
        }
    }

    public static String maptoString(Map<String, String> map) {

        System.out.print(map);
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (sb.length() > 0) {
                sb.append("&");
            }
            sb.append(String.format("%s=%s",
                    encodeString(entry.getKey()),
                    encodeString(entry.getValue())
            ));
        }
        return sb.toString();
    }

    public static String encodeString(String s) {
        try {

            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new UnsupportedOperationException(e);
        }
    }

    public static double distanceBetweentoLocations(double lat1, double lon1, double lat2, double lon2) {
        Location loc1 = new Location("");
        loc1.setLatitude(lat1);
        loc1.setLongitude(lon1);

        Location loc2 = new Location("");
        loc2.setLatitude(lat2);
        loc2.setLongitude(lon2);

        double distanceInMeters = loc1.distanceTo(loc2);
        distanceInMeters = Math.round(distanceInMeters * 100.0) / 100.0;
        return distanceInMeters;
    }

    public static long getCurrentUnixTimestamp() {
        return System.currentTimeMillis() / 1000L;
    }

    public static LocationAddress buildAddress(Context context, double latitude, double longitude) {
        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(context, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }

        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();
        String knownName = addresses.get(0).getFeatureName();

        LocationAddress locationAddress = new LocationAddress();
        locationAddress.setAddress(address);
        locationAddress.setCity(city);
        locationAddress.setState(state);
        locationAddress.setCountry(country);
        locationAddress.setPostalCode(postalCode);
        locationAddress.setKnownName(knownName);
        return locationAddress;
    }
}
