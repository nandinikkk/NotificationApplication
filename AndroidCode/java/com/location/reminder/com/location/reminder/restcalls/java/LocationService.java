package com.location.reminder.com.location.reminder.restcalls.java;


import com.google.gson.Gson;
import com.location.reminder.model.LocationAddress;
import com.location.reminder.model.Locationhistory;
import com.location.reminder.model.UserInfo;
import com.location.reminder.util.AppCommons;
import com.location.reminder.util.GsonUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LocationService {

    public boolean forceadd = false;

    public Locationhistory addNewLocation(double latitude, double longitude, long fromtime, long totime, LocationAddress locationAddress, String placetypes, String placeId, String userid, String addressdetails) {


        JavaEndPointRestService javaEndPointRestService = new JavaEndPointRestService();
        try {

            Map<String, String> map = new HashMap<>();
            map.put("latitude", "" + latitude);
            map.put("longitude", "" + longitude);
            map.put("fromtime", "" + fromtime);
            map.put("totime", "" + totime);
            map.put("spenttime", "" + (totime - fromtime));

            map.put("address", "" + locationAddress.getAddress());
            map.put("city", "" + locationAddress.getCity());
            map.put("state", "" + locationAddress.getState());
            map.put("country", "" + locationAddress.getCountry());
            map.put("postalCode", "" + locationAddress.getPostalCode());
            map.put("knownName", "" + locationAddress.getKnownName());
            map.put("addressdetails", "" + addressdetails);


            map.put("placetypes", "" + placetypes);
            map.put("placeid", "" + placeId);
            map.put("userid", "" + userid);
            map.put("forceadd", "" + forceadd);


            JSONArray response = javaEndPointRestService.getJSON(AppCommons.maptoString(map), "addlocationhistory");
            try {
                JSONObject jsonObject = response.getJSONObject(0);
                GsonUtil<Locationhistory> gsonUtil = new GsonUtil<Locationhistory>(Locationhistory.class);
                Locationhistory userInfo = gsonUtil.jsonObjectDeocde(jsonObject.toString());
                return userInfo;

            } catch (JSONException e) {
                e.printStackTrace();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;


    }
}
