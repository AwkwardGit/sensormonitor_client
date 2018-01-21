package com.example.neil.sensormonitor;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;

public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        Preference siteIdPreference    = findPreference("siteId");
        Preference uploadUrlPreference = findPreference("uploadUrl");
        Preference isActivePreference  = findPreference("isActive");

        bindListener(findPreference("siteId"));
        bindListener(findPreference("uploadUrl"));
        bindListener(findPreference("isActive"));

        Context c = this.getContext();
        SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(c);
        listener.onPreferenceChange(siteIdPreference,p.getString("siteId",""));
        listener.onPreferenceChange(uploadUrlPreference,p.getString("uploadUrl",""));
        listener.onPreferenceChange(isActivePreference,p.getBoolean("isActive",true));
    }

    private static void bindListener(Preference preference) {
        preference.setOnPreferenceChangeListener(listener);
    }

    private static Preference.OnPreferenceChangeListener listener =
            new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            String key = preference.getKey();
            String stringValue = newValue.toString();

            if (key.equals("siteId")) {
                ListPreference listPreference = (ListPreference) preference;
                SharedPreferences.Editor e = preference.getEditor();
                int index = listPreference.findIndexOfValue(stringValue);
                if (index >= 0) {
                    String siteName = listPreference.getEntries()[index].toString();
                    preference.setSummary(siteName);
                    e.putString("siteName",siteName);
                    e.commit();
                } else {
                    preference.setSummary(null);
                    e.putString("siteName","");
                    e.commit();
                }
            } else if (key.equals("uploadUrl")) {
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);
                if (index >= 0) {
                    String siteName = listPreference.getEntries()[index].toString();
                    preference.setSummary(siteName);
                } else {
                    preference.setSummary(null);
                }
            }
            return true;
        }
    };
}
