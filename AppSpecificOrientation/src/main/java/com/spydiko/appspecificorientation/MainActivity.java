package com.spydiko.appspecificorientation;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.ContentObserver;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private final static String TAG = MainActivity.class.getSimpleName();
    private PackageManager packageManager;
    private InteractiveArrayAdapter adapter;
    private List<String> names;
    private List<Model> activities;
    private AppSpecificOrientation myapp;
    private ListView lv;
    private Button button;
    private Boolean test;
    private ToggleButton rotation;
    private ContentObserver rotationObserver = new ContentObserver(new Handler()) {
        @Override
        public void onChange(boolean selfChange) {
            if (android.provider.Settings.System.getInt(getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0) == 1) {
                rotation.setChecked(true);
            } else {
                rotation.setChecked(false);
            }
        }
    };
    private MenuItem itemToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myapp = (AppSpecificOrientation) getApplication();
        test = false;
        button = (Button) findViewById(R.id.button);
        rotation = (ToggleButton) findViewById(R.id.rotation);
        if (android.provider.Settings.System.getInt(getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0) == 1) {
            rotation.setChecked(true);
        } else {
            rotation.setChecked(false);
        }
        getContentResolver().registerContentObserver(Settings.System.getUriFor(Settings.System.ACCELEROMETER_ROTATION), true,
                rotationObserver);
        rotation.setOnCheckedChangeListener(this);
        button.setOnClickListener(this);
        activities = new ArrayList<Model>();
        names = new ArrayList<String>();
        lv = (ListView) findViewById(R.id.appList);
        this.adapter = new InteractiveArrayAdapter(this, activities);
        lv.setAdapter(adapter);
        packageManager = getPackageManager();
    }

    protected void onResume() {
        super.onResume();
        //        MenuItem temp = (MenuItem) findViewById(R.id.itemToggleService);
        //        if (AppSpecificOrientation.isServiceRunning()) {
        //            temp.setTitle(R.string.titleServiceStart);
        //            temp.setIcon(android.R.drawable.ic_media_pause);
        //        } else {
        //            temp.setTitle(R.string.titleServiceStop);
        //            temp.setIcon(android.R.drawable.ic_media_play);
        //        }
        //registering our receiver
    }

    @Override
    protected void onPause() {
        super.onPause();
        //unregister our receiver
    }

    public void updateApps() {
        Intent localIntent = new Intent("android.intent.action.MAIN", null);
        localIntent.addCategory("android.intent.category.LAUNCHER");

        activities.clear();
        names.clear();


        List<ResolveInfo> rInfo = packageManager.queryIntentActivities(localIntent, 1);

        List<ApplicationInfo> packages = new ArrayList<ApplicationInfo>();


        for (ResolveInfo info : rInfo) {
            packages.add(info.activityInfo.applicationInfo);
        }
        Model temp;
        for (ApplicationInfo packageInfo : packages) {
            Log.d(TAG, "Installed package :" + packageInfo.packageName);
            if (names.contains(packageInfo.packageName)) {
                continue;
            }
            names.add(packageInfo.packageName);
            temp = new Model((String) packageManager.getApplicationLabel(packageInfo));
            temp.setPackageName(packageInfo.packageName);
            Drawable pic = packageInfo.loadIcon(packageManager);
            temp.setLabel(pic);
            Log.d(TAG, "Installed package :" + temp.getName());
            //temp.put(IS_CHECKED, true);
            if (myapp.loadPreferences(packageInfo.packageName)) temp.setSelected(true);
            activities.add(temp);
            Log.d(TAG, "Launch Activity :" + packageManager.getLaunchIntentForPackage(packageInfo.packageName));
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        Log.d(TAG,"createOptions");
        itemToggle = menu.findItem(R.id.itemToggleService);
        if (AppSpecificOrientation.isServiceRunning()) {
            menu.findItem(R.id.itemToggleService).setTitle(R.string.titleServiceStart);
            menu.findItem(R.id.itemToggleService).setIcon(android.R.drawable.ic_media_pause);
        } else {
            menu.findItem(R.id.itemToggleService).setTitle(R.string.titleServiceStop);
            menu.findItem(R.id.itemToggleService).setIcon(android.R.drawable.ic_media_play);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                if(AppSpecificOrientation.isServiceRunning()){
                    stopService(new Intent(this,OrientationService.class));
                    itemToggle.setTitle(R.string.titleServiceStop);
                    itemToggle.setIcon(android.R.drawable.ic_media_play);
                }

                UpdateData exec = new UpdateData();
                exec.execute();

                break;
            case R.id.itemToggleService:
                //                if (yamba.isServiceRunning()) {
                //                    stopService(new Intent(this, UpdaterService.class));
                //                } else {
                //                    startService(new Intent(this, UpdaterService.class));
                //                }
                Log.d(TAG, "entered");
                if (AppSpecificOrientation.isServiceRunning()) {
                    item.setTitle(R.string.titleServiceStop);
                    item.setIcon(android.R.drawable.ic_media_play);
                    stopService(new Intent(this, OrientationService.class));
                } else {
                    item.setTitle(R.string.titleServiceStart);
                    item.setIcon(android.R.drawable.ic_media_pause);
                    startService(new Intent(this, OrientationService.class));
                }
                break;
        }
        return true;
    }

    @Override
    public void onClick(View view) {

        for (Model mdl : activities) {
            Log.d(TAG, mdl.getPackageName() + " is " + mdl.isSelected());
            myapp.savePreferences(mdl.getPackageName(), mdl.isSelected());
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        Settings.System.putInt(getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, b ? 1 : 0);
    }

    public class UpdateData extends AsyncTask<String, Integer, String> {
        LinearLayout progBar;
        LinearLayout pic;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progBar = (LinearLayout) findViewById(R.id.channelsProgress);
            pic = (LinearLayout) findViewById(R.id.picture);
            lv.setVisibility(View.GONE);
            pic.setVisibility(View.GONE);
            progBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            updateApps();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progBar.setVisibility(View.GONE);
            lv.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
        }
    }

}
