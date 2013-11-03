package com.spydiko.rotationmanager;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by PuR3v1L on 27/8/2013.
 */
public class AboutActivity extends Activity {

	Intent openFacebook;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) getActionBar().setDisplayHomeAsUpEnabled(true);
		ImageView facebook = (ImageView) findViewById(R.id.facebook_click);
		openFacebook = AppSpecificOrientation.getOpenFacebookIntent(this);
		facebook.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(openFacebook);
			}
		});
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
}

