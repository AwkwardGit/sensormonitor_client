package com.example.neil.sensormonitor;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface BCReadingDao {
    @Query("SELECT * FROM BCReading")
    List<BCReading> getAll();

    @Query("SELECT * FROM BCReading WHERE serverId=0")
    List<BCReading> getUnsaved();

    @Query("SELECT COUNT(*) FROM BCReading")
    int getCount();

    @Query("SELECT * FROM BCReading WHERE id IN (:ids)")
    List<BCReading> loadAllByIds(int[] ids);

    @Query("SELECT * FROM BCReading WHERE id=:id AND timestamp=:timestamp")
    List<BCReading> getByIdAndTimestamp(int id,int timestamp);

    @Query("UPDATE BCReading SET serverId=:serverId WHERE id=:id AND timestamp=:timestamp")
    void setServerIdByIdAndTimestamp(int serverId,int id,long timestamp);

    @Insert
    long insert(BCReading r);

    @Delete
    void delete(BCReading r);

    @Query("DELETE FROM BCReading")
    void deleteAll();

    @Query("DELETE FROM BCReading WHERE id < :id")
    void deleteBeforeId(long id);

}
