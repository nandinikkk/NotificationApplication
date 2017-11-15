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
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Toast;

import com.location.reminder.com.location.reminder.restcalls.LoadPreferencesService;
import com.location.reminder.com.location.reminder.restcalls.PreferencesService;
import com.location.reminder.com.location.reminder.restcalls.ReminderService;
import com.location.reminder.model.Preference;
import com.location.reminder.model.ReminderModel;
import com.location.reminder.util.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AppLocationManager extends Service {

    public static final String BROADCAST_ACTION = "USER LOCATION";
    private static final int MINUTES = 1000 * 30 * 1;

    public LocationManager locationManager;
    public MyLocationListener listener;
    public Location previousBestLocation = null;

    Intent intent;
    int counter = 0;
    public SharedPreferences sp;

    @Override
    public void onCreate() {
        super.onCreate();
        intent = new Intent(BROADCAST_ACTION);
        sp = getSharedPreferences(Constants.sharedPreferences,
                Context.MODE_PRIVATE);
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
        // hide the notification after its selected
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

        notificationManager.notify(notificationId, noti);

    }

    public void createSuggestionNotification() {


        Intent intent = new Intent(this, SuggestionsActivity.class);

        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);


        Notification noti = new Notification.Builder(this)
                .setContentTitle("Picked some places based on your previous reminders ")
                .setContentText("Near by locations").setSmallIcon(R.mipmap.ic_launcher)
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

    public void createNearbyNotification() {


        Intent intent = new Intent(this, NearbyPlacesActivity.class);

        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);


        Notification noti = new Notification.Builder(this)
                .setContentTitle("Picked some places based on your preferences ")
                .setContentText("Suggestions").setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pIntent)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        noti.flags |= Notification.FLAG_AUTO_CANCEL;
        // noti.defaults |= Notification.DEFAULT_SOUND;
        noti.defaults |= Notification.DEFAULT_VIBRATE;

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmSound == null) {
            alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            if (alarmSound == null) {
                alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            }
        }
        noti.sound = alarmSound;

        notificationManager.notify(1, noti);

    }


    @Override
    public void onStart(Intent intent, int startId) {


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        listener = new MyLocationListener();
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {


            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 15000, 3f, listener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 15000, 3f, listener);
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


    public class MyLocationListener implements LocationListener {

        SharedPreferences sp = getSharedPreferences(Constants.sharedPreferences,
                Context.MODE_PRIVATE);


        public void onLocationChanged(final Location loc) {


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

                        createNotification(reminder, "" + clat, "" + clong, reminder.getLatitude(), reminder.getLongitude());
                    }

                    intent.putExtra("Latitude", loc.getLatitude());
                    intent.putExtra("Longitude", loc.getLongitude());
                    intent.putExtra("Provider", loc.getProvider());
                    sendBroadcast(intent);
                    LoadPreferencesService loadPreferencesService = new LoadPreferencesService();

                    if (sp.getBoolean("snotis", true)) {

                        PreferencesService preferencesService = new PreferencesService();
                        ArrayList<Preference> preferenceArrayList = preferencesService.getPreferences(sp.getString("userid", ""), "getPreferences");

                        if (preferenceArrayList.size() > 0 && sp.getBoolean("snotis", true)) {

                            if (loadPreferencesService.makeHttpRequest(preferenceArrayList, "" + loc.getLatitude(), "" + loc.getLongitude()).size() > 0) {
                                createNearbyNotification();
                            }
                        }
                    }

                    if (sp.getBoolean("nnotis", true)) {

                        PreferencesService preferencesService = new PreferencesService();

                        ArrayList<Preference> remindersArrayList = preferencesService.getPreferences(sp.getString("userid", ""), "getSuggestions");

                        if (remindersArrayList.size() > 0 && sp.getBoolean("nnotis", true)) {

                            if (loadPreferencesService.makeHttpRequest(remindersArrayList, "" + loc.getLatitude(), "" + loc.getLongitude()).size() > 0) {

                                createSuggestionNotification();
                            }
                        }
                    }


                    return null;
                }


            };

            task.execute(null, null, null);


        }

        public void onProviderDisabled(String provider) {
            Toast.makeText(getApplicationContext(), "Gps Disabled", Toast.LENGTH_SHORT).show();
        }


        public void onProviderEnabled(String provider) {
            Toast.makeText(getApplicationContext(), "Gps Enabled", Toast.LENGTH_SHORT).show();
        }


        public void onStatusChanged(String provider, int status, Bundle extras) {

        }


    }


}
