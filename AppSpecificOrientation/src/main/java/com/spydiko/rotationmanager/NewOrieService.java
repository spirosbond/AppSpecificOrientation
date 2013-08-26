package com.spydiko.rotationmanager;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

/**
 * Created by PuR3v1L on 23/8/2013.
 */
public class NewOrieService extends Service {

    private static final String TAG = "New Service";
    private AppSpecificOrientation appSpecificOrientation;
    private ActivityManager activityManager;
    private LinearLayout orientationChanger;
    private WindowManager.LayoutParams orientationLayout;
    private String foregroundApp, beforeApp = "";
    private WindowManager wm;

    @Override
    public void onDestroy() {
        super.onDestroy();
        appSpecificOrientation.setServiceRunning(false);
        orientationChanger.setVisibility(View.GONE);
        //        Log.d(TAG, "stopped");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //        Log.d(TAG, "Created");
        appSpecificOrientation = (AppSpecificOrientation) getApplication();
        appSpecificOrientation.setServiceRunning(true);
        wm = (WindowManager) this.getSystemService(Service.WINDOW_SERVICE);
        // Using TYPE_SYSTEM_OVERLAY is crucial to make your window appear on top
        // You'll need the permission android.permission.SYSTEM_ALERT_WINDOW
        orientationChanger = new LinearLayout(this);
        orientationLayout = new WindowManager.LayoutParams(WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY, 0, PixelFormat.RGBA_8888);
        orientationLayout.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR;
        wm.addView(orientationChanger, orientationLayout);
        //        Log.d(TAG,"Trexw");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            new AppMonitoring().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void[]) null);
        else new AppMonitoring().execute((Void[]) null);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //        Log.d(TAG, "MPIKA");
        // Use whatever constant you need for your desired rotation
        return START_STICKY;
    }

    public class AppMonitoring extends AsyncTask<Void, Integer, Void> {


        @Override
        protected Void doInBackground(Void... voids) {
            while (appSpecificOrientation.isServiceRunning()) {
                activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
                // get the info from the currently running task
                try {
                    foregroundApp = activityManager.getRunningTasks(1).get(0).topActivity.getPackageName();
                    //                    Log.d(TAG, "Foreground app: " + foregroundApp);
                } catch (NullPointerException e) {
                    //                    Log.d(TAG, "No foreground app??? Da Fuck???");
                }
                if (!foregroundApp.equals(beforeApp)) {
                    beforeApp = foregroundApp;

                    if (appSpecificOrientation.isCheckedPortrait(foregroundApp) && appSpecificOrientation.isCheckedLandscape
                            (foregroundApp)) {
                        publishProgress(2);
                        //                        Log.d(TAG,"1st IF");
                    } else if (appSpecificOrientation.isCheckedPortrait(foregroundApp)) {
                        publishProgress(3);
                        //                        Log.d(TAG,"2nd IF");
                    } else if (appSpecificOrientation.isCheckedLandscape(foregroundApp)) {
                        publishProgress(1);
                        //                        Log.d(TAG,"3rd IF");
                    } else {
                        publishProgress(4);
                    }
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if (values[0] == 1) {
                //                Log.d(TAG,"1 MPIKA");
                orientationLayout.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                wm.updateViewLayout(orientationChanger, orientationLayout);
                orientationChanger.setVisibility(View.VISIBLE);
                //                Log.d(TAG,"1 VGIKA");
            }
            if (values[0] == 2) {
                //                Log.d(TAG,"2 MPIKA");
                orientationLayout.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR;
                wm.updateViewLayout(orientationChanger, orientationLayout);
                orientationChanger.setVisibility(View.VISIBLE);
                //                Log.d(TAG,"2 VGIKA");
            }
            if (values[0] == 3) {
                //                Log.d(TAG,"3 MPIKA");
                orientationLayout.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                wm.updateViewLayout(orientationChanger, orientationLayout);
                orientationChanger.setVisibility(View.VISIBLE);
                //                Log.d(TAG,"3 VGIKA");
            }
            if (values[0] == 4) {
                //                Log.d(TAG,"3 MPIKA");
                orientationLayout.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_USER;
                wm.updateViewLayout(orientationChanger, orientationLayout);
                orientationChanger.setVisibility(View.GONE);
                //                Log.d(TAG,"3 VGIKA");
            }
        }
    }

}
