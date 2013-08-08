package com.spydiko.appspecificorientation;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener {

    private final static String TAG = MainActivity.class.getSimpleName();
    private PackageManager packageManager;
    private InteractiveArrayAdapter adapter;
    private List<Model> activities;
    private AppSpecificOrientation myapp;
    private ListView lv;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myapp = (AppSpecificOrientation) getApplication();
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);
        activities = new ArrayList<Model>();
        lv = (ListView) findViewById(R.id.appList);
        this.adapter = new InteractiveArrayAdapter(this, activities);
        lv.setAdapter(adapter);
        packageManager = getPackageManager();


        Intent localIntent3 = new Intent("android.intent.action.MAIN",null);
        localIntent3.addCategory("android.intent.category.LAUNCHER");


        List<ResolveInfo> rInfo = packageManager.queryIntentActivities(localIntent3,1);

        List<ApplicationInfo> packages = new ArrayList<ApplicationInfo>();

        for (ResolveInfo info : rInfo) {
            packages.add(info.activityInfo.applicationInfo);
        }
        Model temp;
        for (ApplicationInfo packageInfo : packages) {
            Log.d(TAG, "Installed package :" + packageInfo.className);
            temp = new Model ((String) packageManager.getApplicationLabel(packageInfo));
            Log.d(TAG, "Installed package :" + temp.getName());
            //temp.put(IS_CHECKED, true);
            activities.add(temp);
            Log.d(TAG, "Launch Activity :" + packageManager.getLaunchIntentForPackage(packageInfo.packageName));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onClick(View view) {
        for (Model mdl : activities) {
            if (mdl.isSelected()) {
                Log.d(TAG,mdl.getName());
                mdl.setSelected(false);
            }
        }
        adapter.notifyDataSetChanged();
    }
}
