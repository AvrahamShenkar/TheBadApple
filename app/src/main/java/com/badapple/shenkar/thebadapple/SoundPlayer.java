package com.badapple.shenkar.thebadapple;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;


import com.android.gradient.thebadapple.R;

import java.util.List;

/**
 * Created by Avraham on 8/1/2018.
 */

public class SoundPlayer {

     private static DBAccess _db;
     private static boolean isMusicAllowed;
     private static boolean isSoundAllowed;

    private static SoundPool soundPool;
    private static int goodHitSound;
    private static int badHitSound;
    private static int gameOverSound;
    private static int btnPressSound;
    private static MediaPlayer palyer;
    private static BackGroundMusic currbgStyle;
    private Context _context;

    public SoundPlayer(Context context) {
        soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);

        goodHitSound = soundPool.load(context, R.raw.eat, 1);
        badHitSound = soundPool.load(context, R.raw.spray, 1);
        gameOverSound = soundPool.load(context, R.raw.gameover, 1);
        btnPressSound = soundPool.load(context, R.raw.pressbtn, 1);
        currbgStyle = BackGroundMusic.None;

        _context = context;
        LoadDb();

    }

     public static boolean isIsMusicAllowed() {
         return isMusicAllowed;
     }

     public static boolean isIsSoundAllowed() {
         return isSoundAllowed;
     }

     public static void setIsMusicAllowed(boolean isMusicAllowed) {
         SoundPlayer.isMusicAllowed = isMusicAllowed;
         _db.iDbAccess().UpdateTopIsMusicAllowed(isMusicAllowed);
     }

     public static void setIsSoundAllowed(boolean isSoundAllowed) {
         SoundPlayer.isSoundAllowed = isSoundAllowed;
         _db.iDbAccess().UpdateTopIsSoundAllowed(isSoundAllowed);
     }

     private void LoadDb(){
         _db = Room.databaseBuilder(_context, DBAccess.class, "DevDB")
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

         isMusicAllowed = sd._isMusicAllowed;
         isSoundAllowed = sd._isSoundAllowed;
     }

     public void PlayHit(){
        playSount(goodHitSound);
    }

     public void PlaySpray(){
        playSount(badHitSound);
     }

     public void PlayGameOver(){
        playSount(gameOverSound);
     }

     public void PlayPressBTN(){
        playSount(btnPressSound);
     }

     private void playSount(int sound){
        if(isSoundAllowed) {
            soundPool.play(sound, 1.0f, 1.0f, 1, 0, 1.0f);
        }
     }

     public void PlayBGMusic(BackGroundMusic bgStyle) {
         StopMusic();
         currbgStyle = bgStyle;
         if(isMusicAllowed) {
             if (bgStyle == BackGroundMusic.None) {
                 return;
             }
             switch (bgStyle) {
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
     }

     public void ResumeMusic(){
         PlayBGMusic(currbgStyle);
     }

     public void StopMusic() {
        if(palyer!= null) {
            palyer.setLooping(false);
            palyer.stop();
        }
     }




 }
