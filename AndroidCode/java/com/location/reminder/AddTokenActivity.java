package com.location.reminder;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.location.reminder.com.location.reminder.restcalls.GroupsService;

import org.json.JSONArray;
import org.json.JSONException;

public class AddTokenActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_token);
        setTitle("Add token");
    }

    AsyncTask<Void, Void, Void> task;
    ProgressDialog dialog;
    String token = "";

    JSONArray response ;
    public void tokenValidation(View view) {

        EditText tokenet = (EditText) findViewById(R.id.token);
        if (tokenet.getText().toString().trim().equals("") || !tokenet.getText().toString().startsWith("LR")) {

            tokenet.setError("Please enter valid token");
            return;
        }

        token = tokenet.getText().toString().replaceAll("LR", "");

        if (token.equals(sp.getString("userid",""))) {

            tokenet.setError("This is not a valid token");
            return;
        }

        task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                GroupsService groupsService = new GroupsService();

                response =  groupsService.addGroupMember(sp.getString("userid", ""), token);
                return null;
            }

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();
                dialog = ProgressDialog.show(AddTokenActivity.this, "",
                        " Please wait...", true,true);

            }

            @Override
            protected void onPostExecute(Void result) {
                dialog.dismiss();
                try {
                    if(response==null || response.getJSONObject(0).getInt("success")==0){
                        Toast.makeText(getApplicationContext(), "This token can not be added", Toast.LENGTH_SHORT).show();

                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Token added successfully", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        };
        task.execute(null, null, null);

    }
}
