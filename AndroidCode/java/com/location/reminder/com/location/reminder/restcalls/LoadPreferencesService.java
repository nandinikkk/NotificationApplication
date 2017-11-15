package com.location.reminder.com.location.reminder.restcalls;


import com.location.reminder.model.Preference;
import com.location.reminder.model.PreferenceReminder;
import com.location.reminder.util.Constants;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LoadPreferencesService {

    public ArrayList<PreferenceReminder> makeHttpRequest(ArrayList<Preference> preferences, String clat, String clng) {


        if (preferences.size() == 0) {
            return new ArrayList<PreferenceReminder>();
        }

        InputStream is = null;
        JSONObject jsonObject = null;
        String json = "";

        try {

            String types = "";


            for (Preference preference : preferences) {

                types += preference.getName().toLowerCase() + "|";
            }

            if (types.endsWith("|")) {
                types = types.substring(0, types.length() - 1);
            }

            clat = URLEncoder.encode(clat, "UTF-8");
            clng = URLEncoder.encode(clng, "UTF-8");
            types = URLEncoder.encode(types, "UTF-8");
            String key = URLEncoder.encode("AIzaSyC0HVwfm7Z_cC-8-fADZBrEw6woyN1zC1g", "UTF-8");


            String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + clat + "," + clng + "&radius=400&types=" + types + "&key=" + key + "";

            System.out.println("URL" + url);
            //url = URLEncoder.encode(url, "UTF-8");
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);

            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;

            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            json = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ArrayList<PreferenceReminder> preferenceReminders = new ArrayList<>();

        try {
            System.out.println(json);
            jsonObject = new JSONObject(json);
            JSONArray jsonArray = new JSONArray(jsonObject.getString("results"));


            for (int i = 0; i < jsonArray.length(); i++) {

                String lat = jsonArray.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getString("lat");
                String lng = jsonArray.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getString("lng");
                String name = jsonArray.getJSONObject(i).getString("name");
                String types = jsonArray.getJSONObject(i).getString("types");
                String icon = jsonArray.getJSONObject(i).getString("icon");

                double rating = 2.5;
                double price_level = 1.5;
                double preferencerating = 0;

                if (jsonArray.getJSONObject(i).has("rating")) {
                    rating = jsonArray.getJSONObject(i).getDouble("rating");
                }

                if (jsonArray.getJSONObject(i).has("price_level")) {
                    price_level = jsonArray.getJSONObject(i).getDouble("price_level");
                }

                preferencerating = rating / price_level;


                PreferenceReminder preferenceReminder = new PreferenceReminder();
                preferenceReminder.setPreferencetype(types);
                preferenceReminder.setLatitude(lat);
                preferenceReminder.setLongitude(lng);
                preferenceReminder.setTitle(name);
                preferenceReminder.setImage(icon);

                preferenceReminder.setRating(rating);
                preferenceReminder.setPrice_level(price_level);
                preferenceReminder.setPreferencerating(preferencerating);

                preferenceReminders.add(preferenceReminder);
            }


        } catch (JSONException e) {

            e.printStackTrace();
        }

        class CustomComparator implements Comparator<PreferenceReminder> {
            public int compare(PreferenceReminder s1, PreferenceReminder s2) {
                return Double.compare(s2.getPreferencerating(), s1.getPreferencerating());
            }
        }

        ArrayList<PreferenceReminder> infos = new ArrayList<PreferenceReminder>();
        Collections.sort(preferenceReminders, new CustomComparator());

        return preferenceReminders;

    }

}
