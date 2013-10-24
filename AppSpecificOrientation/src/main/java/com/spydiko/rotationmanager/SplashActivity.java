package com.spydiko.rotationmanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/**
 * Created by jim on 24/10/2013.
 */


public class SplashActivity extends Activity implements Animation.AnimationListener {
	/**
	 * Called when the activity is first created.
	 */
	private ImageView spydikologo;
	private Animation myFadeInAnimation;
//	private long check;
//	private boolean end;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen);
//		end = false;
		spydikologo = (ImageView) findViewById(R.id.spydikologo);
		myFadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fadein);
		if (myFadeInAnimation != null) {
			myFadeInAnimation.setAnimationListener(this);
		}
		spydikologo.startAnimation(myFadeInAnimation);
//		check = System.currentTimeMillis();
/*		Thread logoTimer = new Thread() {
			public void run() {
				boolean showed = false;
				try{
					while(System.currentTimeMillis() -check < 2000){
						sleep(100);
						if (System.currentTimeMillis() -check > 400 && !showed){
							spydikologo.startAnimation(myFadeInAnimation);
							showed = true;
						}
					}
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

		logoTimer.start();*/
	}

	@Override
	public void onAnimationStart(Animation animation) {

	}

	@Override
	public void onAnimationEnd(Animation animation) {
		startActivity(new Intent("com.spydiko.CLEARSCREEN"));
	}

	@Override
	public void onAnimationRepeat(Animation animation) {

	}
}