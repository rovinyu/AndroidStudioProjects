package com.rovin.wearablepeoplecounter;

import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends WearableActivity {

    private TextView mTextView;
    int val = 0;

    public void onReset(View view) {
        val = 0;
        mTextView.setText(Integer.toString(val));
        Log.i("onReset", "Set textView as 0");
    }

    public void onIncrease(View view) {

        val++;

        mTextView.setText(Integer.toString(val));

        Log.i("onIncrease", "val: " + Integer.toString(val));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mTextView = (TextView) findViewById(R.id.textView);

        // Enables Always-on
        setAmbientEnabled();
    }
}
