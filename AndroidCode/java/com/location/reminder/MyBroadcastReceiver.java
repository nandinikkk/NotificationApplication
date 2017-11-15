package com.location.reminder;


import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import com.location.reminder.com.location.reminder.restcalls.ReminderService;
import com.location.reminder.util.Constants;

public class MyBroadcastReceiver extends BroadcastReceiver {

    AsyncTask<Void, Void, Void> task;

    @Override
    public void onReceive(Context context, Intent intent) {

        System.out.println("Testing");
        int id = intent.getIntExtra("notification_id", 1);
        final int reminderid = intent.getIntExtra("reminder_id", 1);

        Toast.makeText(context, "Reminders sharing with others in your group", Toast.LENGTH_SHORT).show();

        final SharedPreferences sp = context.getSharedPreferences(Constants.sharedPreferences,
                Context.MODE_PRIVATE);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(id);

        task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {


                ReminderService reminderService = new ReminderService();
                reminderService.assignReminders("" + reminderid, sp.getString("userid", ""));

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
        };
        task.execute(null, null, null);

    }


}


