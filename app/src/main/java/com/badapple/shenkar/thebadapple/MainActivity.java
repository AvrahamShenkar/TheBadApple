package com.badapple.shenkar.thebadapple;

import android.app.Dialog;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.gradient.thebadapple.R;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity{

    private final static long Interval = 20;


    private GameView gameView;
    private Handler handler = new Handler();
    private  boolean hasFinished;
    private DBAccess _db;

    private GameSettings settingDialog;
    private Dialog tutorialDialog;
    private Dialog aboutDialog;

    private int lastScore;
    private int bestScore;

    private BackGroundMusic currMusic;

    private SoundPlayer soundPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LoadDB();

        lastScore = 0;
        TextView bestScoreVal = (TextView) findViewById(R.id.bestScoreVal);
        bestScoreVal.setText(" " + bestScore);

        soundPlayer = new SoundPlayer(this);
        currMusic = BackGroundMusic.MainScreen;
        soundPlayer.PlayBGMusic(currMusic);

        settingDialog = new GameSettings(this);
        tutorialDialog = new Dialog(this);
        tutorialDialog.setContentView(R.layout.activity_tutorial);

        aboutDialog = new Dialog(this);
        aboutDialog.setContentView(R.layout.activity_about_us);
    }


    public void onSettings_click(View v){
        ImageView exit;
        ImageView music;
        ImageView sounds;

        soundPlayer.PlayPressBTN();
        settingDialog.openSettings();
    }

    public void NewGame_onClick(View v) {
        soundPlayer.StopMusic();
        soundPlayer.PlayPressBTN();
        currMusic = BackGroundMusic.GameView;
        soundPlayer.PlayBGMusic(currMusic);
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

    public void Tutorial_onClick(View v) {
        handleDialog(tutorialDialog);
    }

    public void AboutUs_onClick(View v) {
        handleDialog(aboutDialog);
    }

    private void handleDialog(final Dialog dialog){
        final ImageView exit;
        exit = (ImageView) dialog.findViewById(R.id.exit_image);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void gameOver(int score){
        currMusic = BackGroundMusic.None;
        soundPlayer.PlayBGMusic(currMusic);
        soundPlayer.PlayGameOver();
        lastScore = score;
        if(lastScore > bestScore) {
            bestScore = lastScore;
            saveBestScore();
        }

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
            LoadDB();
            currMusic = BackGroundMusic.MainScreen;
            soundPlayer.PlayBGMusic(currMusic);

            TextView bestScoreVal = (TextView) findViewById(R.id.bestScoreVal);
            bestScoreVal.setText(" " +bestScore);
        }
    }

    private void LoadDB(){
         _db = Room.databaseBuilder(this, DBAccess.class, "DevDB")
                .allowMainThreadQueries()
                .build();

        List<SettingsData> sds = _db.iDbAccess().getAllItem();
        SettingsData sd = null;
        if(sds != null && sds.size() > 0)
        {
            sd = sds.get(0);
        }
        if(sd == null){
            sd = new SettingsData();
            sd._id = 1;
            sd._isMusicAllowed = true;
            sd._isSoundAllowed = true;
            sd._bestScore = 0;
            _db.iDbAccess().addItem(sd);
        }

        bestScore = sd._bestScore;
    }

    private void saveBestScore(){
        _db.iDbAccess().UpdateTopBestScore(bestScore);
    }

    @Override
    protected void onStart() {
        super.onStart();
        soundPlayer.PlayBGMusic(currMusic);
    }

    @Override
    public void onStop(){
        super.onStop();
        soundPlayer.StopMusic();
    }

    @Override
    protected void onDestroy() {
        soundPlayer.StopMusic();
        super.onDestroy();
    }




}