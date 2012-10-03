package org.ade.monitoring.keberadaan.map;

import java.util.List;

import org.ade.monitoring.keberadaan.R;
import org.ade.monitoring.keberadaan.Variable.Entity;
import org.ade.monitoring.keberadaan.Variable.Status;
import org.ade.monitoring.keberadaan.entity.Lokasi;
import org.ade.monitoring.keberadaan.lokasi.GpsManager;
import org.ade.monitoring.keberadaan.storage.PreferenceManager;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;


public class Peta extends MapActivity{

  
  	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		ambilLokasi = intent.getBooleanExtra("ambilLokasi", false);
		
		setContentView(R.layout.monitoring_map);
		ImageView ivSearch 	= (ImageView) findViewById(R.id.monitoringMapSearch);		
		ivSearch.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				
			}
		});
		
		ImageView ivAnak 	= (ImageView) findViewById(R.id.monitoringMapAnak);		
		ivAnak.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				
			}
		});
		
		ImageView ivMonitor 	= (ImageView) findViewById(R.id.monitoringMapMonitoring);		
		ivMonitor.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				
			}
		});
		
		ImageView ivLaporan 	= (ImageView) findViewById(R.id.monitoringMapLaporan);		
		ivLaporan.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				
			}
		});
		
		gpsManager 		= new GpsManager(this, new PetaHandlerPosition(this));
		
		mapView	 		= (MapView) findViewById(R.id.mapview);
		mapController 	= mapView.getController();
		Lokasi lokasi 	= gpsManager.getLastLokasi();
		
		PreferenceManager prefrenceManager = new PreferenceManager(this);
		if(lokasi !=null){
			prefrenceManager.setMapLokasi(lokasi);
		}else{
			lokasi = prefrenceManager.getMapLokasi();
		}
		
		setPetaCenter(lokasi);
		

	    setOverlayFactory();
	    setOverlayOrangtua(lokasi);
		
		setAmbilLokasi();
		
		findLokasiOrangTua();
		
		mapController.setZoom(12);
	    mapView.setBuiltInZoomControls(true);
	}
  	
  	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
  	
  	@Override
	protected Dialog onCreateDialog(int id) {
  		super.onCreateDialog(id);
  		final Dialog dialog = new Dialog(this);
  		dialog.setContentView(R.layout.list_general);
		
		final ListView listView = (ListView) dialog.findViewById(R.id.listGeneral);
		
		
		ArrayAdapter<String>listAdapter = 
				new ArrayAdapter<String>
					(this, android.R.layout.simple_list_item_multiple_choice, Entity.ARR_ENTITY);
		listView.setAdapter(listAdapter);
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		Button buttonOk = (Button) findViewById(R.id.listGeneralButtonOk);
		buttonOk.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				int len = listView.getCount();
				SparseBooleanArray checked = listView.getCheckedItemPositions();
//				haris.clear();
				// TODO : cek yang mw diperlihatkan
				for (int i = 0; i < len; i++){
					 if (checked.get(i)) {
//						 haris.add(i+1);
					 }
				}
				
				dialog.dismiss();
			}
		});
		return dialog;
	}
  	
  	private void setOverlayOrangtua(Lokasi lokasi){
  		if(lokasi != null){
  			overlayFactory.makeOverlayOrtu(lokasi);
  			if(overlayFactory.anyOrangTua()){
  				mapView.getOverlays().add(overlayFactory.getOrangTua());
  			}
  			// FIXME : test circle overlay...............................
  			OrangTuaCircleOverlay orangTuaOverlay = new OrangTuaCircleOverlay(lokasi, 100);
  			mapView.getOverlays().add(orangTuaOverlay);
  			//...........................................................
  		}
  	}
  	
  	private void changeOverlayOrangtua(Lokasi lokasi){
  		if(lokasi!=null){
  			List<Overlay> overlays= mapView.getOverlays();
  			if(overlays!=null){
  				for(Overlay overlay:overlays){
  					if(overlay instanceof PetaOverlay){
  						PetaOverlay petaOverlay = (PetaOverlay) overlay;
  	  					if(petaOverlay.size()>0){
	  	  					if(petaOverlay.getItem(0).getTitle().equals(MonitoringOverlayFactory.TITLE_ORANG_TUA)){
	  	  						overlays.remove(overlay);
	  	  						break;
	  	  					}	
  	  					}
  					}
  				}
  				setOverlayOrangtua(lokasi);
  				mapView.invalidate();
  			}
  		}
  	}
  	
  	private void setOverlayFactory(){
  		if(ambilLokasi){
	    	overlayFactory = new MonitoringOverlayFactory(this, new PetaHandlerPositionNClose(this));
	    }else{
	    	overlayFactory = new MonitoringOverlayFactory(this, null);
	    }
  	}
  	
  	private void setAmbilLokasi(){
  		if(ambilLokasi){
  			LinearLayout ll = (LinearLayout) findViewById(R.id.monitoringMapMenu);
  	  		ll.setVisibility(View.GONE);
  		}
  	}
  	
  	private void findLokasiOrangTua(){
  		gpsManager.searchLokasi();
  	}
  	
  	private void setPetaCenter(Lokasi lokasi){
  		if(mapController!=null&&lokasi!=null){
  			mapController.setCenter(new GeoPoint((int)(lokasi.getlatitude()*1E6), 
  					(int)(lokasi.getLongitude()*1E6)));
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
  	
  	// pengambilan lokasi orang tua.............................................
  	private static final class PetaHandlerPosition extends Handler{
  		public PetaHandlerPosition(Peta peta){
  			mPeta = peta;
  		}
  		@Override
		public void handleMessage(Message msg) {
			if(msg.what==Status.SUCCESS){
				Bundle bundle = msg.getData();
			
				Lokasi lokasi = new Lokasi();
				lokasi.setLatitude(bundle.getDouble("latitude"));
				lokasi.setLongitude(bundle.getDouble("longitude"));
				mPeta.setPetaCenter(lokasi);
				mPeta.changeOverlayOrangtua(lokasi);
			}
  			
			
		}
  		private Peta mPeta;
  	}
  	//..........................................................................
  	
	private MonitoringOverlayFactory 	overlayFactory;
	private boolean 					ambilLokasi; 
	private MapController 				mapController;
	private MapView 					mapView;
	private GpsManager					gpsManager;
	
	
}
