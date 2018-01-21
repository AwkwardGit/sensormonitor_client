package com.example.neil.sensormonitor;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

class Uploader {
    private static final String TAG = "sensormonitor";

    static void upload() {
        URL location;
        HttpURLConnection conn;

        Log.i(TAG,"Uploader.upload()");

        App app = App.get();
        String data = app.getDb().BCSyncData(app.getSiteId());

        try {
            location = new URL(app.getUploadUrl());
        } catch (Exception e) {
            Log.e("sensormonitor","location exception",e);
            return;
        }

        try {
            conn = (HttpURLConnection) location.openConnection();
        } catch (Exception e) {
            Log.e("sensormonitor","conn exception",e);
            return;
        }

        try {
            conn.setReadTimeout( 10000 /*milliseconds*/ );
            conn.setConnectTimeout( 15000 /* milliseconds */ );
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream out = conn.getOutputStream();
            BufferedWriter writer =
                    new BufferedWriter(new OutputStreamWriter(out,"UTF-8"));
            writer.write("data=" +
                    URLEncoder.encode(data,"UTF-8"));
            writer.flush();
            writer.close();
            out.close();
            int responseCode=conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {

                BufferedReader in=new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;

                while((line = in.readLine()) != null) {
                    sb.append(line);
                }

                in.close();
                String response = sb.toString();
                handleResponse(response);
            } else {
                Log.e("sensormonitor","HTTP error");
            }
        } catch(Exception e) {
            Log.e("sensormonitor","sync exception",e);
        } finally {
            conn.disconnect();
        }
    }

    private static void handleResponse(String response) {
        App app = App.get();
        AppDatabase db = app.getDb();

        JSONArray a,b;
        try {
            a = new JSONArray(response);
            for (int i = 0; i < a.length(); i++) {
                b = a.getJSONArray(i);
                if (b.length() == 4) {
                    int siteId = b.getInt(0);
                    int id = b.getInt(1);
                    long timestamp = b.getLong(2);
                    int serverId = b.getInt(3);
                    if (siteId == app.getSiteId()) {
                        db.bcReadingDao().setServerIdByIdAndTimestamp(serverId,id, timestamp);
                    }
                }
            }
        } catch (JSONException e) {
            Log.e("sensormonitor","Invalid JSON in server response");
        }
    }
}
