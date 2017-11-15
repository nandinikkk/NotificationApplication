package com.location.reminder;


import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

import java.util.List;

public class GPSLocator {

    private Context context;
    private boolean isGPSEnabled = false;
    private boolean isNetworkEnabled = false;
    private LocationManager locationManager;
    private Location location;
    private double latitude;
    private double longitude;

    public GPSLocator(Context context) {
        this.context = context;

        locationManager = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);

    }

    public void getMyLocation() {
        List<String> providers = locationManager.getProviders(true);

        Location l = null;
        for (int i = 0; i < providers.size(); i++) {
            try {
                l = locationManager.getLastKnownLocation(providers.get(i));
            } catch (SecurityException e) {
                e.printStackTrace();
            }
            if (l != null)
                break;
        }
        if (l != null) {
            latitude = l.getLatitude();
            longitude = l.getLongitude();
        }
    }

    public boolean isGPSenabled() {
        isGPSEnabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

        // getting network status
        isNetworkEnabled = locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        return (isGPSEnabled || isNetworkEnabled);
    }

    /**
     * Function to get latitude
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Function to get longitude
     */
    public double getLongitude() {
        return longitude;
    }
}
