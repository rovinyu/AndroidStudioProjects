package com.rovin.animations;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import static android.view.View.VISIBLE;

public class MainActivity extends AppCompatActivity {

    static int count = 0;
    public void fade(View view) {
        ImageView bartImageView = (ImageView) findViewById(R.id.bartImageView);
        ImageView homerImageView = (ImageView) findViewById(R.id.homerImageView);

        count++;

        if (count % 2 == 1) {
            bartImageView.animate().alpha(0f).setDuration(2000);
            homerImageView.animate().alpha(1f).setDuration(2000);
            Log.i("Info", "Bart image fade out");
        } else {
            homerImageView.animate().alpha(0f).setDuration(2000);
            bartImageView.animate().alpha(1f).setDuration(2000);
            Log.i("Info", "Homer image fade out");
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
