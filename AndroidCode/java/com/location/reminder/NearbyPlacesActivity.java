package com.location.reminder;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;
import com.location.reminder.com.location.reminder.restcalls.LoadPreferencesService;
import com.location.reminder.com.location.reminder.restcalls.PreferencesService;
import com.location.reminder.com.location.reminder.restcalls.java.NearbyLocationsService;
import com.location.reminder.model.Locationhistory;
import com.location.reminder.model.Preference;
import com.location.reminder.model.PreferenceReminder;
import com.location.reminder.util.Constants;

import java.util.ArrayList;

public class NearbyPlacesActivity extends BaseActivity {

    ArrayList<Locationhistory> preferenceReminders = new ArrayList<Locationhistory>();
    private NearbyPlacesAdapter adapter;
    ListView listView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_places);
        listView1 = (ListView) findViewById(R.id.reminder_list);

        GPSLocator locator = new GPSLocator(this);
        locator.getMyLocation();
        double latitude = locator.getLatitude();
        double longitude = locator.getLongitude();
        LatLng currentLocation = new LatLng(latitude, longitude);

        adapter =

                new NearbyPlacesAdapter(this, 0, preferenceReminders, currentLocation,getFontAwesomeTf());
        listView1.setAdapter(adapter);

        loadnearbyplaces(sp.getString("userid", ""), "" + latitude, "" + longitude);
        setTitle("Suggestions");
    }

    AsyncTask<Void, Void, Void> task;
    ProgressDialog dialog;

    public void loadnearbyplaces(final String uid, final String lat, final String lng) {

        task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {


                preferenceReminders.clear();
                NearbyLocationsService nearbyLocationsService = new NearbyLocationsService();
                preferenceReminders.addAll(nearbyLocationsService.getNearbyLocations(uid, lat, lng,"suggestions",sp.getInt("maxdistance", Constants.DEFAULT_PREFLOC_DISTANCE)));

                return null;
            }

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();
                dialog = ProgressDialog.show(NearbyPlacesActivity.this, "",
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
