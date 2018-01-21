package com.example.neil.sensormonitor;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class App extends Application {
    public static App INSTANCE;

    AppDatabase db;
    SharedPreferences prefs;

    public static App get() {
        return INSTANCE;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Context c = getApplicationContext();
        // create database
        db = Room.databaseBuilder(c, AppDatabase.class,"db").build();

        INSTANCE = this;

        Log.i("sensormonitor","App() constructor");
    }

    public AppDatabase getDb() {
        return db;
    }

    public SharedPreferences getPrefs() {
        return PreferenceManager.getDefaultSharedPreferences(this);
    }

    public int getSiteId() {
        return Integer.parseInt(getPrefs().getString("siteId","0"));
    }

    public String getSiteName() {
        return getPrefs().getString("siteName","");
    }

    public String getUploadUrl() {
        return getPrefs().getString("uploadUrl","");
    }

    public boolean isActive() {
        return getPrefs().getBoolean("isActive",true);
    }

    public long getLastBCTimestamp() {return getPrefs().getLong("lastBCTimestamp", 0); }

    public void setLastBCTimestamp(Long timestamp) {
        SharedPreferences.Editor editor = getPrefs().edit();
        editor.putLong("lastBCTimestamp", timestamp);
        editor.apply();
    }
}
