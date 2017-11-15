package com.location.reminder.com.location.reminder.restcalls;


import com.facebook.login.LoginManager;
import com.location.reminder.model.LocationCalculator;
import com.location.reminder.model.ReminderModel;
import com.location.reminder.util.AppCommons;
import com.location.reminder.util.Constants;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ReminderService {

    public void updateReminder(String userid, String reminderid) {

        final ArrayList<NameValuePair> postdata = new ArrayList<NameValuePair>();
        postdata.add(new BasicNameValuePair("userid", "" + userid));
        postdata.add(new BasicNameValuePair("reminderid", "" + reminderid));

        RestService getjson = new RestService();
        getjson.makeHttpRequest(postdata, "updateremindertime.php");
    }


    public JSONArray createReminder(ReminderModel reminderModel) {

        final ArrayList<NameValuePair> postdata = new ArrayList<NameValuePair>();
        postdata.add(new BasicNameValuePair("userid", "" + reminderModel.getUserid()));
        postdata.add(new BasicNameValuePair("latitude", "" + reminderModel.getLatitude()));
        postdata.add(new BasicNameValuePair("longitude", reminderModel.getLongitude()));
        postdata.add(new BasicNameValuePair("title", reminderModel.getTitle()));
        postdata.add(new BasicNameValuePair("fromtime", "" + reminderModel.getFromtime()));
        postdata.add(new BasicNameValuePair("totime", "" + reminderModel.getTotime()));
        postdata.add(new BasicNameValuePair("repeat", "" + reminderModel.getRepeat()));
        postdata.add(new BasicNameValuePair("repeatinterval", "" + reminderModel.getRepeatinterval()));
        postdata.add(new BasicNameValuePair("reminderinfo", "" + reminderModel.getReminderinfo()));
        postdata.add(new BasicNameValuePair("location_type", "" + reminderModel.getLocation_type()));
        postdata.add(new BasicNameValuePair("fromdate", "" + reminderModel.getFromdate()));
        postdata.add(new BasicNameValuePair("todate", "" + reminderModel.getTodate()));
        postdata.add(new BasicNameValuePair("locationhistoryid", "" + reminderModel.getLocationid()));

        RestService getjson = new RestService();
        JSONArray jsonarray = getjson.makeHttpRequest(postdata, "insert_reminder.php");
        return jsonarray;
    }

    public ArrayList<ReminderModel> getnearbyreminders(String uid, double clat, double clong, int distance) {

        ArrayList<ReminderModel> newreminderslist = getReminders(uid);

        ArrayList<ReminderModel> reminderslist = new ArrayList<ReminderModel>(newreminderslist);


        for (ReminderModel reminder : newreminderslist) {

            double rlatitude = Double.parseDouble(reminder.getLatitude());
            double rlongitude = Double.parseDouble(reminder.getLongitude());

            if (reminder.getStatus().equals("1")) {
                reminderslist.remove(reminder);
                continue;

            }

            if (!LocationCalculator.islocationnearby(clat, clong, rlatitude, rlongitude, distance)) {
                reminderslist.remove(reminder);
            } else {
                if (reminder.getFromtime() != 0 && reminder.getTotime() != 0) {


                    if (!AppCommons.timeinbetween(reminder.getFromtime(), reminder.getTotime())) {


                        reminderslist.remove(reminder);
                        continue;

                    }
                }

                if (reminder.getFromdate() != 0 && reminder.getTodate() != 0) {

                    if (!AppCommons.dateinbetween(reminder.getFromdate(), reminder.getTodate())) {

                        reminderslist.remove(reminder);
                    }
                }
            }
        }
        return reminderslist;
    }


    public ArrayList<ReminderModel> getReminders(String uid) {
        final ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        ArrayList<ReminderModel> reminderslist = new ArrayList<ReminderModel>();
        final ArrayList<NameValuePair> postdata = new ArrayList<NameValuePair>();
        postdata.add(new BasicNameValuePair("userid", uid));

        RestService getjson = new RestService();
        JSONArray jsonArray = getjson.makeHttpRequest(postdata, "loadreminders.php");

        for (int i = 0; i < jsonArray.length(); i++) {

            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                ReminderModel reminder = new ReminderModel();
                reminder.setId(Integer.parseInt(jsonObject.getString("id")));
                reminder.setUserid(jsonObject.getString("userid"));
                reminder.setRemindercreatedby(jsonObject.getString("remindercreatedby"));
                reminder.setTitle(jsonObject.getString("title"));
                reminder.setLongitude(jsonObject.getString("longitude"));
                reminder.setLatitude(jsonObject.getString("latitude"));
                reminder.setRepeat(Integer.parseInt(jsonObject.getString("repeat")));
                reminder.setRepeatinterval(Integer.parseInt(jsonObject.getString("repeatinterval")));
                reminder.setTotime(Integer.parseInt(jsonObject.getString("totime")));
                reminder.setFromtime(Integer.parseInt(jsonObject.getString("fromtime")));
                reminder.setReminderinfo(jsonObject.getString("reminderinfo"));
                reminder.setStatus(jsonObject.getString("status"));
                reminder.setAssignedto(jsonObject.getString("assignedto"));
                reminder.setReminderby(jsonObject.getString("userid"));

                if (jsonObject.getString("startdate") != null && !jsonObject.getString("startdate").equals("null") && !jsonObject.getString("startdate").equals("")) {

                    reminder.setFromdate(Long.parseLong(jsonObject.getString("startdate")));
                }

                if (jsonObject.getString("enddate") != null && !jsonObject.getString("enddate").equals("null") && !jsonObject.getString("enddate").equals("")) {

                    reminder.setTodate(Long.parseLong(jsonObject.getString("enddate")));
                }

                reminderslist.add(reminder);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return reminderslist;


    }


    public void assignReminders(String reminderid, String ignoredby) {

        final ArrayList<NameValuePair> postdata = new ArrayList<NameValuePair>();
        postdata.add(new BasicNameValuePair("reminderid", "" + reminderid));
        postdata.add(new BasicNameValuePair("ignoredby", "" + ignoredby));

        RestService getjson = new RestService();
        getjson.makeHttpRequest(postdata, "ignoredreminders.php");
    }

    public void deleteReminder(String reminderid) {

        final ArrayList<NameValuePair> postdata = new ArrayList<NameValuePair>();
        postdata.add(new BasicNameValuePair("reminderid", "" + reminderid));
        RestService getjson = new RestService();
        getjson.makeHttpRequest(postdata, "deletereminder.php");
    }

    public void removeuserfromreminder(String reminderid, String userid) {

        final ArrayList<NameValuePair> postdata = new ArrayList<NameValuePair>();
        postdata.add(new BasicNameValuePair("reminderid", "" + reminderid));
        postdata.add(new BasicNameValuePair("userid", "" + userid));

        RestService getjson = new RestService();
        getjson.makeHttpRequest(postdata, "removeuserfromreminder.php");
    }
}
