package com.badapple.shenkar.thebadapple;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;

import com.android.gradient.thebadapple.R;

 /**
 * Created by Avraham on 8/1/2018.
 */

public class SoundPlayer {

    private static SoundPool soundPool;
    private static int goodHitSound;
    private static int badHitSound;
    private static int gameOverSound;
    private static int btnPressSound;
    private static MediaPlayer palyer;
    private Context _context;

    public SoundPlayer(Context context) {
        soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);

        goodHitSound = soundPool.load(context, R.raw.eat, 1);
        badHitSound = soundPool.load(context, R.raw.spray, 1);
        gameOverSound = soundPool.load(context, R.raw.gameover, 1);
        btnPressSound = soundPool.load(context, R.raw.pressbtn, 1);
        _context = context;
    }


    public void PlayHit(){
        soundPool.play(goodHitSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    public void PlaySpray(){
         soundPool.play(badHitSound, 1.0f, 1.0f, 1, 0, 1.0f);
     }

    public void PlayGameOver(){
         soundPool.play(gameOverSound, 1.0f, 1.0f, 1, 0, 1.0f);
     }

    public void PlayPressBTN(){
         soundPool.play(btnPressSound, 1.0f, 1.0f, 1, 0, 1.0f);
     }

     public void PlayBGMusic(BackGroundMusic bgStyle) {
         StopMusic();
         if(bgStyle == BackGroundMusic.None) {return;}
         switch (bgStyle){
             case GameView:
                 palyer = MediaPlayer.create(_context, R.raw.gamemusic);
                 break;
             case MainScreen:
                 palyer = MediaPlayer.create(_context, R.raw.mainmsuic);
                 break;
             default:
         }

         palyer.setLooping(true);
         palyer.start();
     }

     public void StopMusic() {
        if(palyer!= null) {
            palyer.setLooping(false);
            palyer.stop();
        }
     }




 }
