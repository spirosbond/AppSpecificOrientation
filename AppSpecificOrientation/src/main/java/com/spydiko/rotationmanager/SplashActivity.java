package com.spydiko.rotationmanager;

import android.animation.LayoutTransition;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/**
 * Created by jim on 24/10/2013.
 */


public class SplashActivity extends Activity {
	/**
	 * Called when the activity is first created.
	 */
	private ImageView spydikologo;
//	private Animation myFadeInAnimation;
	private long check;
	private AppSpecificOrientation appSpecificOrientation;
//	private boolean end;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen);
		Resources res = getApplicationContext().getResources();
		spydikologo = (ImageView) findViewById(R.id.spydikologo);
		TransitionDrawable transition = (TransitionDrawable)
				res.getDrawable(R.drawable.logoon);
		spydikologo.setImageDrawable(transition);
//		end = false;
		transition.startTransition(2000);
		check = System.currentTimeMillis();
		Thread logoTimer = new Thread() {
			public void run() {
				boolean showed = false;
				try{
					while(System.currentTimeMillis() -check < 2500){
						sleep(100);
					}
					startActivity(new Intent("com.spydiko.FIRSTHOWTO"));
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


/*	@Override
	public void onAnimationStart(Animation animation) {

	}

	@Override
	public void onAnimationEnd(Animation animation) {



	}

	@Override
	public void onAnimationRepeat(Animation animation) {

	}*/
}