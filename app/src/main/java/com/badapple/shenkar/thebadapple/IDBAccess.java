package com.badapple.shenkar.thebadapple;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverters;

import java.util.Date;
import java.util.List;


@Dao
public interface IDBAccess {

    @Query("SELECT * FROM SettingsData")
    List<SettingsData> getAllItem();

    @Insert
    void addItem(SettingsData st);

    @Query("UPDATE SettingsData SET bestScore= :score")
    void UpdateTopBestScore(int score);

    @Query("UPDATE SettingsData SET isMusicAllowed= :flag")
    void UpdateTopIsMusicAllowed(boolean flag);

    @Query("UPDATE SettingsData SET isSoundAllowed= :flag")
    void UpdateTopIsSoundAllowed(boolean flag);

}


