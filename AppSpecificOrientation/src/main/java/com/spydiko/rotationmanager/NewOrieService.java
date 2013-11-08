package com.spydiko.rotationmanager;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
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
	private String foregroundApp, beforeApp;
	private WindowManager wm;

	@Override
	public void onDestroy() {
		super.onDestroy();
		appSpecificOrientation.setServiceRunning(false);
		orientationChanger.setVisibility(View.GONE);
		//        if(AppSpecificOrientation.LOG) Log.d(TAG, "stopped");
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		//        if(AppSpecificOrientation.LOG) Log.d(TAG, "Created");
		appSpecificOrientation = (AppSpecificOrientation) getApplication();
		AppSpecificOrientation.setServiceRunning(true);
		wm = (WindowManager) this.getSystemService(Service.WINDOW_SERVICE);
		// Using TYPE_SYSTEM_OVERLAY is crucial to make your window appear on top
		// You'll need the permission android.permission.SYSTEM_ALERT_WINDOW
		orientationChanger = new LinearLayout(this);
		orientationLayout = new WindowManager.LayoutParams(WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY, 0, PixelFormat.RGBA_8888);
		orientationLayout.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR;
		wm.addView(orientationChanger, orientationLayout);
		//        if(AppSpecificOrientation.LOG) Log.d(TAG,"Trexw");
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			new AppMonitoring().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void[]) null);
		else new AppMonitoring().execute((Void[]) null);

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		//        if(AppSpecificOrientation.LOG) Log.d(TAG, "MPIKA");
		// Use whatever constant you need for your desired rotation
		return START_STICKY;
	}

	public class AppMonitoring extends AsyncTask<Void, Integer, Void> {


		@Override
		protected Void doInBackground(Void... voids) {
			while (AppSpecificOrientation.isServiceRunning()) {
				activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
				// get the info from the currently running task
				try {

					ActivityManager.RunningTaskInfo runningTaskInfo = activityManager.getRunningTasks(1).get(0);
					if (runningTaskInfo.topActivity != null) {
						foregroundApp = runningTaskInfo.topActivity.getPackageName();
					} else {
						foregroundApp = beforeApp;
					}
					                    if(AppSpecificOrientation.LOG) Log.d(TAG, "Foreground app: " + foregroundApp);
					if (!foregroundApp.equals(beforeApp)) {
						beforeApp = foregroundApp;

						if (appSpecificOrientation.isCheckedPortrait(foregroundApp) && appSpecificOrientation.isCheckedLandscape
								(foregroundApp)) {
							publishProgress(2);
							//                        if(AppSpecificOrientation.LOG) Log.d(TAG,"1st IF");
						} else if (appSpecificOrientation.isCheckedPortrait(foregroundApp)) {
							publishProgress(3);
							//                        if(AppSpecificOrientation.LOG) Log.d(TAG,"2nd IF");
						} else if (appSpecificOrientation.isCheckedLandscape(foregroundApp)) {
							publishProgress(1);
							//                        if(AppSpecificOrientation.LOG) Log.d(TAG,"3rd IF");
						} else {
							publishProgress(4);
						}
					}
//					Thread.sleep(500);
				} catch (NullPointerException e) {
					if (AppSpecificOrientation.LOG) Log.d(TAG, "No foreground app??? Da Fuck???");
					e.printStackTrace();
				}  catch (Exception e) {
					if (AppSpecificOrientation.LOG) Log.d(TAG, "Exception");
					e.printStackTrace();
				}


			}
			return null;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			beforeApp = "";
		}

		@Override
		protected void onPostExecute(Void aVoid) {
			super.onPostExecute(aVoid);
			beforeApp = "";
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
			if (values[0] == 1) {
				//                if(AppSpecificOrientation.LOG) Log.d(TAG,"1 MPIKA");
				orientationLayout.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
				wm.updateViewLayout(orientationChanger, orientationLayout);
				orientationChanger.setVisibility(View.VISIBLE);
				//                if(AppSpecificOrientation.LOG) Log.d(TAG,"1 VGIKA");
			}
			if (values[0] == 2) {
				//                if(AppSpecificOrientation.LOG) Log.d(TAG,"2 MPIKA");
				orientationLayout.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR;
				wm.updateViewLayout(orientationChanger, orientationLayout);
				orientationChanger.setVisibility(View.VISIBLE);
				//                if(AppSpecificOrientation.LOG) Log.d(TAG,"2 VGIKA");
			}
			if (values[0] == 3) {
				//                if(AppSpecificOrientation.LOG) Log.d(TAG,"3 MPIKA");
				orientationLayout.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
				wm.updateViewLayout(orientationChanger, orientationLayout);
				orientationChanger.setVisibility(View.VISIBLE);
				//                if(AppSpecificOrientation.LOG) Log.d(TAG,"3 VGIKA");
			}
			if (values[0] == 4) {
				//                if(AppSpecificOrientation.LOG) Log.d(TAG,"3 MPIKA");
				orientationLayout.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_USER;
				wm.updateViewLayout(orientationChanger, orientationLayout);
				orientationChanger.setVisibility(View.GONE);
				//                if(AppSpecificOrientation.LOG) Log.d(TAG,"3 VGIKA");
			}
		}
	}

}
