package com.location.reminder.com.location.reminder.restcalls.java;


import com.location.reminder.model.Preference;
import com.location.reminder.util.AppCommons;
import com.location.reminder.util.GsonUtil;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReminderService {

    public boolean ismissed(String userid,String reminderid) {
        JavaEndPointRestService javaEndPointRestService = new JavaEndPointRestService();
        Map<String, String> map = new HashMap<>();
        map.put("userid", "" + userid);
        map.put("reminderid", "" + reminderid);
        JSONArray response = javaEndPointRestService.getJSON(AppCommons.maptoString(map), "missedreminderstatus");
        try {
            if(response.getJSONObject(0).getBoolean("missed")){

                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return false;
    }


}
