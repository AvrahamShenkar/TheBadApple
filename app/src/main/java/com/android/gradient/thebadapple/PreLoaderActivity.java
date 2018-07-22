package com.android.gradient.thebadapple;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class PreLoaderActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preload);

        Thread thread = new Thread(){
            @Override
            public void run(){
                try{
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