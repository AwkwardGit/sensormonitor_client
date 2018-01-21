package com.example.neil.sensormonitor;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import java.util.List;
import java.util.StringJoiner;

@Database(entities = {BCReading.class},version = 2, exportSchema = false)

public abstract class AppDatabase extends RoomDatabase {
    public abstract BCReadingDao bcReadingDao();

    private String BCSyncData(int siteId) {
        List<BCReading> xs = bcReadingDao().getUnsaved();
        StringJoiner sj = new StringJoiner(",");
        for(BCReading x : xs) {
            sj.add(x.toJSON(siteId).toString());
        }
        return "[" + sj.toString() + "]";
    }
}

