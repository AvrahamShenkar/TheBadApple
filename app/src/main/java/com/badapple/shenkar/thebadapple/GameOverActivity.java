package com.badapple.shenkar.thebadapple;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.gradient.thebadapple.R;

public class GameOverActivity extends AppCompatActivity{

    private int score;
    private int bestScore;

    private SoundPlayer soundPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
        soundPlayer = new SoundPlayer(this);

        score = 0;
        bestScore = 0;

        Bundle b = getIntent().getExtras();
        if(b != null) {
            score = b.getInt("LastScore");
            bestScore = b.getInt("BestScore");
        }

        TextView  scoreTxt= (TextView) findViewById(R.id.scoreSection);
        TextView  bestScoreTxt= (TextView) findViewById(R.id.bestScoreSection);
        scoreTxt.setText("Score: " + score);
        bestScoreTxt.setText("Best Score: " + bestScore);
    }

    public void StartOver_onClick(View v) {
        soundPlayer.PlayPressBTN();
        super.finish();
    }

}










