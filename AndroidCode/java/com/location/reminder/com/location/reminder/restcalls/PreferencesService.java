package com.location.reminder.com.location.reminder.restcalls;


import com.location.reminder.model.Preference;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PreferencesService {

    public void udpatePreferences(String userid, ArrayList<String> preferences) {

        final ArrayList<NameValuePair> postdata = new ArrayList<NameValuePair>();
        postdata.add(new BasicNameValuePair("userid", userid));
        for (String preference : preferences) {
            postdata.add(new BasicNameValuePair("preferences[]", "" + preference));
        }
        RestService getjson = new RestService();
        getjson.makeHttpRequest(postdata, "addPreferences.php");
    }

    public ArrayList<Preference> getPreferences(String userid,String action) {

        ArrayList<Preference> preferences = new ArrayList<>();
        final ArrayList<NameValuePair> postdata = new ArrayList<NameValuePair>();

        postdata.add(new BasicNameValuePair("userid", userid));

        RestService getjson = new RestService();
        JSONArray jsonArray = getjson.makeHttpRequest(postdata, action+".php");
        for (int i = 0; i < jsonArray.length(); i++) {

            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Preference preference = new Preference(jsonObject.getString("preference"), false);

                preferences.add(preference);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return preferences;
    }
}
