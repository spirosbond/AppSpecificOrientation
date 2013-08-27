package com.spydiko.rotationmanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;


/**
 * Created by PuR3v1L on 27/8/2013.
 */
public class HowToActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.how_to_view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Respond to the action bar's Up/Home button
        switch (item.getItemId()) {
            case android.R.id.home:
                setResult(RESULT_OK, new Intent());
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
