package com.example.neil.sensormonitor;

import android.content.ContentResolver;
import android.content.SyncRequest;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class Listener implements SensorEventListener {
    private MainActivity m;

    public Listener(MainActivity m0) {
        m = m0;
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void onSensorChanged(final SensorEvent event) {
        if (App.get().isActive()) {
            BCReading x = new BCReading(event);
            m.showBCReading(x);
            new InsertBCReadingTask().execute(x);
        }
    }

    private class InsertBCReadingTask extends AsyncTask<BCReading,Void,Long> {
        Long lastId;
        Long numRecords;

        @Override
        protected Long doInBackground(BCReading... xs) {
            AppDatabase db = App.get().getDb();
            for (BCReading x : xs) {
                lastId = db.bcReadingDao().insert(x);
            }

            numRecords = (long) db.bcReadingDao().getCount();

            if (numRecords > 100) {
                db.bcReadingDao().deleteAll();
                numRecords = (long) db.bcReadingDao().getCount();
            }

            Log.i("sensormonitor","T0");
            Uploader.upload();
            return numRecords;
        }

        protected void onPostExecute(Long i) {
            TextView b = m.bottom_view;
            b.setText(String.valueOf(i));
        }
    }
}
