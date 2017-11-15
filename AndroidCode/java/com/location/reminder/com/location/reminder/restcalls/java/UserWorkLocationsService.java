package com.location.reminder.com.location.reminder.restcalls.java;


import com.google.gson.Gson;
import com.location.reminder.model.UserInfo;
import com.location.reminder.util.AppCommons;
import com.location.reminder.util.GsonUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserWorkLocationsService {

    public JSONArray getUserworkLocations(String userid) {

        JavaEndPointRestService javaEndPointRestService = new JavaEndPointRestService();
        Map<String, String> map = new HashMap<>();
        map.put("userid", "" + userid);

        JSONArray response = javaEndPointRestService.getJSON(AppCommons.maptoString(map), "getworklocatins");
        return response;
    }

}
