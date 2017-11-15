package com.location.reminder;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;
import com.location.reminder.com.location.reminder.restcalls.java.NearbyLocationsService;
import com.location.reminder.model.Locationhistory;
import com.location.reminder.util.Constants;

import java.util.ArrayList;

public class ReminderNearbyPlacesActivity extends BaseActivity {

    int reminderid ;
    ArrayList<Locationhistory> preferenceReminders = new ArrayList<Locationhistory>();
    private NearbyPlacesAdapter adapter;
    ListView listView1;

    String querytype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Near by locations for Reminder");
        setContentView(R.layout.activity_remindernearbyplaces);

    }


    @Override
    public void onResume() {
        super.onResume();
        reminderid = getIntent().getIntExtra("reminderid",0);

        GPSLocator locator = new GPSLocator(this);
        locator.getMyLocation();
        double latitude = locator.getLatitude();
        double longitude = locator.getLongitude();
        LatLng currentLocation = new LatLng(latitude, longitude);
        listView1 = (ListView) findViewById(R.id.listView);
        adapter =

                new NearbyPlacesAdapter(this, 0, preferenceReminders, currentLocation, getFontAwesomeTf());
        listView1.setAdapter(adapter);
        loadnearbyplaces(sp.getString("userid", ""), "" + latitude, "" + longitude);

    }


    AsyncTask<Void, Void, Void> task;
    ProgressDialog dialog;


    public void loadnearbyplaces(final String uid, final String lat, final String lng) {


        querytype = "nearbyplacesbyreminder";
        task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                preferenceReminders.clear();
                NearbyLocationsService nearbyLocationsService = new NearbyLocationsService();
                nearbyLocationsService.querystring = ""+reminderid;
                preferenceReminders.addAll(nearbyLocationsService.getNearbyLocations(uid, lat, lng, querytype,sp.getInt("maxdistance", Constants.DEFAULT_PREFLOC_DISTANCE)));

                return null;
            }

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();
                dialog = ProgressDialog.show(ReminderNearbyPlacesActivity.this, "",
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
