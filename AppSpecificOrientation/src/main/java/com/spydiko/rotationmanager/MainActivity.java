package com.spydiko.rotationmanager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.ContentObserver;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener {

	private final static String TAG = MainActivity.class.getSimpleName();
	protected InteractiveArrayAdapter adapter;
	LinearLayout progBar;
	LinearLayout buttonsLayout;
	LinearLayout globalOrientation;
	private PackageManager packageManager;
	private List<String> names;
	private ArrayList<Model> activities;
	private TextView autoRotate;
	private AppSpecificOrientation myapp;
	private Vibrator vibe;
	private ListView lv;
	private Button buttonClearAll;
	private ImageView orientationButton;
	private ContentObserver rotationObserver = new ContentObserver(new Handler()) {
		@Override
		public void onChange(boolean selfChange) {
			if (android.provider.Settings.System.getInt(getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0) == 1) {
				orientationButton.setImageDrawable(getResources().getDrawable(R.drawable.tb_on));
				autoRotate.setTextColor(Color.GREEN);
				autoRotate.setText(getResources().getText(R.string.orientationOn));
				AppSpecificOrientation.setCheck_button(true);
			} else {
				orientationButton.setImageDrawable(getResources().getDrawable(R.drawable.tb_off));
				autoRotate.setTextColor(Color.RED);
				autoRotate.setText(getResources().getText(R.string.orientationOff));
				AppSpecificOrientation.setCheck_button(false);
			}
		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "destroyed");
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.d(TAG, "stopped");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		myapp = (AppSpecificOrientation) getApplication();
		setContentView(R.layout.activity_main);
		// Initialize everything
		   /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
	          ActionBar actionBar = getActionBar();
            ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#00FEBB31"));
            actionBar.setBackgroundDrawable(colorDrawable);
        }*/

		//		AppFlood.initialize(this, "1lfdyfOzKDcLPRPu", "th4j61EB18bdL522870c8", AppFlood.AD_ALL);
		names = new ArrayList<String>();
		//		if (myapp.loadDonate("appflood2")) AppFlood.showFullScreen(this);
		myapp.configureAdColony(this);
		buttonClearAll = (Button) findViewById(R.id.button2);
		vibe = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
		orientationButton = (ImageView) findViewById(R.id.orientationButton);
		globalOrientation = (LinearLayout) findViewById(R.id.globalOrientation);
		autoRotate = (TextView) findViewById(R.id.orientationText);
		activities = new ArrayList<Model>();
		lv = (ListView) findViewById(R.id.appList);
		final ArrayList<Model> data = (ArrayList<Model>) getLastNonConfigurationInstance();
		// Set Listeners
		orientationButton.setOnClickListener(this);
		buttonClearAll.setOnClickListener(this);
		if (Settings.System.getInt(getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0) == 1) {
			orientationButton.setImageDrawable(getResources().getDrawable(R.drawable.tb_on));
			autoRotate.setTextColor(Color.GREEN);
			autoRotate.setText(getResources().getText(R.string.orientationOn));
			AppSpecificOrientation.setCheck_button(true);
		} else {
			orientationButton.setImageDrawable(getResources().getDrawable(R.drawable.tb_off));
			autoRotate.setTextColor(Color.RED);
			autoRotate.setText(getResources().getText(R.string.orientationOff));
			AppSpecificOrientation.setCheck_button(false);
		}
		// Register Content Observer
		getContentResolver().registerContentObserver(Settings.System.getUriFor(Settings.System.ACCELEROMETER_ROTATION), true,
				rotationObserver);
		// Fill the list
		if (data == null) { // List not stored
			Log.d(TAG, "null");
			packageManager = getPackageManager();
			this.adapter = new InteractiveArrayAdapter(this, activities, (AppSpecificOrientation) getApplication());
			lv.setAdapter(adapter);
			UpdateData updateData = new UpdateData();
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
				updateData.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void[]) null);
			else updateData.execute((Void[]) null);
		} else { // List stored
			Log.d(TAG, "ok");
			activities = data;
			buttonsLayout = (LinearLayout) findViewById(R.id.twoButtons);
			progBar = (LinearLayout) findViewById(R.id.channelsProgress);
			this.adapter = new InteractiveArrayAdapter(this, activities, (AppSpecificOrientation) getApplication());
			lv.setAdapter(adapter);
			for (Model mdl : activities) {
				names.add(mdl.getPackageName());
			}
			progBar.setVisibility(View.GONE);
			lv.setVisibility(View.VISIBLE);
			buttonsLayout.setVisibility(View.VISIBLE);
			globalOrientation.setVisibility(View.VISIBLE);
			adapter.notifyDataSetChanged();
		}
	}

	public Object onRetainNonConfigurationInstance() {
		Log.d(TAG, "onRetain");
		final ArrayList<Model> data = activities;
		return data;
	}

	protected void onResume() {
		super.onResume();
		//        MenuItem temp = (MenuItem) findViewById(R.id.itemToggleService);
		//        if (AppSpecificOrientation.isServiceRunning()) {
		//            temp.setTitle(R.string.titleServiceStart);
		//            temp.setIcon(android.R.drawable.ic_media_pause);
		//        } else {
		//            temp.setTitle(R.string.titleServiceStop);
		//            temp.setIcon(android.R.drawable.ic_media_play);
		//        }
		//registering our receiver
	}

	@Override
	protected void onPause() {
		super.onPause();
		//unregister our receiver
	}

	public void updateApps() {
		//		Log.d(TAG, "0");
		Intent localIntent = new Intent("android.intent.action.MAIN", null);
		localIntent.addCategory("android.intent.category.LAUNCHER");
		//		Log.d(TAG, "1");
		this.adapter = new InteractiveArrayAdapter(this, activities, (AppSpecificOrientation) getApplication());
		packageManager = getPackageManager();
		//		Log.d(TAG, "2");
		List<ResolveInfo> rInfo = packageManager.queryIntentActivities(localIntent, 1);
		//		Log.d(TAG, "3");
		List<ApplicationInfo> packages = new ArrayList<ApplicationInfo>();
		//		Log.d(TAG, "4");
		for (ResolveInfo info : rInfo) {
			packages.add(info.activityInfo.applicationInfo);
		}
		Model temp;
		for (ApplicationInfo packageInfo : packages) {
			//			Log.d(TAG, "Installed package :" + packageInfo.packageName);
			if (names.contains(packageInfo.packageName)) {
				continue;
			}
			names.add(packageInfo.packageName);
			temp = new Model((String) packageManager.getApplicationLabel(packageInfo));
			temp.setPackageName(packageInfo.packageName);
			Drawable pic = packageInfo.loadIcon(packageManager);
			temp.setLabel(pic);
			//			Log.d(TAG, "Installed package :" + temp.getName());
			//temp.put(IS_CHECKED, true);
			if (myapp.loadPreferences(packageInfo.packageName, true)) temp.setSelectedPortrait(true);
			if (myapp.loadPreferences(packageInfo.packageName, false)) temp.setSelectedLandscape(true);
			activities.add(temp);
			//			Log.d(TAG, "Launch Activity :" + packageManager.getLaunchIntentForPackage(packageInfo.packageName));
		}
		// Search and show launchers
		final Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		final ResolveInfo res = packageManager.resolveActivity(intent, 0);
		if (res.activityInfo == null) {
			// should not happen. A home is always installed, isn't it?
		} else if (!names.contains(res.activityInfo.applicationInfo.packageName)) {
			Model launcher = new Model((String) packageManager.getApplicationLabel(res.activityInfo.applicationInfo));
			launcher.setPackageName(res.activityInfo.applicationInfo.packageName);
			Drawable launcher_pic = res.activityInfo.applicationInfo.loadIcon(packageManager);
			launcher.setLabel(launcher_pic);
			if (myapp.loadPreferences(res.activityInfo.applicationInfo.packageName, true)) launcher.setSelectedPortrait(true);
			if (myapp.loadPreferences(res.activityInfo.applicationInfo.packageName, false)) launcher.setSelectedLandscape(true);
			activities.add(launcher);
		}

		Collections.sort(activities, new SortByString());
		Collections.sort(activities, new SortByCheck());
		//		Log.d(TAG, "END");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		//		Log.d(TAG, "createOptions");
		if (AppSpecificOrientation.isServiceRunning()) {
			menu.findItem(R.id.itemToggleService).setTitle(R.string.titleServiceStart);
			menu.findItem(R.id.itemToggleService).setIcon(android.R.drawable.ic_media_pause);
		} else {
			menu.findItem(R.id.itemToggleService).setTitle(R.string.titleServiceStop);
			menu.findItem(R.id.itemToggleService).setIcon(android.R.drawable.ic_media_play);
		}
		if (AppSpecificOrientation.getBoot()) menu.findItem(R.id.setOnBoot).setChecked(true);
		else menu.findItem(R.id.setOnBoot).setChecked(false);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_settings: // Refresh button
				//				Log.d(TAG, "action_settings");
				packageManager = getPackageManager();
				UpdateData updateData = new UpdateData();
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
					updateData.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void[]) null);
				else updateData.execute((Void[]) null);
				//				Log.d(TAG, "execute");
				lv.setAdapter(adapter);
				break;
			case R.id.itemToggleService: // Play - Stop Service
				//                Log.d(TAG, "entered");
				if (AppSpecificOrientation.isServiceRunning()) {
					item.setTitle(R.string.titleServiceStop);
					item.setIcon(android.R.drawable.ic_media_play);
					stopService(new Intent(this, NewOrieService.class));
					AppSpecificOrientation.setServiceRunning(false);
					//                    Log.d(TAG, "if");
				} else {
					item.setTitle(R.string.titleServiceStart);
					item.setIcon(android.R.drawable.ic_media_pause);
					startService(new Intent(this, NewOrieService.class));
					//                    Log.d(TAG, "else");
				}
				break;

			case R.id.setOnBoot: // Set broadcast receiver on or off
				if (AppSpecificOrientation.getBoot()) {
					item.setChecked(false);
					AppSpecificOrientation.setBoot(false);
					//                    Log.d(TAG, "onBoot set to false");
				} else {
					item.setChecked(true);
					AppSpecificOrientation.setBoot(true);
					//                    Log.d(TAG, "onBoot set to true");
				}

				break;
			case R.id.howTo: // Open How To Activity
				startActivityForResult((new Intent(this, HowToActivity.class)), 1);
				break;
			case R.id.about: // Open About Activity
				startActivityForResult((new Intent(this, AboutActivity.class)), 1);
				break;
			case R.id.donate:
				startActivity(new Intent(this, DonateActivity.class));
		}
		return true;
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Collect data from the intent and use it

		//		if (myapp.loadDonate("appflood")) {
		////			AppFlood.showPanel(this, AppFlood.PANEL_TOP);
		//		}

	}

	@Override
	public void onClick(View view) {
		//        Button temp = (Button) view;
		ImageView tmp;
		vibe.vibrate(50);// Vibrate it's time you click something...
		switch (view.getId()) {
			case (R.id.button2):// Clear all button
				for (Model mdl : activities) {
					mdl.setSelectedPortrait(false);
					mdl.setSelectedLandscape(false);
					myapp.savePreferences(mdl.getPackageName(), false, true);
					myapp.savePreferences(mdl.getPackageName(), false, false);
				}
				lv.setAdapter(adapter);
				break;
			case (R.id.orientationButton):// Auto-Rotation button
				tmp = (ImageView) findViewById(view.getId());
				if (AppSpecificOrientation.isCheck_button()) {
					tmp.setImageDrawable(getResources().getDrawable(R.drawable.tb_on));
					autoRotate.setTextColor(Color.GREEN);
					autoRotate.setText(getResources().getText(R.string.orientationOn));
					AppSpecificOrientation.setCheck_button(false);
				} else {
					tmp.setImageDrawable(getResources().getDrawable(R.drawable.tb_off));
					autoRotate.setTextColor(Color.RED);
					autoRotate.setText(getResources().getText(R.string.orientationOff));
					AppSpecificOrientation.setCheck_button(true);
				}
				Settings.System.putInt(getContentResolver(), Settings.System.ACCELEROMETER_ROTATION,
						AppSpecificOrientation.isCheck_button() ? 1 : 0);
				break;
		}
	}

	public class UpdateData extends AsyncTask<Void, Void, Void> {
		LinearLayout progBar;
		LinearLayout buttonsLayout;

		@Override
		protected Void doInBackground(Void... voids) {
			//            Log.d(TAG, "doInBackground");
			updateApps();
			return null;
		}

		@Override
		protected void onPreExecute() {
			//            Log.d(TAG, "onPreExecute1");
			super.onPreExecute();
			//            Log.d(TAG, "onPreExecute2");
			buttonsLayout = (LinearLayout) findViewById(R.id.twoButtons);
			progBar = (LinearLayout) findViewById(R.id.channelsProgress);
			//            Log.d(TAG, "onPreExecute3");
			lv.setVisibility(View.GONE);
			globalOrientation.setVisibility(View.INVISIBLE);
			progBar.setVisibility(View.VISIBLE);
			//            Log.d(TAG, "onPreExecute3");
		}

		@Override
		protected void onPostExecute(Void aVoid) {
			super.onPostExecute(aVoid);
			progBar.setVisibility(View.GONE);
			lv.setVisibility(View.VISIBLE);
			buttonsLayout.setVisibility(View.VISIBLE);
			globalOrientation.setVisibility(View.VISIBLE);
			adapter.notifyDataSetChanged();
		}
	}

}
