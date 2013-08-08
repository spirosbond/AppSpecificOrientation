package com.spydiko.appspecificorientation;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

/**
 * Created by spiros on 8/7/13.
 */
public class OrientationService extends Service {


	private static final String TAG = OrientationService.class.getSimpleName();
	private static boolean running;
	//	private OrientationEventListener orientationEventListener;
	private ActivityManager activityManager;
	private String foregroundApp;
	private int defaultState;
	private int newState;

	public static boolean isRunning() {
		return running;
	}

	public static void setRunning(boolean running) {
		OrientationService.running = running;
	}
//	private AppSpecificOrientation appSpecificOrientation;

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(TAG, "onCreated");
		running = true;
		try {
			defaultState = Settings.System.getInt(getContentResolver(), Settings.System.ACCELEROMETER_ROTATION);
			if (defaultState == 1) newState = 0;
			else newState = 1;
		} catch (Settings.SettingNotFoundException e) {
			Log.d(TAG, "Couldn't find ACCELEROMETER_ROTATION settings");
		}

		Log.d(TAG, "Default state: " + defaultState);
		Log.d(TAG, "New state: " + newState);


		new AppMonitoring().execute();
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
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);

	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	public class AppMonitoring extends AsyncTask<String, Integer, String> {


		@Override
		protected String doInBackground(String... strings) {
			while (running) {
				activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
				// get the info from the currently running task
				try {
					foregroundApp = activityManager.getRunningTasks(1).get(0).topActivity.getClassName();
					Log.d(TAG, "Foreground app: " + foregroundApp);
				} catch (NullPointerException e) {
					Log.d(TAG, "No foreground app??? Da Fuck???");
				}

//				if (appSpecificOrientation.isChecked(foregroundApp)) {
				if (foregroundApp.contains("com.android.gallery3d.app")) {

					Settings.System.putInt(getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, newState);
					Log.d(TAG, "rotation " + newState);


				} else {
					Settings.System.putInt(getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, defaultState);
					Log.d(TAG, "rotation " + defaultState);

				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
			return "end";
		}
	}


}
