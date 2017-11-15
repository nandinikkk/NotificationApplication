package com.location.reminder.com.location.reminder.restcalls.java;


import com.location.reminder.model.Locationhistory;
import com.location.reminder.model.Preference;
import com.location.reminder.util.AppCommons;
import com.location.reminder.util.GsonUtil;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NearbyLocationsService {

    public String querystring;
    public List<Locationhistory> getNearbyLocations(String userid,String currentlatitude,String currentlongitude,String querytype,int maxdistance) {
        JavaEndPointRestService javaEndPointRestService = new JavaEndPointRestService();
        Map<String, String> map = new HashMap<>();
        map.put("userid", "" + userid);
        map.put("currentlatitude", "" + currentlatitude);
        map.put("currentlongitude", "" + currentlongitude);
        map.put("querytype", "" + querytype);

        if(querytype.equals("searchplaces")){
            map.put("querystring", "" + querystring);

        }
        if(querytype.equals("nearbyplacesbyreminder")){
            map.put("reminderid", "" + querystring);

        }

        map.put("maxdistance", "" + maxdistance);


        JSONArray response = javaEndPointRestService.getJSON(AppCommons.maptoString(map), "frequencysets");

        GsonUtil<Locationhistory> gsonUtil = new GsonUtil<Locationhistory>(Locationhistory.class);
        List<Locationhistory> preferences = gsonUtil.jsonArrayDecode(response);

        return preferences;
    }

}
