package com.spydiko.rotationmanager;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Intent;
import android.database.ContentObserver;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

/**
 * Created by spiros on 8/7/13.
 */
public class OrientationService extends Service {


	private static final String TAG = OrientationService.class.getSimpleName();
	//	private OrientationEventListener orientationEventListener;
	private ActivityManager activityManager;
	private String foregroundApp, beforeApp = "";
	private int defaultState;
	private int newState;
	private AppSpecificOrientation appSpecificOrientation;
	private boolean observe = true;
	private ContentObserver rotationObserver = new ContentObserver(new Handler()) {
		@Override
		public void onChange(boolean selfChange) {
			if (observe) {
				try {
					defaultState = Settings.System.getInt(getContentResolver(), Settings.System.ACCELEROMETER_ROTATION);
					appSpecificOrientation.setDefaultState(defaultState);
					if (defaultState == 1) newState = 0;
					else newState = 1;
					appSpecificOrientation.setNewState(newState);
				} catch (Settings.SettingNotFoundException e) {
					Log.d(TAG, "Couldn't find ACCELEROMETER_ROTATION settings");
				}

				Log.d(TAG, "Default state: " + defaultState);
				Log.d(TAG, "New state: " + newState);
			}
		}

	};

	@Override
	public void onCreate() {
		super.onCreate();
		appSpecificOrientation = (AppSpecificOrientation) getApplication();
		Log.d(TAG, "onCreated");
		appSpecificOrientation.setServiceRunning(true);

//        try {
//            defaultState = Settings.System.getInt(getContentResolver(), Settings.System.ACCELEROMETER_ROTATION);
//            if (defaultState == 1) newState = 0;
//            else newState = 1;
//        } catch (Settings.SettingNotFoundException e) {
//            Log.d(TAG, "Couldn't find ACCELEROMETER_ROTATION settings");
//        }
		defaultState = appSpecificOrientation.getDefaultState();
		newState = appSpecificOrientation.getNewState();

		Log.d(TAG, "Default state: " + defaultState);
		Log.d(TAG, "New state: " + newState);

		getContentResolver().registerContentObserver(Settings.System.getUriFor(Settings.System.ACCELEROMETER_ROTATION), true,
				rotationObserver);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			new AppMonitoring().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void[]) null);
		else
			new AppMonitoring().execute((Void[]) null);
		//		orientationEventListener = new OrientationEventListener(getApplicationContext()) {
		//			@Override
		//			public void onOrientationChanged(int i) {
		//				Log.d(TAG, "orientation changed to: " + i + " degrees");
		//
		//				activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		//		get the info from the currently running task
		//				try {
		//					foregroundApp = activityManager.getRunningTasks(1).get(0).topActivity.getClassName();
		//				} catch (NullPointerException e) {
		//					Log.d(TAG, "No foreground app??? Da Fuck???");
		//				}
		//
		//				if (appSpecificOrientation.isChecked(foregroundApp)) {
		//
		//					Settings.System.putInt(getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, newState);
		//
		//
		//				} else {
		//					Settings.System.putInt(getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, defaultState);
		//				}
		//				previousForegroundApp = foregroundApp;
		//			}
		//		};
		//		orientationEventListener.enable();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		appSpecificOrientation.setServiceRunning(false);
		getContentResolver().unregisterContentObserver(rotationObserver);
		Settings.System.putInt(getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, defaultState);
		Log.d(TAG, "rotation " + defaultState);

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		Log.d(TAG, "onStartCommand");
		getContentResolver().registerContentObserver(Settings.System.getUriFor(Settings.System.ACCELEROMETER_ROTATION), true,
				rotationObserver);
		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	public class AppMonitoring extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... voids) {
			while (appSpecificOrientation.isServiceRunning()) {
				activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
				// get the info from the currently running task
				try {
					foregroundApp = activityManager.getRunningTasks(1).get(0).topActivity.getPackageName();
					Log.d(TAG, "Foreground app: " + foregroundApp);
				} catch (NullPointerException e) {
					Log.d(TAG, "No foreground app??? Da Fuck???");
				}
				if (!foregroundApp.equals(beforeApp)) {
					getContentResolver().unregisterContentObserver(rotationObserver);
					beforeApp = foregroundApp;
					observe = false;

					if (appSpecificOrientation.isChecked(foregroundApp)) {

						//getContentResolver().unregisterContentObserver(rotationObserver);
						Settings.System.putInt(getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, newState);
						Log.d(TAG, "rotation " + newState);
						//getContentResolver().registerContentObserver(Settings.System.getUriFor(Settings.System.ACCELEROMETER_ROTATION),
						//true, rotationObserver);


					} else {

						//getContentResolver().unregisterContentObserver(rotationObserver);
						Settings.System.putInt(getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, defaultState);
						Log.d(TAG, "rotation " + defaultState);
						//getContentResolver().registerContentObserver(Settings.System.getUriFor(Settings.System.ACCELEROMETER_ROTATION),
						//true, rotationObserver);

					}
					observe = true;
					getContentResolver().registerContentObserver(Settings.System.getUriFor(Settings.System.ACCELEROMETER_ROTATION),
							true, rotationObserver);
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
			return null;
		}

	}


}
