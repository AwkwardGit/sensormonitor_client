package com.example.neil.sensormonitor;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.hardware.SensorEvent;
import android.os.Build;
import android.support.annotation.RequiresApi;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Neil on 07/01/2018.
 */

@Entity
public class BCReading {
    @PrimaryKey(autoGenerate = true)
    public long id;
    public long serverId;

    public long timestamp;
    public double Bx;
    public double By;
    public double Bz;
    public double Cx;
    public double Cy;
    public double Cz;

    public BCReading() {}

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    public BCReading(SensorEvent event) {
        serverId = 0;
        timestamp = event.timestamp;
        Bx = event.values[0];
        By = event.values[1];
        Bz = event.values[2];
        Cx = event.values[3];
        Cy = event.values[4];
        Cz = event.values[5];
    }

    public JSONObject toJSON(int siteId) {
        JSONObject o = new JSONObject();
        try {
            o.put("site_id",siteId);
            o.put("site_reading_id",id);
            o.put("server_id",serverId);
            o.put("timestamp",timestamp);
            o.put("Bx",Bx);
            o.put("By",By);
            o.put("Bz",Bz);
            o.put("Cx",Cx);
            o.put("Cy",Cy);
            o.put("Cz",Cz);
        } catch(JSONException e) {
            ;
        }
        return o;
    }
}
