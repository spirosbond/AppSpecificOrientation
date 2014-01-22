package com.spydiko.rotationmanager;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.util.Log;

import com.jirbo.adcolony.AdColony;
import com.winsontan520.wversionmanager.library.WVersionManager;

/**
 * Created by PuR3v1L on 7/8/2013.
 */
public class AppSpecificOrientation extends Application {

	public static final boolean LOG = true;
	private static final String TAG = AppSpecificOrientation.class.getSimpleName();
	//	public static boolean ALREADY_SHOWED;
	//	public static boolean RETURN_FROM_ABOUT;
	private static SharedPreferences prefs;
	private static SharedPreferences.Editor editor;
	private static int check_button;

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

	/**
	 * 0: Auto-rotate On
	 * 1: Auto-rotate Off
	 * 2: Force Portrait
	 * 3: Force Landscape
	 * 4: Forced Auto
	 */
	public static int getCheck_button() {
		return check_button;
	}

	public static void setCheck_button(int check_button) {
		AppSpecificOrientation.check_button = check_button % 5;
	}

	public static void saveState() {
		editor.putInt("4state", check_button);
		editor.commit();
	}

	public static void loadState() {
		check_button = prefs.getInt("4state", 0);
	}

	public static boolean isPermNotification() {
		return prefs.getBoolean("perm_notification", false);

	}

	public static void setPermNotification(boolean permNotification) {
		editor.putBoolean("perm_notification", permNotification);
		editor.commit();
	}

	public boolean loadDonate(String type) {
		return prefs.getBoolean(type, false);
	}

	public boolean checkIfFirstTime() {
		return prefs.getBoolean("notfirsttime", false);
	}

	public void setNotFirstTime() {
		editor.putBoolean("notfirsttime", true);
		editor.commit();
	}

	@Override
	public void onCreate() {
		super.onCreate();
		//		ALREADY_SHOWED = false;
		//		RETURN_FROM_ABOUT = false;
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		editor = prefs.edit();
		loadState();
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
	//	public void registerShared(DonateActivity donateActivity) {
	//		prefs.registerOnSharedPreferenceChangeListener(donateActivity);
	//	}
	//
	//	public void unregisterShared(DonateActivity donateActivity) {
	//		prefs.unregisterOnSharedPreferenceChangeListener(donateActivity);
	//	}

	public boolean isCheckedLandscape(String temp) {
		return prefs.getBoolean(temp.concat("landscape"), false);
	}

	public boolean isCheckedPortrait(String temp) {
		return prefs.getBoolean(temp.concat("portrait"), false);
	}

	public void configureAdColony(Activity act) {
		try {
			if (AppSpecificOrientation.LOG) Log.d(TAG, "version Code: " + getPackageManager().getPackageInfo(getPackageName(), 0).versionCode);
			AdColony.configure(act, "version=" + getPackageManager().getPackageInfo(getPackageName(), 0).versionCode + ",store:google", "appc0bebfc9f4a3489fb82153", "vz9bf8a5eb30ef477798b82b"/*, "vz81c21390fa4e4b25aaa8ed", "vzf738e644f1394a9abcf4cf", "vz6494ace59eb4446db403f4"*/);
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void chechForUpdate(Activity activity) {
		WVersionManager versionManager = new WVersionManager(activity);
		versionManager.setVersionContentUrl("https://dl.dropbox.com/s/v3qzyxgrmkr1nh3/RotationManager_update"); // your update content url, see the response format below
		versionManager.setUpdateNowLabel(getResources().getString(R.string.update_now_label));
		versionManager.setRemindMeLaterLabel(getResources().getString(R.string.remind_me_later));
		versionManager.setIgnoreThisVersionLabel(getResources().getString(R.string.ignore));
		versionManager.setReminderTimer(10); // this mean checkVersion() will not take effect within 10 minutes
		versionManager.checkVersion();
	}
}
