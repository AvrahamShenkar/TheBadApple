package com.badapple.shenkar.thebadapple;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by Avraham on 8/22/2018.
 */

@Entity
public class SettingsData{
    @PrimaryKey(autoGenerate = true)
    public int _id;

    @ColumnInfo(name= "bestScore")
    public int _bestScore;

    @ColumnInfo(name= "isMusicAllowed")
    public boolean _isMusicAllowed;

    @ColumnInfo(name= "isSoundAllowed")
    public boolean _isSoundAllowed ;
}
