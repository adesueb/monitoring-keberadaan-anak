package org.ade.monitoring.keberadaan.test;

import org.ade.monitoring.keberadaan.R;
import org.ade.monitoring.keberadaan.lokasi.GpsManager;
import org.ade.monitoring.keberadaan.util.Status;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

public class TestGps extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_gps);
		mGps = new GpsManager(this, mHandler);
		mGps.searchLokasi();
	}
	Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch(msg.what){
				case Status.SUCCESS :{
					TextView txt = (TextView) findViewById(R.id.txtTest);
					txt.setText("Lokasi : "+
							mGps.getLokasi().getlatitude()+"Latitude"+
							mGps.getLokasi().getLongitude()+"Longitude");
				}
			}
		}
		
	};
	
	private GpsManager mGps;
}
