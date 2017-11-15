package com.location.reminder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;


import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.location.reminder.model.UserInfo;
import com.location.reminder.util.Constants;

public class BaseActivity extends AppCompatActivity {

    public SharedPreferences sp;

    protected boolean needhomebutton = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (needhomebutton) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        sp = getSharedPreferences(Constants.sharedPreferences,
                Context.MODE_PRIVATE);
    }

    public Typeface getFontAwesomeTf() {
        return Typeface.createFromAsset(getAssets(), "fonts/fontawesome-webfont.ttf");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void startHomeActivity(UserInfo dto) {


        login_user(dto);
        if (dto.isActivated()) {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, UserRegistrationPendingActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }

    }

    public void login_user(UserInfo dto) {

        SharedPreferences.Editor editor = sp.edit();
        editor.putString("userid", dto.getUserid());
        editor.putString("name", dto.getName());
        editor.putString("email", dto.getEmail());
        editor.putBoolean("activated", dto.isActivated());
        editor.commit();

    }

    public void logout_user() {

        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        if (FacebookSdk.isInitialized()) {
            if (LoginManager.getInstance() != null)
                LoginManager.getInstance().logOut();
        }

    }

}
