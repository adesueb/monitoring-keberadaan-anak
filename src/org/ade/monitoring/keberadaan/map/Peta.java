package org.ade.monitoring.keberadaan.map;

import org.ade.monitoring.keberadaan.R;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;


public class Peta extends MapActivity{

  
  	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.monitoring_map);
		MapView mapView = (MapView) findViewById(R.id.mapview);
	    mapView.setBuiltInZoomControls(true);
	    
	}
  	
  	@Override
	protected boolean isRouteDisplayed() {
		return false;
		
	}
  	
  	@Override
	protected Dialog onCreateDialog(int id) {
		return super.onCreateDialog(id);
	}



	private final Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			Bundle bundle = msg.getData();
			if(bundle!=null){
				Intent intent = new Intent();
				intent.putExtras(bundle);
				Peta.this.setResult(RESULT_OK, intent);
			}
		}
  		
  	};

	
}
