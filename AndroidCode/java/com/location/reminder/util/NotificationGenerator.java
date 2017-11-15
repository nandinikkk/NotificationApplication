package com.location.reminder.util;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;

import com.location.reminder.R;
import com.location.reminder.ReminderNearbyPlacesActivity;
import com.location.reminder.SuggestionsActivity;

public class NotificationGenerator {

    public void createMissedReminderNotification(Context context,int reminderid) {


        Intent intent = new Intent(context, ReminderNearbyPlacesActivity.class);
        intent.putExtra("reminderid", reminderid);
        PendingIntent pIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), intent, 0);


        Notification noti = new Notification.Builder(context)
                .setContentTitle("Found missed reminder, Check for nearby places with that reminder place types")
                .setContentText("Missed Reminder").setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pIntent)
                .build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
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
        notificationManager.notify((int) System.currentTimeMillis(), noti);

    }
}
