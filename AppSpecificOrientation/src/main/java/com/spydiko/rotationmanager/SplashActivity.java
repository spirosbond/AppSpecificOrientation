package com.spydiko.rotationmanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by jim on 24/10/2013.
 */


public class SplashActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen);

		Thread logoTimer = new Thread() {
			public void run(){
				try{
					int logoTimer = 0;
					while(logoTimer < 2000){
						sleep(100);
						logoTimer = logoTimer +100;
					};
					startActivity(new Intent("com.spydiko.CLEARSCREEN"));
				}

				catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				finally{
					finish();
				}
			}
		};

		logoTimer.start();
	}
}