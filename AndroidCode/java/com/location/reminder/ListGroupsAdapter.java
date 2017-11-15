package com.location.reminder;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.location.reminder.com.location.reminder.restcalls.GroupsService;
import com.location.reminder.model.Groups;

import java.util.ArrayList;

public class ListGroupsAdapter extends ArrayAdapter<Groups> {

    private Activity activity;
    int layoutResourceId;
    private LayoutInflater inflater;
    Groups b;
    ArrayList<Groups> data;
    String uid;


    public ListGroupsAdapter(Activity activity, int layoutResourceId, ArrayList<Groups> data, String uid) {
        super(activity, layoutResourceId, data);

        this.activity = activity;
        this.data = data;
        this.layoutResourceId = layoutResourceId;
        this.uid = uid;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            inflater = ((Activity) activity).getLayoutInflater();

        }
        convertView = inflater.inflate(R.layout.user_row, parent, false);
        b = data.get(position);
        TextView username = (TextView) convertView.findViewById(R.id.username);
        username.setText(b.getName());


        return convertView;

    }


}

