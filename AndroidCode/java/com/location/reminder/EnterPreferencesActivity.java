package com.location.reminder;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.location.reminder.com.location.reminder.restcalls.PreferencesService;
import com.location.reminder.com.location.reminder.restcalls.ReminderService;
import com.location.reminder.model.Preference;
import com.location.reminder.model.ReminderModel;

import java.util.ArrayList;

public class EnterPreferencesActivity extends BaseActivity {


    EnterPreferencesAdapter dataAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_preferences);
        setTitle("Choose your preferences");

        ArrayList<Preference> preferenceList = new ArrayList<Preference>();
        Preference preference = new Preference("Restaurant", false);
        preferenceList.add(preference);
        preference = new Preference("Cafe", false);
        preferenceList.add(preference);
        preference = new Preference("Church", false);
        preferenceList.add(preference);
        preference = new Preference("Post_office", false);
        preferenceList.add(preference);
        preference = new Preference("Police", false);
        preferenceList.add(preference);
        preference = new Preference("Hospital", false);
        preferenceList.add(preference);
        preference = new Preference("Library", false);
        preferenceList.add(preference);
        preference = new Preference("Museum", false);
        preferenceList.add(preference);
        preference = new Preference("Park", false);
        preferenceList.add(preference);
        preference = new Preference("Gym", false);
        preferenceList.add(preference);
        preference = new Preference("Zoo", false);
        preferenceList.add(preference);
        preference = new Preference("Bar", false);
        preferenceList.add(preference);
        preference = new Preference("Insurance_agency", false);
        preferenceList.add(preference);


        dataAdapter = new EnterPreferencesAdapter(this,
                R.layout.preference_info, preferenceList);
        ListView listView = (ListView) findViewById(R.id.listView1);
        listView.setAdapter(dataAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Preference preference = (Preference) parent.getItemAtPosition(position);

            }
        });

        Button myButton = (Button) findViewById(R.id.saveselected);
        myButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                StringBuffer responseText = new StringBuffer();
                responseText.append("The following were selected...\n");

                ArrayList<Preference> PreferenceList = dataAdapter.PreferenceList;

                ArrayList<String> preferences = new ArrayList<String>();

                for (int i = 0; i < PreferenceList.size(); i++) {
                    Preference Preference = PreferenceList.get(i);
                    if (Preference.isSelected()) {
                        responseText.append("\n" + Preference.getName());
                        preferences.add(Preference.getName());
                    }
                }


                new AddPreferencesTask(EnterPreferencesActivity.this, sp.getString("userid", ""), preferences).execute(null, null, null);

//
//                Toast.makeText(getApplicationContext(),
//                        responseText, Toast.LENGTH_LONG).show();

            }
        });
    }


    private class AddPreferencesTask extends AsyncTask<Void, Void, Void> {

        Activity activity;
        ProgressDialog progressdialog;
        String userid;
        ArrayList<String> preferences;

        public AddPreferencesTask(Activity activity, String userid, ArrayList<String> preferences) {

            this.activity = activity;
            this.userid = userid;
            this.preferences = preferences;
        }

        @Override
        protected void onPreExecute() {
            progressdialog = ProgressDialog.show(activity, "",
                    "Please wait...", true,true);

        }

        @Override
        protected Void doInBackground(Void... params) {
            PreferencesService preferencesService = new PreferencesService();
            preferencesService.udpatePreferences(userid, preferences);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            progressdialog.dismiss();
            Toast.makeText(getApplicationContext(),
                    "Your Preferences Updated", Toast.LENGTH_LONG).show();


        }

    }
}
