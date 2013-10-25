package com.spydiko.rotationmanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by jim on 25/10/2013.
 */
public class HelperActivity extends Activity {

	private AppSpecificOrientation appSpecificOrientation;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		appSpecificOrientation = (AppSpecificOrientation) getApplication();
		if (!appSpecificOrientation.checkIfFirstTime()){
			appSpecificOrientation.setNotFirstTime();
			startActivity(new Intent("com.spydiko.SPLASHSCREEN"));
		} else{
			startActivity(new Intent("com.spydiko.CLEARSCREEN"));
		}
		finish();
	}
}
