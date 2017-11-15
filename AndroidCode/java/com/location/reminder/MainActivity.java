package com.location.reminder;

import android.content.Intent;
import android.os.Bundle;

import com.location.reminder.model.UserInfo;

public class MainActivity extends SocialApiIntegrationActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTitle("Location");
        needhomebutton = false;
        initFacebook();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initSocialApi();
    }

    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();

        if (sp.contains("userid")) {


            UserInfo dto = new UserInfo();
            dto.setEmail(sp.getString("email", ""));
            dto.setUserid(sp.getString("userid", ""));
            dto.setName(sp.getString("name", ""));
            dto.setActivated(sp.getBoolean("activated", true));
            startHomeActivity(dto);
        }
    }

}
