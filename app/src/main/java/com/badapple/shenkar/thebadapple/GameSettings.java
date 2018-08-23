package com.badapple.shenkar.thebadapple;

import android.app.Dialog;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.preference.Preference;
import android.view.View;
import android.widget.ImageView;

import com.android.gradient.thebadapple.R;

import java.util.List;

/**
 * Created by Avraham on 8/20/2018.
 */

public class GameSettings  {

    SoundPlayer player;

    private Dialog settingDialog;

    public GameSettings(Context context) {
        settingDialog = new Dialog(context);
        player = new SoundPlayer(context);
    }

    public void openSettings() {
        ImageView exit;
        final ImageView music;
        final ImageView sounds;

        settingDialog.setContentView(R.layout.activity_settings);
        exit = (ImageView) settingDialog.findViewById(R.id.exit_image);
        music = (ImageView) settingDialog.findViewById(R.id.music_image);
        sounds = (ImageView) settingDialog.findViewById(R.id.sounds_image);

        setSoundImage(sounds);
        setMusicImage(music);

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settingDialog.dismiss();
            }
        });

        music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isMusicOn =  !player.isIsMusicAllowed();
                player.setIsMusicAllowed(isMusicOn);
                setMusicImage(music);
                if (isMusicOn) {
                    player.ResumeMusic();
                } else {
                    player.StopMusic();
                }
            }
        });

        sounds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isSoundsOn = !player.isIsSoundAllowed();
                player.setIsSoundAllowed(isSoundsOn);
                setSoundImage(sounds);
            }
        });

        settingDialog.show();
    }

    private void setSoundImage(ImageView sounds){
        if (player.isIsSoundAllowed()) {
            sounds.setImageResource(R.drawable.sound);
        } else {
            sounds.setImageResource(R.drawable.sound_of);
        }
    }

    private void setMusicImage(ImageView music){
        if (player.isIsMusicAllowed()) {
            music.setImageResource(R.drawable.music);

        } else {
            music.setImageResource(R.drawable.music_of);
        }
    }
}


