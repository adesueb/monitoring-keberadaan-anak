package org.ade.monitoring.keberadaan.map;

import org.ade.monitoring.keberadaan.R;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;


public class Peta extends MapActivity{

  
  	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		ambilLokasi = intent.getBooleanExtra("ambilLokasi", false);
		
		setContentView(R.layout.monitoring_map);
		
		ImageView ivSetting = (ImageView) findViewById(R.id.monitoringMapSetting);
		ivSetting.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				
			}
		});
		
		ImageView ivSearch 	= (ImageView) findViewById(R.id.monitoringMapSearch);		
		ivSearch.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				
			}
		});
		
		MapView mapView = (MapView) findViewById(R.id.mapview);
		MapController mapController = mapView.getController();
		mapController.setCenter(null);
		mapController.setZoom(10);
	    mapView.setBuiltInZoomControls(true);
	    setOverlayFactory();
	}
  	
  	@Override
	protected boolean isRouteDisplayed() {
		return false;
		
	}
  	
  	@Override
	protected Dialog onCreateDialog(int id) {
		return super.onCreateDialog(id);
	}
  	
  	private void setOverlayFactory(){
  		if(ambilLokasi){
	    	overlayFactory = new MonitoringOverlayFactory(this, new PetaHandlerPositionNClose(this));
	    }else{
	    	overlayFactory = new MonitoringOverlayFactory(this, null);
	    }
  	}

  	//berhubungan dengan pengambilan lokasi dan close.....
  	
  	private void showAlertClosing(final Bundle bundle){
  		AlertDialog.Builder alert = new AlertDialog.Builder(this);                 
		alert.setTitle("Pengambilan Lokasi");  
		alert.setMessage(
				"latitude : "	+bundle.getDouble("latitude")+
				"longitude : "	+bundle.getDouble("longitude"));                

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {  
	      
			public void onClick(DialogInterface dialog, int whichButton) {  
	          
				actionCloseWithPosition(bundle);
				return;                  
	         }  
	     });  

		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
              return;   
			}
		});
		alert.show();
		
  	}
  	
  	private void actionCloseWithPosition(Bundle bundle){
  		
  		if(bundle!=null){
			Intent intent = new Intent();
			intent.putExtras(bundle);
			setResult(RESULT_OK, intent);
			finish();
		}
  	}


  	private static final class PetaHandlerPositionNClose extends Handler{

		public PetaHandlerPositionNClose(Peta peta){
			mPeta = peta;
		}
		
		@Override
		public void handleMessage(Message msg) {
			Bundle bundle = msg.getData();
			mPeta.showAlertClosing(bundle);
		}
  		private Peta mPeta;
  	}
	//..........................................................................
  	
	private MonitoringOverlayFactory 	overlayFactory;
	private boolean 					ambilLokasi; 
	
}
