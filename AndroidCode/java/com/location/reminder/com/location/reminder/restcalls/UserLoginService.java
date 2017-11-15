package com.location.reminder.com.location.reminder.restcalls;


import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

import java.util.ArrayList;

public class UserLoginService {

    public JSONArray longwithFacebook(String facebookid, String email, String name, String birthday, String gender) {

        final ArrayList<NameValuePair> postdata = new ArrayList<NameValuePair>();
        postdata.add(new BasicNameValuePair("facebookid", facebookid));
        postdata.add(new BasicNameValuePair("name", name));
        postdata.add(new BasicNameValuePair("email", email));
        postdata.add(new BasicNameValuePair("birthday", birthday));
        postdata.add(new BasicNameValuePair("gender", gender));
        RestService getjson = new RestService();
        JSONArray jsonarray = getjson.makeHttpRequest(postdata, "facebook_registration.php");
        return jsonarray;
    }

    public JSONArray loginUserGmail(String email, String name, String gender, String birthday) {

        final ArrayList<NameValuePair> postdata = new ArrayList<NameValuePair>();
        postdata.add(new BasicNameValuePair("name", name));
        postdata.add(new BasicNameValuePair("email", email));
        postdata.add(new BasicNameValuePair("birthday", birthday));
        postdata.add(new BasicNameValuePair("gender", gender));

        RestService getjson = new RestService();
        JSONArray jsonarray = getjson.makeHttpRequest(postdata, "gmail_registration.php");
        return jsonarray;
    }

}
