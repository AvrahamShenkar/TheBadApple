package com.badapple.shenkar.thebadapple;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.RoomDatabase;


@Database(entities = SettingsData.class, version = 1)
public abstract class DBAccess extends RoomDatabase{
    public abstract IDBAccess iDbAccess();


}