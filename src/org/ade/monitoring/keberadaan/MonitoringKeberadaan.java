package org.ade.monitoring.keberadaan;

import org.ade.monitoring.keberadaan.test.TestGps;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class MonitoringKeberadaan extends Activity {
	 /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, TestGps.class);
        startActivity(intent);
    }
}