package com.rovin.basicphrases;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void buttonTapped(View view) {

        int id = view.getId();
        String ourId = "";

        ourId = view.getResources().getResourceEntryName(id);

        Log.i("Package",getPackageName());

        int resourceId = getResources().getIdentifier(ourId, "raw", getPackageName());

        MediaPlayer mplayer = MediaPlayer.create(this, resourceId);
        mplayer.start();

        Log.i("button tapped", ourId);

    }

}
