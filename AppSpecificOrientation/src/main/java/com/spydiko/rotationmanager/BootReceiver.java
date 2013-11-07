package com.spydiko.rotationmanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by spiros on 8/14/13.
 */
public class BootReceiver extends BroadcastReceiver {
	private static final String TAG = BootReceiver.class.getSimpleName();
//	AppSpecificOrientation appSpecificOrientation;

	@Override
	public void onReceive(Context context, Intent intent) {

		if (AppSpecificOrientation.LOG) Log.d(TAG, "onReceived");
//		appSpecificOrientation = (AppSpecificOrientation) context.getApplicationContext();
		if (AppSpecificOrientation.getBoot()) {
			if (AppSpecificOrientation.isServiceRunning()) {
				if (AppSpecificOrientation.LOG) Log.d(TAG, "Restarting Service");
				context.startService(new Intent(context, NewOrieService.class));
			}
		}else {
			AppSpecificOrientation.setServiceRunning(false);
		}
	}
}
