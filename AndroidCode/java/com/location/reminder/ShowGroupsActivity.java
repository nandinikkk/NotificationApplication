package com.location.reminder;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.location.reminder.com.location.reminder.restcalls.GroupsService;
import com.location.reminder.model.Groups;

import java.util.ArrayList;

public class ShowGroupsActivity extends BaseActivity {

    ListView listView1;
    ArrayList<Groups> groupmembers = new ArrayList<Groups>();

    private GroupMembersAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_groups);
        listView1 = (ListView) findViewById(R.id.listView1);

        adapter =
                new GroupMembersAdapter(this, 0, groupmembers, sp.getString("userid", ""));
        listView1.setAdapter(adapter);

        laodGroupMembers(sp.getString("userid", ""));
        setTitle("Group Members");

    }

    AsyncTask<Void, Void, Void> task;
    ProgressDialog dialog;


    public void laodGroupMembers(final String uid) {

        task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                GroupsService groupsService = new GroupsService();
                groupmembers.addAll(groupsService.getGroupMember(uid));

                return null;
            }

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();
                dialog = ProgressDialog.show(ShowGroupsActivity.this, "",
                        " Please wait...", true,true);

            }

            @Override
            protected void onPostExecute(Void result) {
                dialog.dismiss();

                adapter.notifyDataSetChanged();
            }
        };
        task.execute(null, null, null);

    }
}
