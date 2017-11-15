package com.location.reminder;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.location.reminder.com.location.reminder.restcalls.UserLoginService;
import com.location.reminder.com.location.reminder.restcalls.java.UserRegistrationService;
import com.location.reminder.model.UserInfo;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;

public class UserRegistrationPendingActivity extends BaseActivity {

    private EditText dob;
    private DatePickerDialog dobDialog;
    private SimpleDateFormat dateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration_pending);
        setTitle("User Registration");
        dob = (EditText) findViewById(R.id.dob);
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);


        Calendar newCalendar = Calendar.getInstance();
        dobDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                dob.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        Locale[] locale = Locale.getAvailableLocales();
        ArrayList<String> countries = new ArrayList<String>();
        String country;
        for (Locale loc : locale) {
            country = loc.getDisplayCountry();
            if (country.length() > 0 && !countries.contains(country)) {
                countries.add(country);
            }
        }
        Collections.sort(countries, String.CASE_INSENSITIVE_ORDER);

        Spinner citizenship = (Spinner) findViewById(R.id.country);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, countries);
        citizenship.setAdapter(adapter);


    }


    public void showDobDialog(View view) {
        dobDialog.show();

    }

    public void btn_register(View view) {

        dob.setError(null);
        String dobtext = dob.getText().toString().trim();
        if (dobtext.length() == 0) {
            dob.setError("Date of birth is required");
            return;
        }

        String gender = "Female";
        RadioGroup gendergroup = (RadioGroup) findViewById(R.id.gender);
        int id = gendergroup.getCheckedRadioButtonId();
        if (id == R.id.radioMale) {
            gender = "Male";
        }

        Spinner spinner = (Spinner) findViewById(R.id.country);
        String country = spinner.getSelectedItem().toString();


        new UpdateRegistrationTaskRunner(dobtext, gender, country, sp.getString("userid", ""), this).execute(null, null, null);
    }


    private class UpdateRegistrationTaskRunner extends AsyncTask<Void, Void, Void> {

        private String dob;
        private String gender;

        ProgressDialog progressdialog;
        Activity activity;
        String country;
        String userid;
        UserInfo userInfo;

        public UpdateRegistrationTaskRunner(String dob, String gender, String country, String userid, Activity activity) {
            this.dob = dob;
            this.gender = gender;
            this.country = country;
            this.activity = activity;
            this.userid = userid;
        }

        @Override
        protected Void doInBackground(Void... params) {

            UserRegistrationService userRegistrationService = new UserRegistrationService();
            userInfo = userRegistrationService.updateRegistrationDetails(dob, country, gender, userid);
            userInfo.setUserid(userid);

            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            progressdialog.dismiss();
            startHomeActivity(userInfo);

        }

        @Override
        protected void onPreExecute() {
            progressdialog = ProgressDialog.show(activity, "",
                    "Please wait...", true,true);
        }
    }

}
