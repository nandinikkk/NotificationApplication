package com.location.reminder;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.PlaceTypes;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.location.reminder.com.location.reminder.restcalls.LoadPreferencesService;
import com.location.reminder.com.location.reminder.restcalls.PreferencesService;
import com.location.reminder.com.location.reminder.restcalls.ReminderService;
import com.location.reminder.com.location.reminder.restcalls.java.LocationService;
import com.location.reminder.model.LocationAddress;
import com.location.reminder.model.LocationCalculator;
import com.location.reminder.model.Preference;
import com.location.reminder.model.ReminderModel;
import com.location.reminder.util.AppCommons;
import com.location.reminder.util.Constants;
import com.location.reminder.util.PlaceCommons;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class AppLocationService extends Service {

    public static final String BROADCAST_ACTION = "USER LOCATION";

    public LocationManager locationManager;
    public AppLocationService.MyLocationListener listener;
    public Location previousBestLocation = null;

    Intent intent;
    int counter = 0;
    public SharedPreferences sp;
    private GoogleApiClient mGoogleApiClient;

    @Override
    public void onCreate() {
        super.onCreate();
        intent = new Intent(BROADCAST_ACTION);
        sp = getSharedPreferences(Constants.sharedPreferences,
                Context.MODE_PRIVATE);
        mGoogleApiClient = new GoogleApiClient.Builder(this)

                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();
        mGoogleApiClient.connect();
    }


    @Override
    public void onStart(Intent intent, int startId) {

        this.intent = intent;
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        listener = new AppLocationService.MyLocationListener();
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10, Constants.STAY_TRESHOLD_DISTANCE_METERS, listener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, Constants.STAY_TRESHOLD_DISTANCE_METERS, listener);
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            locationManager.removeUpdates(listener);
        }
    }

    public static Thread runinBG(final Runnable runnable) {
        final Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    runnable.run();
                } finally {

                }
            }
        };
        t.start();
        return t;
    }

    public void createSuggestionNotification() {


        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("type", "suggestions");

        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);


        Notification noti = new Notification.Builder(this)
                .setContentTitle("Picked some places based on your activity ")
                .setContentText("Suggestions").setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pIntent)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        noti.flags |= Notification.FLAG_AUTO_CANCEL;
        //noti.defaults |= Notification.DEFAULT_SOUND;
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmSound == null) {
            alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            if (alarmSound == null) {
                alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            }
        }
        noti.sound = alarmSound;
        noti.defaults |= Notification.DEFAULT_VIBRATE;
        notificationManager.notify(2, noti);

    }

    public void createNotification(ReminderModel reminder, String clat, String clong, String dlat, String dlong) {

        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?saddr=" + clat + "," + clong + "&daddr=" + dlat + "," + dlong + ""));

        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);

        int notificationId = (int) System.currentTimeMillis();

        Intent cancelIntent = new Intent(this, MyBroadcastReceiver.class);
        Bundle extras = new Bundle();
        extras.putInt("notification_id", notificationId);
        extras.putInt("reminder_id", reminder.getId());
        cancelIntent.putExtras(extras);

        PendingIntent resultPendingIntent =
                PendingIntent.getBroadcast(this, notificationId, cancelIntent, PendingIntent.FLAG_CANCEL_CURRENT);


        Notification noti = new Notification.Builder(this)
                .setContentTitle("Reminder at this location " + reminder.getReminderinfo())
                .setContentText("Subject").setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pIntent)
                .addAction(android.R.drawable.ic_menu_my_calendar, "Navigate", pIntent)
                .addAction(android.R.drawable.ic_menu_delete, "Ignore", resultPendingIntent)

                .build();


        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        noti.flags |= Notification.FLAG_AUTO_CANCEL;
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmSound == null) {
            alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            if (alarmSound == null) {
                alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            }
        }
        noti.sound = alarmSound;
        noti.defaults |= Notification.DEFAULT_VIBRATE;

        notificationManager.notify(notificationId, noti);

    }

    public void getNearbyReminders(final Location loc) {

        if (sp.getString("userid", "").equals("")) {
            return;
        }
        Toast.makeText(getApplicationContext(), "Searching for reminders", Toast.LENGTH_SHORT).show();


        AsyncTask<Void, Void, Void> task;

        task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                double clat = loc.getLatitude();
                double clong = loc.getLongitude();

                ReminderService reminderService = new ReminderService();
                ArrayList<ReminderModel> nearbyreminders = reminderService.getnearbyreminders(sp.getString("userid", ""), clat, clong, sp.getInt("maxdistance", Constants.DEFAULT_PREFLOC_DISTANCE));

                for (ReminderModel reminder : nearbyreminders) {

                    reminderService.updateReminder(sp.getString("userid", ""), "" + reminder.getId());

                    if (sp.getBoolean("shownotifications", true)) {
                        createNotification(reminder, "" + clat, "" + clong, reminder.getLatitude(), reminder.getLongitude());
                    }
                }


                return null;
            }


        };

        task.execute(null, null, null);
    }

    public class MyLocationListener implements LocationListener {

        SharedPreferences sp = getSharedPreferences(Constants.sharedPreferences,
                Context.MODE_PRIVATE);


        public void updateLocation(final double previousLatitude, final double previousLongitude, final long fromtime, final String placetypes, final String placeId, final String addressdetails) {


            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    LocationService locationService = new LocationService();
                    LocationAddress locationAddress = AppCommons.buildAddress(AppLocationService.this, previousLatitude, previousLongitude);
                    long totime = AppCommons.getCurrentUnixTimestamp();

                    locationService.addNewLocation(previousLatitude, previousLongitude, fromtime, totime, locationAddress, placetypes, placeId, sp.getString("userid", ""), addressdetails);
                    return null;
                }

                @Override
                protected void onPreExecute() {
                    // TODO Auto-generated method stub
                    super.onPreExecute();
                }

                @Override
                protected void onPostExecute(Void result) {
                }
            }.execute(null, null, null);

        }

        String placetypes = "";


        public void onLocationChanged(final Location loc) {

            if (sp.getString("userid", "").equals("")) {
                return;
            }
            getNearbyReminders(loc);
            final double latitude = loc.getLatitude();
            final double longitude = loc.getLongitude();

            System.out.println("Latitude" + latitude);
            System.out.println("Longitude" + longitude);

            boolean persistLocation = true;
            if (!sp.getString("previouslatitude", "0").equals("0")) {

                System.out.println("Inside if cond");
                final double previousLatitude = Double.parseDouble(sp.getString("previouslatitude", "0"));
                final double previousLongitude = Double.parseDouble(sp.getString("previouslongitude", "0"));
                final long fromtime = Long.parseLong(sp.getString("fromtime", "0"));
                final String placeId = sp.getString("placeid", "");
                final String addressdetails = sp.getString("addressdetails", "");

                boolean isNearby = LocationCalculator.islocationnearby(latitude, longitude, previousLatitude, previousLongitude, Constants.STAY_TRESHOLD_DISTANCE_METERS);

                System.out.println("Is Near by" + isNearby);

                if (isNearby) {
                    persistLocation = false;

                } else {

                    long totime = AppCommons.getCurrentUnixTimestamp();
                    System.out.println(totime);
                    System.out.println(fromtime);
                    if (totime - fromtime > Constants.STAY_TRESHOLD_TIME_SECS) {

                        Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeId)
                                .setResultCallback(new ResultCallback<PlaceBuffer>() {
                                    @Override
                                    public void onResult(PlaceBuffer places) {
                                        if (places.getStatus().isSuccess() && places.getCount() > 0) {
                                            final Place myPlace = places.get(0);
                                            placetypes = PlaceCommons.getPlaceTypesCSV(myPlace);

                                            updateLocation(previousLatitude, previousLongitude, fromtime, placetypes, placeId, addressdetails);
                                        }
                                        places.release();
                                    }
                                });


                    }
                }

            }

            System.out.println("LOCATION CHANGED" + persistLocation);
            if (persistLocation) {

                final int mMaxEntries = 5;

                PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi
                        .getCurrentPlace(mGoogleApiClient, null);
                result.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {
                    @Override
                    public void onResult(@NonNull PlaceLikelihoodBuffer likelyPlaces) {
                        int i = 0;
//                    mLikelyPlaceNames = new String[mMaxEntries];
//                    mLikelyPlaceAddresses = new String[mMaxEntries];
//                    mLikelyPlaceAttributions = new String[mMaxEntries];
//                    mLikelyPlaceLatLngs = new LatLng[mMaxEntries];
                        System.out.println("Likely Places" + likelyPlaces.getCount());

                        for (PlaceLikelihood placeLikelihood : likelyPlaces) {
                            // Build a list of likely places to show the user. Max 5.
//                        mLikelyPlaceNames[i] = (String) placeLikelihood.getPlace().getName();
//                        mLikelyPlaceAddresses[i] = (String) placeLikelihood.getPlace().getAddress();
//                        mLikelyPlaceAttributions[i] = (String) placeLikelihood.getPlace()
//                                .getAttributions();
//                        mLikelyPlaceLatLngs[i] = placeLikelihood.getPlace().getLatLng();
                            String placeid = placeLikelihood.getPlace().getId();
                            System.out.println("LATLNG" + placeLikelihood.getPlace().getLatLng().latitude);
                            System.out.println(placeLikelihood.getPlace().getLatLng().longitude);

                            System.out.println("ADDRESS" + placeLikelihood.getPlace().getAddress());
                            System.out.println("ADDRESS" + placeLikelihood.getPlace().getName());
                            System.out.println("ADDRESS" + placeLikelihood.getPlace().getPhoneNumber());


                            SharedPreferences mypref = getSharedPreferences(Constants.sharedPreferences,
                                    Context.MODE_PRIVATE);
                            SharedPreferences.Editor prefsEditr = mypref.edit();
                            prefsEditr.putString("previouslatitude", "" + latitude);
                            prefsEditr.putString("previouslongitude", "" + longitude);
                            prefsEditr.putString("fromtime", "" + AppCommons.getCurrentUnixTimestamp());
                            prefsEditr.putString("placeid", "" + placeid);
                            prefsEditr.putString("addressdetails", "" + placeLikelihood.getPlace().getName() + " " + placeLikelihood.getPlace().getAddress());

                            prefsEditr.commit();

                            break;


                        }
                        // Release the place likelihood buffer, to avoid memory leaks.
                        likelyPlaces.release();

                        // Show a dialog offering the user the list of likely places, and add a
                        // marker at the selected place.
                    }
                });


            }

            if (sp.getBoolean("snotis", true)) {
                createSuggestionNotification();
            }
            //   Toast.makeText(getApplicationContext(), "Searching for reminders", Toast.LENGTH_SHORT).show();
        }

        public void onProviderDisabled(String provider) {
            Toast.makeText(getApplicationContext(), "Gps Disabled", Toast.LENGTH_SHORT).show();
            Intent gpsOptionsIntent = new Intent(
                    android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(gpsOptionsIntent);
        }


        public void onProviderEnabled(String provider) {
            Toast.makeText(getApplicationContext(), "Gps Enabled", Toast.LENGTH_SHORT).show();
        }


        public void onStatusChanged(String provider, int status, Bundle extras) {

        }
    }

}
