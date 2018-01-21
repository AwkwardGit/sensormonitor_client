package com.example.neil.sensormonitor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    TextView Bx_view,By_view,Bz_view,Cx_view,Cy_view,Cz_view,BC_time_view,
            bottom_view,siteName_view;
    Sensor BC_sensor;
    Listener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("sensormonitor","MainActivity.onCreate()");

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        findViews();
        showPrefs();
        getSensors();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            // launch settings activity
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void findViews() {
        Log.i("sensormonitor","MainActivity.findViews()");
        Bx_view = findViewById(R.id.Bx);
        By_view = findViewById(R.id.By);
        Bz_view = findViewById(R.id.Bz);
        Cx_view = findViewById(R.id.Cx);
        Cy_view = findViewById(R.id.Cy);
        Cz_view = findViewById(R.id.Cz);
        BC_time_view = findViewById(R.id.BC_time);
        bottom_view = findViewById(R.id.bottomMessage);
        siteName_view = findViewById(R.id.siteName);
    }

    public void getSensors() {
        Log.i("sensormonitor","MainActivity.getSensors()");
        listener = new Listener(this);
        SensorManager m = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        try {
            BC_sensor = m.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED);
            Log.i("sensormonitor","Magnetometer detected");
        } catch (NullPointerException e) {
            BC_sensor = null;
            bottom_view.setText("No magnetometer");
            Log.i("sensormonitor","No magnetometer");
        }

        if (! (BC_sensor == null)) {
            m.registerListener(listener,BC_sensor,10000000);
        }
    }

    public void showBCReading(BCReading r) {
        DecimalFormat df = new DecimalFormat("#.0000");
        Bx_view.setText(df.format(r.Bx));
        By_view.setText(df.format(r.By));
        Bz_view.setText(df.format(r.Bz));
        Cx_view.setText(df.format(r.Cx));
        Cy_view.setText(df.format(r.Cy));
        Cz_view.setText(df.format(r.Cz));
        Date d = new Date(r.timestamp);
        DateFormat tf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
        BC_time_view.setText(tf.format(d));
    }

    private void showPrefs() {
        SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(this);
        siteName_view.setText(p.getString("siteName",""));
    }

    public void deleteAllBCReadings(View view) {
        new DeleteAllBCReadingsTask().execute();
    }

    private static class DeleteAllBCReadingsTask extends AsyncTask<Void,Void,String> {
        @Override
        protected String doInBackground(Void... xs) {
            App.get().getDb().bcReadingDao().deleteAll();
            return "";
        }
    }
}
