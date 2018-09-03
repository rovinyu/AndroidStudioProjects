package com.rovin.sounddemo;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;

import java.util.Timer;
import java.util.TimerTask;

public class    MainActivity extends AppCompatActivity {

    MediaPlayer mplayer;
    AudioManager audioManager;
    SeekBar musicSeekBar;
    Timer timer;
    //Handler handler=new Handler();

    public void onMusicStart(View view) {

        if( timer != null) {
            Log.i("onMusicStart", "timer is " + timer.toString());
            return;
        } else {
            Log.i("onMusicStart", "timer is null ");
        }

        mplayer.start();
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                musicSeekBar.setProgress(mplayer.getCurrentPosition());
            }
        }, 0, 100);
    //    handler.post(start);
    }

    public void onMusicPause(View view) {
        mplayer.pause();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mplayer = MediaPlayer.create(this, R.raw.birds);

        mplayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (timer != null) {
                    timer.cancel();
                    timer = null;
                }
            }
        });

        audioManager = (AudioManager)getSystemService(AUDIO_SERVICE);

        SeekBar volSeekBar = (SeekBar) findViewById(R.id.volSeekBar);
        musicSeekBar = (SeekBar) findViewById(R.id.musicSeekBar);

        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        volSeekBar.setMax(maxVolume);
        volSeekBar.setProgress(currVolume);

        volSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.i("Info", "Current volume value: " + Integer.toString(progress));
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }
        });

        musicSeekBar.setMax(mplayer.getDuration());

        musicSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    //seekBar.setProgress(progress);
                    mplayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    /*
    Runnable start=new Runnable(){

        @Override
        public void run() {
            // TODO Auto-generated method stub
            mplayer.start();
            // Use one handler to update SeekBar
            handler.post(updatesb);

        }

    };
    Runnable updatesb =new Runnable(){

        @Override
        public void run() {
            // TODO Auto-generated method stub
            musicSeekBar.setProgress(mplayer.getCurrentPosition());
            //update every 100ms
            handler.postDelayed(updatesb, 100);
        }

    };
    */
}
