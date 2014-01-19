package com.spydiko.rotationmanager;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

/**
 * Created by PuR3v1L on 27/8/2013.
 */
public class AboutActivity extends Activity {

	Intent openFacebook;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		TextView version = (TextView) findViewById(R.id.aboutFooterTextView);
		try {
			version.setText(version.getText()+" "+getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) getActionBar().setDisplayHomeAsUpEnabled(true);
		//		ImageView facebook = (ImageView) findViewById(R.id.facebook_click);
		//		openFacebook = AppSpecificOrientation.getOpenFacebookIntent(this);
		//		facebook.setOnClickListener(new View.OnClickListener() {
		//			@Override
		//			public void onClick(View v) {
		//				startActivity(openFacebook);
		//			}
		//		});
		AppSpecificOrientation.RETURN_FROM_ABOUT = true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			// Respond to the action bar's Up/Home button
			case android.R.id.home:
				setResult(RESULT_OK, new Intent());
				finish();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void onOpenFacebookClick(View view) {
		try {
			getPackageManager().getPackageInfo("com.facebook.katana", 0);
			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/657088267656391")));
		} catch (Exception e) {
			openBrowser("https://www.facebook.com/rotationmanager");
		}

	}

	public void onOpenXdaClick(View view) {
		openBrowser("http://forum.xda-developers.com/showthread.php?t=2405849");
	}

	public void onOpenGoogleplusClick(View view) {
		openBrowser("https://plus.google.com/116314324772899749159");
	}

	public void onOpenCrowdinClick(View view) {
		openBrowser("http://crowdin.net/project/rotation-manager/invite");
	}

	private void openBrowser(String url) {
		startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(url)));
	}
}

