package com.spydiko.rotationmanager;

import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

/**
 * Created by PuR3v1L on 23/8/2013.
 */
public class NewOrieService extends Service {

	private static final String TAG = "New Service";
	private static final String PACKAGE_INSTALLER = "com.android.packageinstaller";
	private AppSpecificOrientation appSpecificOrientation;
	private LinearLayout orientationChanger;
	private WindowManager.LayoutParams orientationLayout;
	private String beforeApp;
	private WindowManager wm;
	private boolean isNotification;

	@Override
	public void onDestroy() {
		super.onDestroy();
		AppSpecificOrientation.setServiceRunning(false);
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

		isNotification = AppSpecificOrientation.isPermNotification();
		if (isNotification) {
			createAndStartNotification();
		}


	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		//        if(AppSpecificOrientation.LOG) Log.d(TAG, "MPIKA");
		// Use whatever constant you need for your desired rotation

		if (isNotification != AppSpecificOrientation.isPermNotification()) {
			Log.d(TAG, "Notification Changed");
			if (AppSpecificOrientation.isPermNotification()) {
				createAndStartNotification();
			} else {
				stopForeground(true);
			}
			isNotification = AppSpecificOrientation.isPermNotification();
		}

		return START_STICKY;
	}

	public void createAndStartNotification() {
		Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this).setSmallIcon(R.drawable.nothing).setTicker(getResources().getString(R.string.notification_text))
				.setContentTitle(getResources().getString(R.string.notification_title)).setContentText(getResources().getString(R.string.notification_context_text))
				.setWhen(0).setPriority(NotificationCompat.PRIORITY_MIN).setLargeIcon(image);
		Intent resultIntent = new Intent(this, MainActivity.class);
		// Because clicking the notification opens a new ("special") activity, there's
		// no need to create an artificial back stack.
		PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(resultPendingIntent);
		Notification notification = mBuilder.build();
		startForeground(1337, notification);
	}

	private boolean isLocked() {
		KeyguardManager myKM = (KeyguardManager) this.getSystemService(Context.KEYGUARD_SERVICE);
		if (myKM.inKeyguardRestrictedInputMode()) {
			//			if (AppSpecificOrientation.LOG) Log.d(TAG, "screen is locked");
			return true;
		} else {
			//			if (AppSpecificOrientation.LOG) Log.d(TAG, "screen is NOT locked");
			return false;
		}
	}

	private boolean isScreenOff(boolean enabled) {
		PowerManager powermanager;
		powermanager = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
		if (powermanager.isScreenOn()) {
			//			if (AppSpecificOrientation.LOG) Log.d(TAG, "screen is ON");
			enabled = false;
		} else {
			//			if (AppSpecificOrientation.LOG) Log.d(TAG, "screen is OFF");
		}
		return enabled;

	}

	public class AppMonitoring extends AsyncTask<Void, Integer, Void> {


		@Override
		protected Void doInBackground(Void... voids) {
			String foregroundApp;
			while (AppSpecificOrientation.isServiceRunning()) {
				ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
				// get the info from the currently running task
				if (isLocked()) {
					publishProgress(5);
					beforeApp = "**LOCKED**";
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					continue;
				}
				try {

					ActivityManager.RunningTaskInfo runningTaskInfo = activityManager.getRunningTasks(1).get(0);
					if (runningTaskInfo.topActivity != null) {
						foregroundApp = runningTaskInfo.topActivity.getPackageName();
					} else {
						foregroundApp = beforeApp;
					}
					if (AppSpecificOrientation.LOG) Log.d(TAG, "Foreground app: " + foregroundApp);
					if (!foregroundApp.equals(beforeApp)) {
						beforeApp = foregroundApp;

						if (foregroundApp.equals(PACKAGE_INSTALLER)) {
							publishProgress(5);

						} else if (appSpecificOrientation.isCheckedPortrait(foregroundApp) && appSpecificOrientation.isCheckedLandscape
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
					Thread.sleep(250);
				} catch (NullPointerException e) {
					if (AppSpecificOrientation.LOG) Log.d(TAG, "No foreground app??? Da Fuck???");
					e.printStackTrace();
				} catch (Exception e) {
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
			if (AppSpecificOrientation.LOG) Log.d(TAG, "categ: " + values[0]);
			if (values[0] == 1) {
				if (orientationLayout.screenOrientation != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
					orientationLayout.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
					wm.updateViewLayout(orientationChanger, orientationLayout);
					if (orientationChanger.getVisibility() == View.GONE) orientationChanger.setVisibility(View.VISIBLE);
				}
			}
			if (values[0] == 2) {
				if (orientationLayout.screenOrientation != ActivityInfo.SCREEN_ORIENTATION_SENSOR) {
					orientationLayout.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR;
					wm.updateViewLayout(orientationChanger, orientationLayout);
					if (orientationChanger.getVisibility() == View.GONE) orientationChanger.setVisibility(View.VISIBLE);
				}
			}
			if (values[0] == 3) {
				if (orientationLayout.screenOrientation != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
					orientationLayout.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
					wm.updateViewLayout(orientationChanger, orientationLayout);
					if (orientationChanger.getVisibility() == View.GONE) orientationChanger.setVisibility(View.VISIBLE);
				}
			}
			if (values[0] == 4) {
				if (AppSpecificOrientation.getCheck_button() == 2) {
					if (orientationLayout.screenOrientation != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
						orientationLayout.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
						wm.updateViewLayout(orientationChanger, orientationLayout);
						if (orientationChanger.getVisibility() == View.GONE) orientationChanger.setVisibility(View.VISIBLE);
					}
				} else if (AppSpecificOrientation.getCheck_button() == 3) {
					if (orientationLayout.screenOrientation != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
						orientationLayout.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
						wm.updateViewLayout(orientationChanger, orientationLayout);
						if (orientationChanger.getVisibility() == View.GONE) orientationChanger.setVisibility(View.VISIBLE);
					}
				} else if (AppSpecificOrientation.getCheck_button() == 4) {
					if (orientationLayout.screenOrientation != ActivityInfo.SCREEN_ORIENTATION_SENSOR) {
						orientationLayout.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR;
						wm.updateViewLayout(orientationChanger, orientationLayout);
						if (orientationChanger.getVisibility() == View.GONE) orientationChanger.setVisibility(View.VISIBLE);
					}
				} else {
					if (orientationLayout.screenOrientation != ActivityInfo.SCREEN_ORIENTATION_USER) {
						orientationLayout.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_USER;
						wm.updateViewLayout(orientationChanger, orientationLayout);
						orientationChanger.setVisibility(View.GONE);
					}
				}
			}
			if (values[0] == 5) {
				if (orientationLayout.screenOrientation != ActivityInfo.SCREEN_ORIENTATION_USER) {
					orientationLayout.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_USER;
					wm.updateViewLayout(orientationChanger, orientationLayout);
					orientationChanger.setVisibility(View.GONE);
				}
			}
		}
	}
}
