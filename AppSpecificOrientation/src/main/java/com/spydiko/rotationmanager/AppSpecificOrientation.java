package com.spydiko.rotationmanager;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by PuR3v1L on 7/8/2013.
 */
public class AppSpecificOrientation extends Application {

    private static SharedPreferences prefs;
    private static SharedPreferences.Editor editor;

    public static boolean isServiceRunning() {
        return prefs.getBoolean("service", false);
    }

    public static void setServiceRunning(boolean serviceRunning) {
        editor.putBoolean("service",serviceRunning);
        editor.commit();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        editor = prefs.edit();
    }

    public boolean loadPreferences(String app) {
        return prefs.getBoolean(app, false);
    }

    public void savePreferences(String app, Boolean check) {
        editor.putBoolean(app, check);
        editor.commit();
    }

    public boolean isChecked(String temp) {
        return prefs.getBoolean(temp, false);
    }
}
