package com.rovin.uber;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Switch;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class MainActivity extends AppCompatActivity {

    public void redirectActivity() {

        if (ParseUser.getCurrentUser().getString("riderOrDriver").equals("rider")) {

            Intent intent = new Intent(getApplicationContext(), RiderActivity.class);
            startActivity(intent);

        } else {

            Intent intent = new Intent(getApplicationContext(), ViewRequestsActivity.class);
            startActivity(intent);


        }
    }

    public void getStarted(View view) {

        Switch userTypeSwitch = (Switch) findViewById(R.id.userTypeSwitch);

        Log.i("Switch value", String.valueOf(userTypeSwitch.isChecked()));

        String userType = "rider";

        if (userTypeSwitch.isChecked()) {

            userType = "driver";

        }

        if(ParseUser.getCurrentUser() != null) {
            ParseUser.logOut();
        }

        final String finalUserType = userType;

        ParseAnonymousUtils.logIn(new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {

                if (e == null) {

                    Log.i("Info", "Anonymous login successful as " + finalUserType);
                    Log.i("Info", "Username: " + user.getUsername());

                    user.put("riderOrDriver", finalUserType);

                    user.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {

                            redirectActivity();

                        }
                    });

                } else {

                    Log.i("Info", "Anonymous login failed");

                }

            }
        });


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        if (ParseUser.getCurrentUser() == null) {
            /*

            ParseAnonymousUtils.logIn(new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {

                    if (e == null) {

                        Log.i("Info", "Anonymous login successful");

                    } else {

                        Log.i("Info", "Anonymous login failed");

                    }

                }
            });
            */

        } else {

            if (ParseUser.getCurrentUser().get("riderOrDriver") != null) {

                Log.i("Info", "Redirecting as " + ParseUser.getCurrentUser().get("riderOrDriver"));

                redirectActivity();

            }


        }

        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }
}
