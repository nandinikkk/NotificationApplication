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

public class GroupMembersAdapter extends ArrayAdapter<Groups> {

    private Activity activity;
    int layoutResourceId;
    private LayoutInflater inflater;
    Groups b;
    ArrayList<Groups> data;
    String uid;


    public GroupMembersAdapter(Activity activity, int layoutResourceId, ArrayList<Groups> data, String uid) {
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
        convertView = inflater.inflate(R.layout.groupmembersrow, parent, false);
        b = data.get(position);
        TextView username = (TextView) convertView.findViewById(R.id.username);
        username.setText(b.getName());

        final TextView delete = (TextView) convertView.findViewById(R.id.delete);
        delete.setTag(b);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(activity)
                        .setTitle("Delete user")
                        .setMessage("Are you sure you want to delete user from this group?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                int otheruid = ((Groups) delete.getTag()).getId();
                                deleteUser(uid, otheruid + "",(Groups) delete.getTag());
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();


            }
        });

        return convertView;

    }

    AsyncTask<Void, Void, Void> task;
    ProgressDialog dialog;

    public void deleteUser(final String uid, final String otheruid,final Groups group) {

        task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                GroupsService groupsService = new GroupsService();
                groupsService.deleteGroupMember(uid, otheruid);

                return null;
            }

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();
                dialog = ProgressDialog.show(activity, "",
                        " Please wait...", true,true);

            }

            @Override
            protected void onPostExecute(Void result) {
                dialog.dismiss();

                data.remove(group);

                notifyDataSetChanged();
            }
        };
        task.execute(null, null, null);

    }


}
