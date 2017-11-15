package com.location.reminder.com.location.reminder.restcalls;


import com.location.reminder.model.Groups;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GroupsService {


    public void updateGroups(String reminderid, ArrayList<Integer> userids) {
        final ArrayList<NameValuePair> postdata = new ArrayList<NameValuePair>();
        postdata.add(new BasicNameValuePair("reminderid", reminderid));
        for (int i : userids) {

            postdata.add(new BasicNameValuePair("userids[]", "" + i));

        }
        RestService getjson = new RestService();
        getjson.makeHttpRequest(postdata, "addusertoreminder.php");


    }

    public JSONArray addGroupMember(String userid, String otheruserid) {

        final ArrayList<NameValuePair> postdata = new ArrayList<NameValuePair>();
        postdata.add(new BasicNameValuePair("userid", userid));
        postdata.add(new BasicNameValuePair("otheruserid", otheruserid));
        RestService getjson = new RestService();
        return getjson.makeHttpRequest(postdata, "addGroup.php");
    }

    public void deleteGroupMember(String userid, String otheruserid) {

        final ArrayList<NameValuePair> postdata = new ArrayList<NameValuePair>();
        postdata.add(new BasicNameValuePair("userid", userid));
        postdata.add(new BasicNameValuePair("otheruserid", otheruserid));
        RestService getjson = new RestService();
        getjson.makeHttpRequest(postdata, "deleteuserfromGroup.php");
    }

    public ArrayList<Groups> getGroupMember(String userid) {

        ArrayList<Groups> groupmembers = new ArrayList<>();

        final ArrayList<NameValuePair> postdata = new ArrayList<NameValuePair>();
        postdata.add(new BasicNameValuePair("userid", userid));

        RestService getjson = new RestService();
        JSONArray jsonarray = getjson.makeHttpRequest(postdata, "getGroups.php");

        for (int i = 0; i < jsonarray.length(); i++) {

            try {
                JSONObject jsonObject = jsonarray.getJSONObject(i);
                Groups group = new Groups();
                group.setId(Integer.parseInt(jsonObject.getString("id")));
                group.setName(jsonObject.getString("name"));
                groupmembers.add(group);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return groupmembers;
    }
}
