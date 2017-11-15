package com.location.reminder;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.location.reminder.com.location.reminder.restcalls.ReminderService;
import com.location.reminder.com.location.reminder.restcalls.UserLoginService;
import com.location.reminder.com.location.reminder.restcalls.java.LocationService;
import com.location.reminder.model.LocationAddress;
import com.location.reminder.model.Locationhistory;
import com.location.reminder.model.ReminderModel;
import com.location.reminder.util.AppCommons;
import com.location.reminder.util.Constants;
import com.location.reminder.util.Keywords;
import com.location.reminder.util.PlaceCommons;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.json.JSONArray;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CreateNewReminder extends BaseActivity implements
        TimePickerDialog.OnTimeSetListener {

    private Toolbar mToolbar;
    private EditText mTitleText;
    private TextView mFromTimeText, mTimeText, mRepeatText, mRepeatNoText;

    private Calendar mCalendar;
    private int mHour, mMinute;
    private String mTitle;
    private String mTime;
    private String mRepeat = "";
    private String mRepeatNo;
    private String timerestrict = "false";
    private String daterestrict = "false";

    private TextView fromset_date;
    private TextView toset_date;


    private int ifromtime;
    private int iendtime;


    private long ifromdate;
    private long ienddate;

    private static final String KEY_TITLE = "title_key";
    private static final String KEY_TIME = "time_key";
    private static final String KEY_DATE = "date_key";
    private static final String KEY_REPEAT = "repeat_key";
    private static final String KEY_REPEAT_NO = "repeat_no_key";


    RelativeLayout date;
    RelativeLayout RepeatNo;
    RelativeLayout time;
    RelativeLayout from_datelayout;
    RelativeLayout to_datelayout;

    ReminderModel reminderModel = new ReminderModel();

    Keywords keywords = new Keywords();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        needhomebutton = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_reminder);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mTitleText = (EditText) findViewById(R.id.reminder_title);
        mFromTimeText = (TextView) findViewById(R.id.fromset_time);
        mTimeText = (TextView) findViewById(R.id.set_time);
        mRepeatText = (TextView) findViewById(R.id.set_repeat);
        mRepeatNoText = (TextView) findViewById(R.id.set_repeat_no);

        date = (RelativeLayout) findViewById(R.id.date);
        RepeatNo = (RelativeLayout) findViewById(R.id.RepeatNo);
        time = (RelativeLayout) findViewById(R.id.time);

        from_datelayout = (RelativeLayout) findViewById(R.id.from_datelayout);
        to_datelayout = (RelativeLayout) findViewById(R.id.to_datelayout);

        fromset_date = (TextView) findViewById(R.id.fromset_date);
        toset_date = (TextView) findViewById(R.id.toset_date);

        disableorEnableLayout(date, false);
        disableorEnableLayout(RepeatNo, false);
        disableorEnableLayout(time, false);
        disableorEnableLayout(from_datelayout, false);
        disableorEnableLayout(to_datelayout, false);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.add_reminder);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        mRepeat = "false";
        mRepeatNo = Integer.toString(1);

        mCalendar = Calendar.getInstance();
        mHour = mCalendar.get(Calendar.HOUR_OF_DAY);
        mMinute = mCalendar.get(Calendar.MINUTE);

        mTime = mHour + ":" + mMinute;

        // Setup Reminder Title EditText
        mTitleText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTitle = s.toString().trim();
                mTitleText.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Setup TextViews using reminder values
//        mFromTimeText.setText(mTime);
//        mTimeText.setText(mTime);
//        mRepeatNoText.setText(mRepeatNo);

        // To save state on device rotation
        if (savedInstanceState != null) {
            String savedTitle = savedInstanceState.getString(KEY_TITLE);
            mTitleText.setText(savedTitle);
            mTitle = savedTitle;

            String savedTime = savedInstanceState.getString(KEY_TIME);
            mTimeText.setText(savedTime);
            mTime = savedTime;

            String savedDate = savedInstanceState.getString(KEY_DATE);
            mFromTimeText.setText(savedDate);

            String saveRepeat = savedInstanceState.getString(KEY_REPEAT);
            mRepeatText.setText(saveRepeat);
            mRepeat = saveRepeat;

            String savedRepeatNo = savedInstanceState.getString(KEY_REPEAT_NO);
            mRepeatNoText.setText(savedRepeatNo);
            mRepeatNo = savedRepeatNo;

        }

    }

    public void disableorEnableLayout(RelativeLayout layout, boolean enable) {
        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);

            if (enable)
                child.setAlpha(1.0f);
            else
                child.setAlpha(0.3f);

            child.setEnabled(enable);
        }

    }

    // To save state on device rotation
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putCharSequence(KEY_TITLE, mTitleText.getText());
        outState.putCharSequence(KEY_TIME, mTimeText.getText());
        outState.putCharSequence(KEY_DATE, mFromTimeText.getText());
        outState.putCharSequence(KEY_REPEAT, mRepeatText.getText());
        outState.putCharSequence(KEY_REPEAT_NO, mRepeatNoText.getText());
    }

    int PLACE_PICKED = 100;
    Place place;

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKED) {
            if (resultCode == RESULT_OK) {
                place = PlacePicker.getPlace(data, this);


                LatLng latLng = place.getLatLng();
                reminderModel.setLatitude("" + latLng.latitude);
                reminderModel.setLongitude("" + latLng.longitude);

                TextView locationinfo = (TextView) findViewById(R.id.pick_location);
                locationinfo.setVisibility(View.VISIBLE);
                Geocoder gcd = new Geocoder(this, Locale.getDefault());
                try {
                    List<Address> addresses = gcd.getFromLocation(latLng.latitude, latLng.longitude, 1);
                    if (addresses.size() > 0) {

                        locationinfo.setText(addresses.get(0).getAddressLine(0) + " " + addresses.get(0).getAddressLine(1) + " " + addresses.get(0).getAddressLine(2));
                        reminderModel.setReminderinfo(locationinfo.getText().toString());

                    } else {

                        locationinfo.setText(latLng.latitude + " " + latLng.longitude);
                        reminderModel.setReminderinfo(locationinfo.getText().toString());
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public void pickLocation(View view) {

        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        Context context = getApplicationContext();
        try {
            startActivityForResult(builder.build(CreateNewReminder.this), PLACE_PICKED);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }

    }

    boolean fromtime = false;
    boolean fromdate = false;
    SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);


    public void setFromDate(View v) {

        if (!daterestrict.equals("true")) {
            return;
        }

        fromdate = false;
        Calendar now = Calendar.getInstance();
        android.app.DatePickerDialog tpd = new android.app.DatePickerDialog(this, new android.app.DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                newDate.set(Calendar.HOUR_OF_DAY, 0);
                newDate.set(Calendar.MINUTE, 0);
                newDate.set(Calendar.SECOND, 0);
                newDate.set(Calendar.MILLISECOND, 0);

                view.setMinDate(System.currentTimeMillis() - 1000);

                ifromdate = newDate.getTimeInMillis() / 1000L;

                fromset_date.setText(dateFormatter.format(newDate.getTime()));

            }

        }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
        tpd.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

        tpd.show();
    }

    public void setToDate(View v) {
        if (!daterestrict.equals("true")) {
            return;
        }
        fromdate = true;

        Calendar now = Calendar.getInstance();
        android.app.DatePickerDialog tpd = new android.app.DatePickerDialog(this, new android.app.DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                newDate.set(Calendar.HOUR_OF_DAY, 0);
                newDate.set(Calendar.MINUTE, 0);
                newDate.set(Calendar.SECOND, 0);
                newDate.set(Calendar.MILLISECOND, 0);

                view.setMinDate(System.currentTimeMillis() - 1000);

                ienddate = newDate.getTimeInMillis() / 1000L;

                toset_date.setText(dateFormatter.format(newDate.getTime()));

            }

        }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));

        tpd.show();
    }

    public void setTime(View v) {

        if (!timerestrict.equals("true")) {
            return;
        }
        fromtime = false;
        Calendar now = Calendar.getInstance();
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                false
        );
        tpd.setThemeDark(false);
        tpd.show(getFragmentManager(), "Timepickerdialog");
    }

    public void setFromTime(View v) {
        if (!timerestrict.equals("true")) {
            return;
        }
        fromtime = true;

        Calendar now = Calendar.getInstance();
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                false
        );
        tpd.setThemeDark(false);
        tpd.show(getFragmentManager(), "Timepickerdialog");
    }


    // On clicking the repeat switch
    public void onSwitchRepeat(View view) {
        boolean on = ((Switch) view).isChecked();
        if (on) {
            mRepeat = "true";
            disableorEnableLayout(RepeatNo, true);

        } else {
            mRepeat = "false";
            mRepeatText.setText(R.string.repeat_off);
            disableorEnableLayout(RepeatNo, false);

        }
    }


    public void ondaterestrict(View view) {
        boolean on = ((Switch) view).isChecked();
        if (on) {
            daterestrict = "true";
            disableorEnableLayout(from_datelayout, true);
            disableorEnableLayout(to_datelayout, true);

        } else {
            daterestrict = "false";
            disableorEnableLayout(from_datelayout, false);
            disableorEnableLayout(to_datelayout, false);


        }
    }

    public void ontimerestrict(View view) {
        boolean on = ((Switch) view).isChecked();
        if (on) {
            timerestrict = "true";
            disableorEnableLayout(date, true);
            disableorEnableLayout(time, true);

        } else {
            timerestrict = "false";
            disableorEnableLayout(date, false);
            disableorEnableLayout(time, false);

        }
    }

    // On clicking repeat interval button
    public void setRepeatNo(View v) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Enter Number");

        // Create EditText box to input repeat number
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        alert.setView(input);
        alert.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        if (input.getText().toString().length() == 0) {
                            mRepeatNo = Integer.toString(1);
                            mRepeatNoText.setText(mRepeatNo);
                        } else {
                            mRepeatNo = input.getText().toString().trim();
                            mRepeatNoText.setText(mRepeatNo);
                        }
                    }
                });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // do nothing
            }
        });
        alert.show();
    }

    ProgressDialog progressdialog;
    Locationhistory locationhistory;

    public void updateLocation(final double previousLatitude, final double previousLongitude, final long fromtime, final long totime, final String placetypes, final String placeId, final String addressdetails) {

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                LocationService locationService = new LocationService();
                LocationAddress locationAddress = AppCommons.buildAddress(CreateNewReminder.this, previousLatitude, previousLongitude);
                locationService.forceadd = true;
                locationhistory = locationService.addNewLocation(previousLatitude, previousLongitude, fromtime, totime, locationAddress, placetypes, placeId, sp.getString("userid", ""), addressdetails);
                return null;
            }

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();
                progressdialog = ProgressDialog.show(CreateNewReminder.this, "",
                        "Please wait...", true, true);
            }

            @Override
            protected void onPostExecute(Void result) {
                progressdialog.dismiss();
                reminderModel.setLocationid("" + locationhistory.getId());
                new CreateReminderRunner(reminderModel, CreateNewReminder.this, keywords.keywords, sp.getString("userid", "")).execute(null, null, null);

            }
        }.execute(null, null, null);

    }

    public void saveReminder() {


        reminderModel.setUserid(sp.getString("userid", ""));
        reminderModel.setTitle(mTitleText.getText().toString().trim());


        int repeat = 0;
        if (mRepeat.equals("true")) {
            repeat = 1;
        }
        reminderModel.setRepeat(repeat);
        if (!mRepeatNoText.getText().toString().trim().equals(""))
            reminderModel.setRepeatinterval(Integer.parseInt(mRepeatNoText.getText().toString().trim()));


        if (daterestrict.equals("true")) {

            if (ifromdate > 0 && ienddate > 0) {
                reminderModel.setFromdate(ifromdate);
                reminderModel.setTodate(ienddate);
            } else {
                Toast.makeText(getApplicationContext(), "Please enter from date and to dates",
                        Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (timerestrict.equals("true")) {
            if (ifromtime > 0 && iendtime > 0) {
                reminderModel.setFromtime(ifromtime);
                reminderModel.setTotime(iendtime);
            } else {
                Toast.makeText(getApplicationContext(), "Please enter from time and to times",
                        Toast.LENGTH_SHORT).show();
                return;
            }

        }

        if (reminderModel.getFromdate() + reminderModel.getFromtime() > 0 || reminderModel.getTodate() + reminderModel.getTodate() > 0) {
            if ((reminderModel.getFromdate() + reminderModel.getFromtime() >= (reminderModel.getTodate() + reminderModel.getTotime()))) {
                Toast.makeText(getApplicationContext(), "Invalid dates",
                        Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (place != null) {

            double latitude = place.getLatLng().latitude;
            double longitude = place.getLatLng().longitude;
            long fromtime = ifromdate + ifromtime;
            long totime = ienddate + iendtime;
            String addressdetails = place.getName() + " " + place.getAddress();

            updateLocation(latitude, longitude, fromtime, totime, PlaceCommons.getPlaceTypesCSV(place), place.getId(), addressdetails);

        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_reminder, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        TextView pick_location = (TextView) findViewById(R.id.pick_location);

        switch (item.getItemId()) {


            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.save_reminder:
                mTitleText.setText(mTitle);

                if (mTitleText.getText().toString().length() == 0) {
                    mTitleText.setError("Reminder Title cannot be blank!");
                } else if (pick_location.getText().toString().length() == 0) {
                    pick_location.setError("Please pick reminder location!");
                } else if (ifromdate > ienddate) {

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            this);

                    alertDialogBuilder.setTitle("Error");
                    alertDialogBuilder
                            .setMessage("Start date should be less than end date!")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                }
                            });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                } else {
                    saveReminder();
                }
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {


        mHour = hourOfDay;
        mMinute = minute;
        if (minute < 10) {
            mTime = hourOfDay + ":" + "0" + minute;
        } else {
            mTime = hourOfDay + ":" + minute;
        }

        int mins = hourOfDay * 60 + minute;


        if (fromtime) {
            mFromTimeText.setText(mTime);
            ifromtime = mins;

        } else {
            mTimeText.setText(mTime);
            iendtime = mins;

        }
    }


}
