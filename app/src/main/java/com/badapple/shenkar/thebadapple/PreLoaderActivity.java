package com.badapple.shenkar.thebadapple;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.gradient.thebadapple.R;

public class PreLoaderActivity extends AppCompatActivity{

    private SoundPlayer soundPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preload);
        soundPlayer = new SoundPlayer(this);

        Thread thread = new Thread(){
            @Override
            public void run(){
                try{
                    soundPlayer.PlayHit();
                    sleep(1500);
                }
                catch(Exception ex){
                    ex.printStackTrace();
                }
                finally{
                    Intent intent = new Intent(PreLoaderActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        };
        thread.start();
    }



    @Override
    protected void onPause(){
        super.onPause();

        finish();
    }

}