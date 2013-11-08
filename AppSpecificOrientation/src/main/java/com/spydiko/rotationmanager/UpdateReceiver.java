package com.spydiko.rotationmanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.ads.a;

/**
 * Created by jim on 8/11/2013.
 */
public class UpdateReceiver extends BroadcastReceiver {
	private static final String TAG = "UpdateReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(TAG, "mpika");
		AppSpecificOrientation appSpecificOrientation= (AppSpecificOrientation) context.getApplicationContext();
		if (appSpecificOrientation.isServiceRunning()){
			context.startService(new Intent(context, NewOrieService.class));
		}
	}
}