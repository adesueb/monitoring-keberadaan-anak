package org.ade.monitoring.keberadaan.map;

import org.ade.monitoring.keberadaan.R;

import com.google.android.maps.MapView;

import android.app.Activity;
import android.os.Bundle;


public class Peta extends Activity{

  
  	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.monitoring_map);
		MapView mapView = (MapView) findViewById(R.id.mapview);
	    mapView.setBuiltInZoomControls(true);
	    
	}

	public void tampilkanPeta(  ){
  	}

  	public long getLongitude(  ){
	  
  		return 0;
  	}


  	public long getLatitude(  ){
	  
  		return 0;
  	}

  	public void setLongitude( long longitude ){
  	}

  

  	public void setLatitude( long latitude ){
  	}


}
