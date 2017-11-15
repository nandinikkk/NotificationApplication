package com.location.reminder;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.location.reminder.com.location.reminder.restcalls.GroupsService;
import com.location.reminder.com.location.reminder.restcalls.PreferencesService;
import com.location.reminder.com.location.reminder.restcalls.java.UserpreferencesService;
import com.location.reminder.model.Groups;
import com.location.reminder.model.Preference;

import java.util.ArrayList;

public class YourPreferencesActivity extends BaseActivity {

    ListView listView1;
    ArrayList<Preference> preferences = new ArrayList<Preference>();
    private YourPreferenceAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_preferences);
        listView1 = (ListView) findViewById(R.id.listView1);

        adapter =
                new YourPreferenceAdapter(this, 0, preferences, sp.getString("userid", ""),getFontAwesomeTf());
        listView1.setAdapter(adapter);

        loadPreferences(sp.getString("userid", ""));
        setTitle("Your Preferences");
    }

    AsyncTask<Void, Void, Void> task;
    ProgressDialog dialog;


    public void loadPreferences(final String uid) {

        task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                UserpreferencesService preferencesService = new UserpreferencesService();
                preferences.addAll(preferencesService.getPreferences(uid));

                System.out.println("PREFERENCES SIZE" + preferences.size());
                return null;
            }

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();
                dialog = ProgressDialog.show(YourPreferencesActivity.this, "",
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
