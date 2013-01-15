package org.ade.monitoring.keberadaan.map.view;

import java.util.ArrayList;
import java.util.List;

import org.ade.monitoring.keberadaan.R;
import org.ade.monitoring.keberadaan.Variable.VariableEntity;
import org.ade.monitoring.keberadaan.Variable.Status;
import org.ade.monitoring.keberadaan.boundary.DaftarAnak;
import org.ade.monitoring.keberadaan.boundary.DaftarMonitoring;
import org.ade.monitoring.keberadaan.entity.Anak;
import org.ade.monitoring.keberadaan.entity.Lokasi;
import org.ade.monitoring.keberadaan.map.service.GpsManager;
import org.ade.monitoring.keberadaan.service.MonakService;
import org.ade.monitoring.keberadaan.service.BinderHandlerMonak;
import org.ade.monitoring.keberadaan.service.gate.monak.SenderRequestLokasiAnak;
import org.ade.monitoring.keberadaan.service.storage.DatabaseManager;
import org.ade.monitoring.keberadaan.service.storage.PreferenceMonitoringManager;
import org.ade.monitoring.keberadaan.service.util.IBindMonakServiceConnection;
import org.ade.monitoring.keberadaan.service.util.ServiceMonakConnection;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
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

public class Peta extends MapActivity implements IBindMonakServiceConnection{

  	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		isAmbilLokasi = intent.getBooleanExtra(EXTRA_AMBIL_LOKASI, false);
		isPelanggaran = intent.getBooleanExtra(EXTRA_PELANGGARAN, false);
		
		setContentView(R.layout.monitoring_map);
		
		setToolbarMenu();
		
		databaseManager = new DatabaseManager(this);
		
		gpsManager 		= new GpsManager(this, new PetaHandlerLocationOrangTua(this));
		
		petaDialog		= new PetaDialog(this);
		
		MapView mapView	= (MapView) findViewById(R.id.mapview);
		mapController 	= mapView.getController();
		Lokasi lokasi 	= gpsManager.getLastLokasi();
		
		PreferenceMonitoringManager prefrenceManager = new PreferenceMonitoringManager(this);
		if(lokasi !=null){
			prefrenceManager.setMapLokasi(lokasi);
		}else{
			lokasi = prefrenceManager.getMapLokasi();
		}		
		
		serviceConnection = new ServiceMonakConnection(this);
		bindService(new Intent(this, MonakService.class), 
				serviceConnection, 
				Context.BIND_AUTO_CREATE);
		
		setPetaCenter(lokasi);
		
		MonitoringOverlayFactory overlayFactory;
		if(isAmbilLokasi){
			overlayFactory = new MonitoringOverlayFactory(this, new PetaHandlerPositionNClose(this));
	    }else{
	    	overlayFactory = new MonitoringOverlayFactory(this, null);
	    }
		overlayControllerMonak = new OverlayControllerMonak(this, overlayFactory, mapView, gpsManager);
		
		if(isPelanggaran){
			overlayControllerMonak.setOverlayPelanggaran();
		}
		
		
		setMenuAmbilLokasi();
		
		findLokasiOrangTua();
		
		requestAllAnakLocations();
		
		
		mapController.setZoom(12);
	    mapView.setBuiltInZoomControls(true);
	}
  	
  	private void setToolbarMenu(){
  		ImageView ivSearch 	= (ImageView) findViewById(R.id.monitoringMapSearch);		
		ivSearch.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				showDialog(DIALOG_SEARCH);
			}
		});
		
		
		ImageView ivLog 	= (ImageView) findViewById(R.id.monitoringMapLog);		
		ivLog.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				showDialog(DIALOG_LOG);
			}
		});
		
		ImageView ivTracking 	= (ImageView) findViewById(R.id.monitoringMapTracking);		
		ivTracking.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				showDialog(DIALOG_TRACK);
			}
		});
		
  	}
  	
  	@Override
	protected boolean isRouteDisplayed() {
		return false;
	} 
  	
  	@Override
	protected Dialog onCreateDialog(int id) {
  		super.onCreateDialog(id);
  		switch(id){
  			case DIALOG_SEARCH : {
  				return petaDialog.getDialogSearch();
  			}case DIALOG_LOG : {
  				return petaDialog.getDialogLog();
  			}case DIALOG_TRACK : {
  				return petaDialog.getDialogTrack();
  			}
  		}
  		return  null;
	}
  	
  	public void actionOkSearchDialog(List<Integer> pilihanOverlay){
  		overlayControllerMonak.refreshOverlay(pilihanOverlay);
  	}
  	
  	public void actionOkLogDialog(List<Integer> pilihanOverlay, List<Anak> anaks){
  		
  	}
  	
  	public void actionOkTrackDialog(List<Integer> pilihanOverlay, List<Anak> anaks){
  		
  	}
  	
  	public void receiveLogFromAnak(String idAnak, List<Lokasi> lokasis){
  		
  	}
  	  	
  	public BinderHandlerMonak getBinderHandler(){
  		return handlerBinder;
  	}
  	
  	private void updateOverlaySingleAnak(){
		overlayControllerMonak.removeOverlayAnaks();
  		overlayControllerMonak.setOverlayAnak();
  	}
  	
  	private void requestAllAnakLocations(){
  		List<Anak> anaks = databaseManager.getAllAnak(false, false);
  		// FIXME : bind sender waiting harus satu2.... disini lgsg di looping.... seharusnya g boleh....
  		for(Anak anak:anaks){
  			SenderRequestLokasiAnak sender = 
  					new SenderRequestLokasiAnak(this, new SendingLocationHandler(this, anak), anak);
  			sender.send();
  			if(handlerBinder!=null){
  				handlerBinder.bindUIHandlerWaitingLocation(new WaitingLocationAnakHandler(this));
  			}
  		}
  		//.......................................................................
  	}
  
  	
  	private void setMenuAmbilLokasi(){
  		if(isAmbilLokasi){
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
  	
  	
  	
  	@Override
	protected void onStop() {
		super.onStop();
		if(bound){
			handlerBinder.unBindUIHandlerWaitingLocation();
			handlerBinder.unBindUIHandlerWaitingLogLocation();
			unbindService(serviceConnection);
		}
		bound = false;
	}

  	public void setBinderHandlerMonak(BinderHandlerMonak binderHandlerMonak) {
		handlerBinder = binderHandlerMonak;
		
	}

	public void setBound(boolean bound) {
		this.bound = bound;
	}
  	
	private PetaDialog					petaDialog;
	private boolean						bound;
	
  	private boolean 					isAmbilLokasi; 
	private boolean						isPelanggaran;
	private MapController 				mapController;
	private GpsManager					gpsManager;
	private DatabaseManager				databaseManager;
		
	private BinderHandlerMonak			handlerBinder;
	
	private OverlayControllerMonak		overlayControllerMonak;
	
	private ServiceMonakConnection		serviceConnection;
	
	
	public final static String EXTRA_AMBIL_LOKASI	= "ambilLokasi";
	public final static String EXTRA_PELANGGARAN	= "pelanggaran";
	
	private final static int DIALOG_SEARCH		= 0;
	private final static int DIALOG_LOG			= 1;
	private final static int DIALOG_TRACK		= 2;
	
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
  	private static final class PetaHandlerLocationOrangTua extends Handler{
  		public PetaHandlerLocationOrangTua(Peta peta){
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
				mPeta.overlayControllerMonak.changeLocationOverlayOrangtua(lokasi);
			}
  			
			
		}
  		private Peta mPeta;
  	}
  	//..........................................................................
  	
  	private final static class SendingLocationHandler extends Handler{
		public SendingLocationHandler(Peta peta, Anak anak){
			this.peta = peta;
			this.anak = anak;
		}
		
		@Override
		public void handleMessage(Message msg) {
			if(msg.what==Status.SUCCESS){
				//TODO : what're u gonna do?
			}else if(msg.what==Status.FAILED){
				//TODO : 
			}
		}

		private final Peta peta;
		private final Anak anak;
	}
  	
  	private final static class WaitingLocationAnakHandler extends Handler{

  		public WaitingLocationAnakHandler(Peta peta){
  			this.peta = peta;
  		}
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch(msg.what){
				case Status.SUCCESS:{
					peta.updateOverlaySingleAnak();
					break;
				}case Status.FAILED:{
					break;
				}
			}
		}
		
		private final Peta peta;
  		 
  	}

}
