package com.location.reminder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class UniqueTokenActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setTitle("Unique token");
        setContentView(R.layout.activity_unique_token);

        TextView unique_token = (TextView)findViewById(R.id.unique_token);
        unique_token.setText("TOKEN : LR"+sp.getString("userid",""));

    }

    public void sharetoken(View view){

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "Use token "+"LR"+sp.getString("userid","")+" to join "+sp.getString("name","");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share token");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }
}
