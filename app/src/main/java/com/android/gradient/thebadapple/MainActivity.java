package com.android.gradient.thebadapple;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.os.Handler;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity{

    private final static long Interval = 20;

    private GameView gameView;
    private Handler handler = new Handler();
    private  boolean hasFinished;

    private int lastScore;
    private int bestScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lastScore = bestScore = 0;
    }



    public void NewGame_onClick(View v) {

        gameView = new GameView(this);
        setContentView(gameView);
        hasFinished = false;

        final Timer timer = new Timer();
        final TimerTask task = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run(){
                        gameView.invalidate();
                        if(gameView.getLifes() > 0){
                            gameView.invalidate();
                        }
                        else {
                            timer.cancel();
                            timer.purge();
                            if(!hasFinished) {
                                hasFinished = true;
                                gameOver(gameView.getScore());
                            }
                        }
                    }
                });
            }
        };
        timer.schedule(task, 0, Interval);
    }

    private void gameOver(int score){

        lastScore = score;
        if(lastScore > bestScore) { bestScore = lastScore;}

        Intent intent = new Intent(MainActivity.this, GameOverActivity.class);
        Bundle b = new Bundle();
        b.putInt("LastScore", lastScore);
        b.putInt("BestScore", bestScore);
        intent.putExtras(b);
        startActivityForResult(intent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if(requestCode==2)
        {

            setContentView(R.layout.activity_main);
        }
    }
}