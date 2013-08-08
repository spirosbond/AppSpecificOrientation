package com.spydiko.appspecificorientation;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ToggleButton;

public class MainActivity extends Activity implements View.OnClickListener {

	private static final String TAG = MainActivity.class.getSimpleName();
	private ToggleButton serviceButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreated");
		setContentView(R.layout.activity_main);
		serviceButton = (ToggleButton) findViewById(R.id.serviceButton);
		serviceButton.setOnClickListener(this);
		if(OrientationService.isRunning()) serviceButton.setChecked(true);
//		startService(new Intent(this, OrientationService.class));
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		Log.d(TAG, "onConfigurationChanged");
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		Log.d(TAG, "onRestoredInstanceState");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View view) {
		ToggleButton button = (ToggleButton) view;
		switch (button.getId()) {
			case R.id.serviceButton:
				if (button.isChecked()) {
					startService(new Intent(this, OrientationService.class));
				}
				if (!button.isChecked()) {
					stopService(new Intent(this, OrientationService.class));
				}
		}
	}
}
