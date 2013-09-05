package com.spydiko.rotationmanager;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.startapp.android.publish.StartAppAd;

/**
 * Created by PuR3v1L on 7/8/2013.
 */
public class AppSpecificOrientation extends Application {

    private static SharedPreferences prefs;
    private static SharedPreferences.Editor editor;
    private static boolean check_button;


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

    public static boolean isCheck_button() {
        return check_button;
    }

    public static void setCheck_button(boolean check_button) {
        AppSpecificOrientation.check_button = check_button;
    }

    public boolean loadDonate (String type) {
        return prefs.getBoolean(type,false);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        editor = prefs.edit();
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

    public void registerShared(DonateActivity donateActivity) {
        prefs.registerOnSharedPreferenceChangeListener(donateActivity);
    }

    public void unregisterShared(DonateActivity donateActivity) {
        prefs.unregisterOnSharedPreferenceChangeListener(donateActivity);
    }
}
