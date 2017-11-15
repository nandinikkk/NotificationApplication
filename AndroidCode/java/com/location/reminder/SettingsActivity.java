package com.location.reminder;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.annotation.IntegerRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.location.reminder.com.location.reminder.restcalls.java.NearbyLocationsService;
import com.location.reminder.com.location.reminder.restcalls.java.UserRegistrationService;
import com.location.reminder.com.location.reminder.restcalls.java.UserWorkLocationsService;
import com.location.reminder.model.UserInfo;
import com.location.reminder.model.User_registrations;
import com.location.reminder.model.Userworklocations;
import com.location.reminder.util.Constants;
import com.location.reminder.util.GsonUtil;
import com.rey.material.widget.Switch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import me.drakeet.materialdialog.MaterialDialog;

public class SettingsActivity extends BaseActivity {

    EditText name;
    EditText maxdistance;
    EditText email;
    com.rey.material.widget.Switch switchMy;

    MyTextView homelocation;
    MyTextView worklocation;
    MyTextView selectedtextview;

    Userworklocations currentWorkLocation;
    Userworklocations currentHomeLocation;

    Switch snotis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        needhomebutton = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        setTitle("Settings");
        snotis = (Switch) findViewById(R.id.suggestion_notification);
        if (sp.getBoolean("snotis", true)) {

            snotis.setChecked(true);
        } else {
            snotis.setChecked(false);

        }

        Switch nnotis = (Switch) findViewById(R.id.nearby_notification);
        if (sp.getBoolean("nnotis", true)) {

            nnotis.setChecked(true);
        } else {
            nnotis.setChecked(false);

        }

        homelocation = (MyTextView) findViewById(R.id.homelocation);
        worklocation = (MyTextView) findViewById(R.id.worklocation);


        name = (EditText) findViewById(R.id.name);
        maxdistance = (EditText) findViewById(R.id.maxdistance);
        email = (EditText) findViewById(R.id.email);

        name.setText(sp.getString("name", ""));
        maxdistance.setText("" + (sp.getInt("maxdistance", Constants.DEFAULT_PREFLOC_DISTANCE)));
        email.setText(sp.getString("email", ""));
        switchMy = (com.rey.material.widget.Switch) findViewById(R.id.switchMy);
        if (sp.getBoolean("shownotifications", true)) {
            switchMy.setChecked(true);
        } else {
            switchMy.setChecked(false);
        }
        setLocationDetails(sp.getString("userid", ""));
    }

    public void goback(View view) {
        finish();
    }

    public void updateSettings(int maxdistance, boolean shownotifications,boolean isSuggestionsChecked) {

        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("maxdistance", maxdistance);
        editor.putBoolean("shownotifications", shownotifications);
        editor.putBoolean("snotis", isSuggestionsChecked);

        editor.commit();
    }

    AsyncTask<Void, Void, Void> task;
    ProgressDialog dialog;
    User_registrations user_registrations;
    MaterialDialog mMaterialDialog;

    public void saveDetails(View view) {

        name.setError(null);
        email.setError(null);
        final String namelabel = name.getText().toString().trim();
        final String emaillabel = email.getText().toString().trim();
        final String userid = sp.getString("userid", "");
        final String maxdistancellabel = maxdistance.getText().toString().trim();
        final boolean ischecked = switchMy.isChecked();
        final boolean isSuggestionsChecked = snotis.isChecked();
        Gson gson = new Gson();
        final String userhomelocation = gson.toJson(currentHomeLocation);
        final String userworklocation = gson.toJson(currentWorkLocation);

        if (namelabel.equals("")) {
            name.setError("This field is required");
            return;
        }

        if (emaillabel.equals("")) {
            email.setError("This field is required");
            return;
        }

        if (maxdistancellabel.equals("")) {
            maxdistance.setError("This field is required");
            return;
        }

        if (Integer.parseInt(maxdistancellabel) < 500) {

            maxdistance.setError("Minimum value is 500");
            return;
        }

        task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                UserRegistrationService userRegistrationService = new UserRegistrationService();
                user_registrations = userRegistrationService.updateSettings(namelabel, emaillabel, userid, userhomelocation, userworklocation);
                return null;
            }

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();
                dialog = ProgressDialog.show(SettingsActivity.this, "",
                        " Please wait...", true, true);

                mMaterialDialog = new MaterialDialog(SettingsActivity.this)
                        .setTitle("Updated")
                        .setMessage("Data updated")
                        .setNegativeButton("Ok", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mMaterialDialog.dismiss();
                            }
                        });

                mMaterialDialog.show();
            }


            @Override
            protected void onPostExecute(Void result) {
                dialog.dismiss();
                if (user_registrations != null) {
                    UserInfo userInfo = new UserInfo();
                    userInfo.setActivated(true);
                    userInfo.setUserid("" + user_registrations.getId());
                    userInfo.setEmail(user_registrations.getEmail());
                    userInfo.setName(user_registrations.getName());
                    login_user(userInfo);
                    updateSettings(Integer.parseInt(maxdistancellabel), ischecked,isSuggestionsChecked);
                }
            }
        };
        task.execute(null, null, null);

    }

    public void onnchange(View view) {
        boolean on = ((Switch) view).isChecked();
        SharedPreferences.Editor editor = sp.edit();

        if (on) {
            editor.putBoolean("nnotis", true);
        } else {
            editor.putBoolean("nnotis", false);
        }
        editor.commit();

    }

    public void onchange(View view) {
        boolean on = ((Switch) view).isChecked();
        SharedPreferences.Editor editor = sp.edit();

        if (on) {
            editor.putBoolean("snotis", true);
        } else {
            editor.putBoolean("snotis", false);
        }
        editor.commit();

    }

    public void setLocationDetails(Userworklocations userworklocations, double latitude, double longitude, String remark, String type) {

        userworklocations.setLatitude(latitude);
        userworklocations.setLongitude(longitude);
        userworklocations.setRemark(remark);
        userworklocations.setType(type);

    }

    int PLACE_PICKED = 100;
    Place place;

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKED) {
            if (resultCode == RESULT_OK) {
                place = PlacePicker.getPlace(data, this);
                LatLng latLng = place.getLatLng();

                Geocoder gcd = new Geocoder(this, Locale.getDefault());
                try {
                    List<Address> addresses = gcd.getFromLocation(latLng.latitude, latLng.longitude, 1);
                    if (addresses.size() > 0) {
                        selectedtextview.setText(addresses.get(0).getAddressLine(0) + " " + addresses.get(0).getAddressLine(1) + " " + addresses.get(0).getAddressLine(2));

                    } else {
                        selectedtextview.setText(latLng.latitude + " " + latLng.longitude);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (selectedtextview.getId() == R.id.worklocation) {
                    setLocationDetails(currentWorkLocation, latLng.latitude, latLng.longitude, selectedtextview.getText().toString(), "work");
                } else {
                    setLocationDetails(currentHomeLocation, latLng.latitude, latLng.longitude, selectedtextview.getText().toString(), "home");

                }
            }
        }
    }

    public void pickLocation(View view) {

        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(SettingsActivity.this), PLACE_PICKED);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }

    }

    public void worklocation(View view) {
        selectedtextview = worklocation;
        pickLocation(view);

    }

    public void homelocation(View view) {
        selectedtextview = homelocation;
        pickLocation(view);
    }


    public void setLocationDetails(final String userid) {

        currentWorkLocation = new Userworklocations();
        currentHomeLocation = new Userworklocations();

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {


                UserWorkLocationsService userWorkLocationsService = new UserWorkLocationsService();
                JSONArray jsonArray = userWorkLocationsService.getUserworkLocations(userid);
                try {
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String workjson = jsonObject.getString("work");
                    String homejson = jsonObject.getString("home");
                    GsonUtil<Userworklocations> gsonUtil = new GsonUtil<Userworklocations>(Userworklocations.class);
                    System.out.println("work json" + workjson);

                    if (workjson != null && !workjson.equals("null")) {
                        currentWorkLocation = gsonUtil.jsonObjectDeocde(workjson);
                    }
                    if (homejson != null && !homejson.equals("null")) {
                        System.out.println("home json" + homejson);
                        currentHomeLocation = gsonUtil.jsonObjectDeocde(homejson);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();
                dialog = ProgressDialog.show(SettingsActivity.this, "",
                        " Please wait...", true, true);
            }

            @Override
            protected void onPostExecute(Void result) {
                dialog.dismiss();
                if (currentHomeLocation != null)
                    homelocation.setText(currentHomeLocation.getRemark());
                if (currentWorkLocation != null)
                    worklocation.setText(currentWorkLocation.getRemark());
            }
        }.execute(null, null, null);
    }

}
