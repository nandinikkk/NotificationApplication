package com.location.reminder;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;
import com.location.reminder.com.location.reminder.restcalls.LoadPreferencesService;
import com.location.reminder.com.location.reminder.restcalls.PreferencesService;
import com.location.reminder.model.Preference;
import com.location.reminder.model.Locationhistory;

import java.util.ArrayList;

public class SuggestionsActivity extends BaseActivity {

    ListView listView1;
    ArrayList<Locationhistory> Locationhistorys = new ArrayList<Locationhistory>();
    private NearbyPlacesAdapter adapter;

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

                new NearbyPlacesAdapter(this, 0, Locationhistorys, currentLocation, getFontAwesomeTf());
        listView1.setAdapter(adapter);

        loadSuggestions(sp.getString("userid", ""), "" + latitude, "" + longitude);
        setTitle("Near by places");
    }

    AsyncTask<Void, Void, Void> task;
    ProgressDialog dialog;

    public void loadSuggestions(final String uid, final String lat, final String lng) {

        task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {


//                PreferencesService preferencesService = new PreferencesService();
//                ArrayList<Preference> preferenceArrayList = preferencesService.getPreferences(uid, "getSuggestions");
//
//                Locationhistorys.clear();
//                if(preferenceArrayList.size()>0) {
//                    LoadPreferencesService loadPreferencesService = new LoadPreferencesService();
//                    Locationhistorys.addAll(loadPreferencesService.makeHttpRequest(preferenceArrayList, lat, lng));
//                }

                return null;
            }

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();
                dialog = ProgressDialog.show(SuggestionsActivity.this, "",
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
