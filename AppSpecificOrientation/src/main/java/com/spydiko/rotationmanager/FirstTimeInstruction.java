package com.spydiko.rotationmanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by jim on 25/10/2013.
 */
public class FirstTimeInstruction extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.first_time_how_to);
		Button got_it = (Button) findViewById(R.id.got_it_button);
		got_it.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent("com.spydiko.CLEARSCREEN"));
			}
		});
	}
}
