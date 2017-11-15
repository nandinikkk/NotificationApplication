package com.location.reminder;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import com.location.reminder.com.location.reminder.restcalls.ReminderService;
import com.location.reminder.model.ReminderModel;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;

public class CreateReminderRunner extends AsyncTask<Void, Void, Void> {

    private ReminderModel reminderModel;


    JSONArray jsonArray;
    ProgressDialog progressdialog;
    Activity activity;
    ArrayList<String> keywords;
    String reminderid;
    String userid;
    private long unixtime;


    public CreateReminderRunner(ReminderModel reminderModel, Activity activity, ArrayList<String> keywords, String userid) {
        this.reminderModel = reminderModel;
        this.activity = activity;
        this.keywords = keywords;
        this.userid = userid;
    }

    @Override
    protected Void doInBackground(Void... params) {


        String location_type = "";
        for (String keyword : keywords) {

            if (reminderModel.getTitle().contains(keyword) || reminderModel.getReminderinfo().contains(keyword)) {
                location_type = keyword;
                break;
            }
        }
        reminderModel.setLocation_type(location_type);
        ReminderService reminderService = new ReminderService();
        jsonArray = reminderService.createReminder(reminderModel);
        try {
            reminderid = jsonArray.getJSONObject(0).getString("reminderid");

            System.out.println("Reminder ID" + reminderid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    protected void onPostExecute(Void result) {
        progressdialog.dismiss();


        Toast.makeText(activity, "Reminder Created", Toast.LENGTH_LONG).show();

        long unixtime = reminderModel.getTodate() + (reminderModel.getTotime() * 60);


        if (unixtime == 0) {

            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MINUTE, 3);
            unixtime = cal.getTimeInMillis() / 1000L;
        } else {
            if (reminderModel.getTodate() == 0) {

                Calendar newDate = Calendar.getInstance();
                newDate.set(Calendar.HOUR_OF_DAY, 0);
                newDate.set(Calendar.MINUTE, 0);
                newDate.set(Calendar.SECOND, 0);
                newDate.set(Calendar.MILLISECOND, 0);
                unixtime = ((newDate.getTimeInMillis() + (reminderModel.getTotime() * 60 * 1000)) / 1000L);
            }
        }
        System.out.println("Unix time" + unixtime);

        new AlarmManagerReceiver().SetAlarm(activity, reminderid, userid, unixtime);


        activity.finish();

    }

    @Override
    protected void onPreExecute() {
        progressdialog = ProgressDialog.show(activity, "",
                "Please wait...", true, true);
    }


}
