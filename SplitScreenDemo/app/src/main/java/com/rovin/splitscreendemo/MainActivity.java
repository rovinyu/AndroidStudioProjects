package com.rovin.splitscreendemo;

import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    public void onMultiWindowModeChanged(boolean isInMultiWindowMode, Configuration newConfig) {
        super.onMultiWindowModeChanged(isInMultiWindowMode, newConfig);

        // Multi-window mode has changed
        Log.i("Info", "Multi-window mode has change!");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (isInMultiWindowMode()) {

            // We're in multi window mode!
            Log.i("Info", "We're in multi window mode!");

        }

        if (isInPictureInPictureMode()) {

            // We're in picture in picture mode
            Log.i("Info", "We're in picture in picture mode!");

        }
    }
}
