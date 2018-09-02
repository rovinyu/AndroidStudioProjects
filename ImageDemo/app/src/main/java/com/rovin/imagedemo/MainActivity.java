package com.rovin.imagedemo;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    public void OnClickMe(View view) {
        ImageView catImageView = (ImageView) findViewById(R.id.catImageView);
        Bitmap bitmap = ((BitmapDrawable)getResources().getDrawable(R.drawable.cat)).getBitmap();
        Bitmap cur = ((BitmapDrawable)catImageView.getDrawable()).getBitmap();


        if (cur == bitmap) {
            catImageView.setImageDrawable(getResources().getDrawable(R.drawable.cat2));
            Log.i("Info", "Change image to cat2!");
        } else {
            catImageView.setImageDrawable(getResources().getDrawable(R.drawable.cat));
            Log.i("Info","Change image to cat!");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
