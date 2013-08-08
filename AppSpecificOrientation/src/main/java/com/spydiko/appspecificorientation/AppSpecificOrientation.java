package com.spydiko.appspecificorientation;

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
    SimpleAdapter adapter;
    ListView lv;
    ArrayList<HashMap<String, String>> saved;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    public void createAdapter() {
        //this.adapter = new SimpleAdapter(getApplicationContext(), saved, R.layout.app_row, )
    }

    @Override
    public void onCreate() {
        super.onCreate();
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
    }

    public void loadPreferences(String app) {
        prefs.getBoolean(app, false);
    }

    public void savePreferences(String app, Boolean check) {
        editor.putBoolean(app,check);
        editor.commit();
    }

}
