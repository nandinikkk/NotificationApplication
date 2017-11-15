package com.location.reminder.com.location.reminder.restcalls.java;

import android.content.Context;

import com.location.reminder.util.Constants;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class JavaEndPointRestService {


    static String HTTP_METHOD_POST = "POST";
    static String HTTP_REQUEST_PROPERTY_CONTENT_TYPE = "Context-Type";
    static String HTTP_REQUEST_PROPERTY_CONTENT_TYPE_VALUE_URL_ENCODED = "application/x-www-form-urlencoded";


    public JSONArray getJSON(String keyvaluepair, String controller) {
        try {

            URL url;
            url = new URL(Constants.JAVA_URL + controller);

            HttpURLConnection connection =
                    (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(HTTP_METHOD_POST);
            connection
                    .setRequestProperty(
                            HTTP_REQUEST_PROPERTY_CONTENT_TYPE,
                            HTTP_REQUEST_PROPERTY_CONTENT_TYPE_VALUE_URL_ENCODED);

            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setAllowUserInteraction(false);
            connection.setInstanceFollowRedirects(false);


            DataOutputStream requestData = new DataOutputStream(
                    connection.getOutputStream());
            requestData.writeBytes(keyvaluepair);

            System.out.println("URL" + Constants.JAVA_URL + controller + keyvaluepair);
            requestData.flush();
            requestData.close();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            StringBuffer json = new StringBuffer();
            String tmp = "";
            while ((tmp = reader.readLine()) != null)
                json.append(tmp).append("\n");
            reader.close();

            System.out.println(json.toString());

            Object jsontoken = new JSONTokener(json.toString()).nextValue();
            if (jsontoken instanceof JSONObject) {

                JSONObject jsonObject = new JSONObject(json.toString());
                JSONArray jsonArray = new JSONArray();
                jsonArray.put(jsonObject);

                return jsonArray;
            } else if (jsontoken instanceof JSONArray) {

                JSONArray data = new JSONArray(json.toString());

                return data;
            } else
                return new JSONArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
