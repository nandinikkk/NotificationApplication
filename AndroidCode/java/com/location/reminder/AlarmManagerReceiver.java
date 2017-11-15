package com.location.reminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;

import com.location.reminder.com.location.reminder.restcalls.java.NearbyLocationsService;
import com.location.reminder.com.location.reminder.restcalls.java.ReminderService;
import com.location.reminder.util.Constants;
import com.location.reminder.util.NotificationGenerator;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class AlarmManagerReceiver extends BroadcastReceiver {

    AsyncTask<Void, Void, Void> task;
    boolean ismissed = false;


    @Override
    public void onReceive(final Context context, Intent intent) {

        final int reminderid = intent.getIntExtra("reminderid", 0);
        System.out.println("alarm received" + reminderid);

        final String userid = intent.getStringExtra("userid");
        task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                ReminderService reminderService = new ReminderService();
                ismissed = reminderService.ismissed(userid, ""+reminderid);
                return null;
            }

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(Void result) {
                System.out.println("Reminded");
                if (ismissed) {
                    NotificationGenerator notificationGenerator = new NotificationGenerator();

                    SharedPreferences sp = context.getSharedPreferences(Constants.sharedPreferences,
                            Context.MODE_PRIVATE);

                    if (sp.getBoolean("shownotifications", true)) {
                        notificationGenerator.createMissedReminderNotification(context, reminderid);
                    }
                }
            }
        };
        task.execute(null, null, null);

    }


    public void SetAlarm(Context context, String reminderid, String userid) {

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmManagerReceiver.class);
        intent.putExtra("reminderid", reminderid);
        intent.putExtra("userid", userid);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 3);
        am.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pi);


    }

    public void SetAlarm(Context context, String reminderid, String userid, long unixtime) {

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmManagerReceiver.class);
        intent.putExtra("reminderid", Integer.parseInt(reminderid));
        intent.putExtra("userid", userid);

        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        unixtime = unixtime * 1000L;
        System.out.println("Setting alarm at" + unixtime);
        am.set(AlarmManager.RTC_WAKEUP, unixtime, pi);


    }

}
