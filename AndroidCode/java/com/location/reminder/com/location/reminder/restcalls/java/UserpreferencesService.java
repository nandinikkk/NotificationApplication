package com.location.reminder.com.location.reminder.restcalls.java;


import com.location.reminder.model.Preference;
import com.location.reminder.util.AppCommons;
import com.location.reminder.util.GsonUtil;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserpreferencesService {
    public List<Preference> getPreferences(String userid) {
        JavaEndPointRestService javaEndPointRestService = new JavaEndPointRestService();
        Map<String, String> map = new HashMap<>();
        map.put("userid", "" + userid);
        JSONArray response = javaEndPointRestService.getJSON(AppCommons.maptoString(map), "userPreferences");

        GsonUtil<Preference> gsonUtil = new GsonUtil<Preference>(Preference.class);
        List<Preference> preferences = gsonUtil.jsonArrayDecode(response);

        return preferences;
    }

    public void addPreferences(String userid, String placetype) {
        JavaEndPointRestService javaEndPointRestService = new JavaEndPointRestService();
        Map<String, String> map = new HashMap<>();
        map.put("userid", "" + userid);
        map.put("placetype", "" + placetype);
        javaEndPointRestService.getJSON(AppCommons.maptoString(map), "addPreference");
    }

    public void deletePreferences(String userid, String placetype) {
        JavaEndPointRestService javaEndPointRestService = new JavaEndPointRestService();
        Map<String, String> map = new HashMap<>();
        map.put("userid", "" + userid);
        map.put("placetype", "" + placetype);
        javaEndPointRestService.getJSON(AppCommons.maptoString(map), "deletePreference");
    }
}
