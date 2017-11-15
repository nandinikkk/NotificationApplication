package com.location.reminder.com.location.reminder.restcalls.java;


import com.google.gson.Gson;
import com.location.reminder.model.UserInfo;
import com.location.reminder.model.User_registrations;
import com.location.reminder.util.AppCommons;
import com.location.reminder.util.GsonUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserRegistrationService {

    public UserInfo updateRegistrationDetails(String dob, String country, String gender, String userid) {

        JavaEndPointRestService javaEndPointRestService = new JavaEndPointRestService();
        Map<String, String> map = new HashMap<>();
        map.put("dob", "" + dob);
        map.put("gender", "" + gender);
        map.put("country", "" + country);
        map.put("userid", "" + userid);
        JSONArray response = javaEndPointRestService.getJSON(AppCommons.maptoString(map), "updateRegistrationDetails");
        Gson gson = new Gson();
        try {
            JSONObject jsonObject = response.getJSONObject(0);
            GsonUtil<UserInfo> gsonUtil = new GsonUtil<UserInfo>(UserInfo.class);
            UserInfo userInfo = gsonUtil.jsonObjectDeocde(jsonObject.toString());
            return userInfo;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    public User_registrations updateSettings(String name, String email,String userid,String userhomelocation,String userworklocation) {

        JavaEndPointRestService javaEndPointRestService = new JavaEndPointRestService();
        Map<String, String> map = new HashMap<>();
        map.put("email", "" + email);
        map.put("userid", "" + userid);
        map.put("name", "" + name);
        map.put("userhomelocation", "" + userhomelocation);
        map.put("userworklocation", "" + userworklocation);

        JSONArray response = javaEndPointRestService.getJSON(AppCommons.maptoString(map), "updateRegistration");
        Gson gson = new Gson();
        try {
            JSONObject jsonObject = response.getJSONObject(0);
            GsonUtil<User_registrations> gsonUtil = new GsonUtil<User_registrations>(User_registrations.class);
            User_registrations userInfo = gsonUtil.jsonObjectDeocde(jsonObject.toString());
            return userInfo;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
