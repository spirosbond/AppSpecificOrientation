package com.spydiko.rotationmanager;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

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
        editor.putBoolean("service", serviceRunning);
        editor.commit();
    }

    public static boolean getBoot() {
        return prefs.getBoolean("boot", false);
    }

    public static void setBoot(boolean state) {
        editor.putBoolean("boot", state);
        editor.commit();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        editor = prefs.edit();
    }

    public int getDefaultState() {
        return prefs.getInt("default", -1);

    }

    public void setDefaultState(int ds) {
        editor.putInt("default", ds);
        editor.commit();

    }

    public int getNewState() {
        return prefs.getInt("newstate", -1);
    }

    public void setNewState(int ns) {
        editor.putInt("newstate", ns);
        editor.commit();
    }

    public boolean loadPreferences(String app, Boolean type) {
        if (type) {
            return prefs.getBoolean(app.concat("portrait"), false);
        } else {
            return prefs.getBoolean(app.concat("landscape"), false);
        }
    }

    public void savePreferences(String app, Boolean check, Boolean type) {
        if (type) {
            editor.putBoolean(app.concat("portrait"), check);
        } else {
            editor.putBoolean(app.concat("landscape"), check);
        }
        editor.commit();
    }

    public boolean isCheckedLandscape(String temp) {
        return prefs.getBoolean(temp.concat("landscape"), false);
    }

    public boolean isCheckedPortrait(String temp) {
        return prefs.getBoolean(temp.concat("portrait"), false);
    }
}
