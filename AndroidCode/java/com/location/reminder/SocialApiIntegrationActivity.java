package com.location.reminder;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;


import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.location.reminder.com.location.reminder.restcalls.UserLoginService;
import com.location.reminder.model.UserInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;


public class SocialApiIntegrationActivity extends BaseActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private CallbackManager callbackManager;
    private LoginButton login_button;

    private ConnectionResult mConnectionResult;
    private boolean mSignInClicked;
    private static final int RC_SIGN_IN = 0;
    private GoogleApiClient mGoogleApiClient;
    SignInButton signInBtn;


    public void initFacebook() {
        FacebookSdk.sdkInitialize(getApplicationContext());

        callbackManager = CallbackManager.Factory.create();

    }

    public void initSocialApi() {

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(Plus.API, Plus.PlusOptions.builder().build())
                .addScope(Plus.SCOPE_PLUS_LOGIN).build();

        login_button = (LoginButton) findViewById(R.id.login_button);
        signInBtn = (SignInButton) findViewById(R.id.signin);

        signInBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                mGoogleApiClient.connect();
            }
        });
        login_button = facebookRegisterCallback(login_button, callbackManager, this);


    }

    public LoginButton facebookRegisterCallback(LoginButton login_button, CallbackManager callbackManager, final BaseActivity activity) {

        login_button.setReadPermissions(Arrays.asList("public_profile, email,user_birthday,user_friends"));

        login_button.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest.newMeRequest(
                        loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject jsonObject, GraphResponse response) {
                                String email = jsonObject.optString("email");
                                String id = jsonObject.optString("id");
                                String name = jsonObject.optString("name");
                                String birthday = jsonObject.optString("birthday");
                                String gender = jsonObject.optString("gender");


                                new FBLoginTaskRunner(id, email, name, birthday, gender, activity).execute(null, null, null);
//                                System.out.println("ID" + id);
                                UserInfo dto = new UserInfo();
                                dto.setEmail(email);
                                dto.setUserid(id);
                                dto.setName(name);
                                dto.setBirthday(birthday);
                                dto.setGender(gender);

                                // activity.startHomeActivity(dto);


                            }
                        }).executeAsync();
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });
        return login_button;
    }


    protected void onStart() {
        super.onStart();

    }

    protected void onStop() {
        super.onStop();

        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        System.out.println(resultCode);
        if (resultCode == RESULT_OK) {

            // if (requestCode == RC_SIGN_IN) {
            // mIntentInProgress = false;
            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
        } else {
//            mGoogleApiClient.c


        }
    }


    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (result.hasResolution()) {
            try {
                //mIntentInProgress = true;
                result.startResolutionForResult(this, RC_SIGN_IN);
            } catch (IntentSender.SendIntentException e) {
                // mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }

    }


    @Override
    public void onConnected(Bundle arg0) {

        System.out.println("called on connected");
        try {
            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
                String name = currentPerson.getDisplayName();
                String email = Plus.AccountApi.getAccountName(mGoogleApiClient);
                int g = currentPerson.getGender();
                String gender = "Fe male";
                if (g == 1) {
                    gender = "Male";
                }
                String birthday = currentPerson.getBirthday();

                new GmailLoginTaskRunner(email, name, gender, birthday, this).execute(null, null, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionSuspended(int arg0) {

    }

    public void postverifiedlogindetails(JSONArray jsonArray) {
        try {
            JSONObject jsonobject = jsonArray.getJSONObject(0);
            if (jsonobject.getString("success").equals("0")) {

                new AlertDialog.Builder(this)
                        .setTitle("Error")
                        .setMessage(jsonobject.getString("failurereason"))
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

                if (mGoogleApiClient != null) {
                    if (mGoogleApiClient.isConnected() || mGoogleApiClient.isConnecting())
                        mGoogleApiClient.disconnect();

                }
            } else {
                UserInfo dto = new UserInfo();
                dto.setEmail(jsonobject.getString("email"));
                dto.setUserid(jsonobject.getString("userid"));
                dto.setName(jsonobject.getString("name"));
                dto.setActivated(jsonobject.getBoolean("activated"));
                startHomeActivity(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class GmailLoginTaskRunner extends AsyncTask<Void, Void, Void> {

        private String email;
        private String name;
        JSONArray jsonArray;
        ProgressDialog progressdialog;
        Activity activity;
        String gender;
        String birthday;

        public GmailLoginTaskRunner(String email, String name, String gender, String birthday, Activity activity) {
            this.email = email;
            this.name = name;
            this.activity = activity;
            this.gender = gender;
            this.birthday = birthday;
        }

        @Override
        protected Void doInBackground(Void... params) {

            UserLoginService login = new UserLoginService();
            jsonArray = login.loginUserGmail(email, name, gender, birthday);
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            progressdialog.dismiss();
            if (jsonArray != null) {
                postverifiedlogindetails(jsonArray);
            }
        }

        @Override
        protected void onPreExecute() {
            progressdialog = ProgressDialog.show(activity, "",
                    "Please wait...", true);
        }
    }

    private class FBLoginTaskRunner extends AsyncTask<Void, Void, Void> {

        private String facebookid;
        private String email;
        private String name;
        private String birthday;
        private String gender;


        JSONArray jsonArray;
        ProgressDialog progressdialog;
        Activity activity;

        public FBLoginTaskRunner(String facebookid, String email, String name, String birthday, String gender, Activity activity) {
            this.facebookid = facebookid;
            this.email = email;
            this.name = name;
            this.birthday = birthday;
            this.gender = gender;
            this.activity = activity;
        }

        @Override
        protected Void doInBackground(Void... params) {

            UserLoginService login = new UserLoginService();
            jsonArray = login.longwithFacebook(facebookid, email, name, birthday, gender);
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            progressdialog.dismiss();
            if (jsonArray != null) {
                postverifiedlogindetails(jsonArray);
            }

        }

        @Override
        protected void onPreExecute() {
            progressdialog = ProgressDialog.show(activity, "",
                    "Please wait...", true,true);
        }

    }
}
