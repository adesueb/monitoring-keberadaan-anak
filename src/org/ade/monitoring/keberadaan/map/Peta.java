package org.ade.monitoring.keberadaan.map;

import java.util.ArrayList;
import java.util.List;

import org.ade.monitoring.keberadaan.R;
import org.ade.monitoring.keberadaan.Variable.Entity;
import org.ade.monitoring.keberadaan.Variable.Status;
import org.ade.monitoring.keberadaan.boundary.DaftarAnak;
import org.ade.monitoring.keberadaan.boundary.DaftarMonitoring;
import org.ade.monitoring.keberadaan.entity.Anak;
import org.ade.monitoring.keberadaan.entity.DataMonitoring;
import org.ade.monitoring.keberadaan.entity.Lokasi;
import org.ade.monitoring.keberadaan.entity.Pelanggaran;
import org.ade.monitoring.keberadaan.lokasi.GpsManager;
import org.ade.monitoring.keberadaan.service.BackgroundService;
import org.ade.monitoring.keberadaan.service.HandlerMonakBinder;
import org.ade.monitoring.keberadaan.service.koneksi.SenderRequestLokasiAnak;
import org.ade.monitoring.keberadaan.service.koneksi.SenderSMS;
import org.ade.monitoring.keberadaan.service.storage.DatabaseManager;
import org.ade.monitoring.keberadaan.service.storage.PreferenceManager;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
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
		
		setToolbarMenu();
		
		databaseManager = new DatabaseManager(this);
		
		gpsManager 		= new GpsManager(this, new PetaHandlerLocationOrangTua(this));
		
		mapView	 		= (MapView) findViewById(R.id.mapview);
		mapController 	= mapView.getController();
		Lokasi lokasi 	= gpsManager.getLastLokasi();
		
		PreferenceManager prefrenceManager = new PreferenceManager(this);
		if(lokasi !=null){
			prefrenceManager.setMapLokasi(lokasi);
		}else{
			lokasi = prefrenceManager.getMapLokasi();
		}
		
		serviceConnection = new ServiceConnectionPeta(this);
		bindService(new Intent(this, BackgroundService.class), 
				serviceConnection, 
				Context.BIND_AUTO_CREATE);
		
		setPetaCenter(lokasi);
		

	    setOverlayFactory();
	    		
		setAmbilLokasi();
		
		findLokasiOrangTua();
		
		
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
		
		ImageView ivAnak 	= (ImageView) findViewById(R.id.monitoringMapAnak);		
		ivAnak.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				Intent intent = new Intent(Peta.this, DaftarAnak.class);
				startActivity(intent);
				finish();
			}
		});
		
		ImageView ivMonitor 	= (ImageView) findViewById(R.id.monitoringMapMonitoring);		
		ivMonitor.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				Intent intent = new Intent(Peta.this, DaftarMonitoring.class);
				startActivity(intent);
				finish();
			}
		});
		
		ImageView ivLaporan 	= (ImageView) findViewById(R.id.monitoringMapLaporan);		
		ivLaporan.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				
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
				List<Integer> pilihanOverlay = new ArrayList<Integer>();
				SparseBooleanArray checked = listView.getCheckedItemPositions();
				for (int i = 0; i < len; i++){
					 if (checked.get(i)) {
						 pilihanOverlay.add(i);
					 }
				}
				refreshOverlay(pilihanOverlay);
					
				dialog.dismiss();
			}
		});
		return dialog;
	}
  	
  	private void refreshOverlay(List<Integer> pilihanOverlay){
  		for(int i:pilihanOverlay){
  			switch(i){
  				case Entity.ORANG_TUA:{
  					setFirstOverlayOrangTua();
  					break;
  				}case Entity.DATA_MONITORING:{
  					setOverlayDataMonitoring();
  					break;
  				}case Entity.ANAK:{
  					setOverlayAnak();
  					break;
  				}case Entity.PELANGGARAN:{
  					setOverlayPelanggaran();
  					break;
  				}
  			}
  		}
  	}
  	
  	private void setOverlayDataMonitoring(){
  		List<DataMonitoring> dataMonitorings = databaseManager.getAllDataMonitorings(false, true);
  		overlayFactory.makeOverlayDataMonitoring(dataMonitorings);
  		for(DataMonitoring dataMonitoring:dataMonitorings){
  			int color = 0;
  			if(dataMonitoring.getStatus()==DataMonitoring.SEHARUSNYA){
  				RadiusOverlay dataMonitoringOverlay = 
  						new RadiusOverlay(ID_SEHARUSNYA, dataMonitoring.getLokasi(), 100, COLOR_SEHARUSNYA);
  	  			mapView.getOverlays().add(dataMonitoringOverlay);
  			}else{
  				RadiusOverlay dataMonitoringOverlay = 
  						new RadiusOverlay(ID_TERLARANG, dataMonitoring.getLokasi(), 100, COLOR_TERLARANG);
  	  			mapView.getOverlays().add(dataMonitoringOverlay);
  			}
  		}
  	}
  	  	
  	private void updateOverlaySingleAnak(Anak anak){
  		if(anaks!=null){
  			for(Anak anakFor:anaks){
  				if(anakFor.getNoHpAnak().equals(anak.getNoHpAnak())){
  					anakFor.setLokasi(anak.getLokasi());
  				}
  			}
  			removeOverlayAnaks();
  			setOverlayAnak();
  		}
  	}
  	
  	private void RequestAllAnakLocations(List<Anak> anaks){
  		// FIXME : bind sender waiting harus satu2.... disini lgsg di looping.... seharusnya g boleh....
  		for(Anak anak:anaks){
  			SenderRequestLokasiAnak sender = 
  					new SenderRequestLokasiAnak(this, new SendingLocationHandler(this, anak), anak);
  			sender.send();
  			if(handlerBinder!=null){
  				handlerBinder.bindWaitingLocation(new WaitingLocationAnakHandler(this));
  			}
  		}
  		//.......................................................................
  	}
  	private void setOverlayAnak(){
  		anaks = databaseManager.getAllAnak(false, false);
  		List<Lokasi> lokasis = new ArrayList<Lokasi>();
  		for(Anak anak:anaks){
  			lokasis.add(anak.getLokasi());
  		}
  		
  		overlayFactory.makeOverlayAnak(anaks, lokasis);
  		for(Lokasi lokasi:lokasis){
  			RadiusOverlay anakOverlay = new RadiusOverlay(ID_ANAK, lokasi, 100, COLOR_ANAK);
  			mapView.getOverlays().add(anakOverlay);
  		}
  	}
  	
  	private void setOverlayPelanggaran(){
  		List<Pelanggaran> pelanggarans = databaseManager.getAllDataPelanggarans(false, false);
  		overlayFactory.makeOverlayPelanggaran(pelanggarans);
  		for(Pelanggaran pelanggaran : pelanggarans){
  			RadiusOverlay pelanggaranOverlay = new RadiusOverlay(ID_PELANGGARAN, pelanggaran.getLokasi(), 100, COLOR_PELANGGARAN);
  			mapView.getOverlays().add(pelanggaranOverlay);
  		}
  	}
  	
  	private void setFirstOverlayOrangTua(){
  		if(gpsManager!=null){
  			Lokasi lokasi 	= gpsManager.getLastLokasi();
  			
  			PreferenceManager prefrenceManager = new PreferenceManager(this);
  			if(lokasi !=null){
  				prefrenceManager.setMapLokasi(lokasi);
  			}else{
  				lokasi = prefrenceManager.getMapLokasi();
  			}
  			setOverlayOrangtua(lokasi);
  		}
  	}
  	
  	private void setOverlayOrangtua(Lokasi lokasi){
  		if(lokasi != null){
  			overlayFactory.makeOverlayOrtu(lokasi);
  			if(overlayFactory.anyOrangTua()){
  				mapView.getOverlays().add(overlayFactory.getOrangTua());
  			}
  			// FIXME : test circle overlay...............................
  			RadiusOverlay orangTuaOverlay = new RadiusOverlay(ID_ORANGTUA,lokasi, 100, COLOR_ORTU);
  			mapView.getOverlays().add(orangTuaOverlay);
  			//...........................................................
  		}
  	}
  	
  	private void removeOverlayAnaks(){
		List<Overlay> overlays= mapView.getOverlays();
		if(overlays!=null){
			for(int i=0;i<overlays.size();i++){
  				Overlay overlay = overlays.get(i);
  				if(overlay instanceof PetaOverlay){
  					PetaOverlay petaOverlay = (PetaOverlay) overlay;
  					if(petaOverlay.size()>0){
  						if(petaOverlay.getItem(0).getTitle().equals(MonitoringOverlayFactory.ANAK)){
  							overlays.remove(i);
  						}
  					}
  				}else if(overlay instanceof RadiusOverlay){
  					RadiusOverlay radius = (RadiusOverlay) overlay;
					String id = radius.getId();
					if(id.equals(COLOR_ANAK)){
						overlays.remove(i);
					}
  				}
  			}
			mapView.invalidate();
		}
  	}
  	
  	private void changeLocationOverlayOrangtua(Lokasi lokasi){
  		if(lokasi!=null){
  			List<Overlay> overlays= mapView.getOverlays();
  			if(overlays!=null){
  				for(int i=0;i<overlays.size();i++){
  					Overlay overlay = overlays.get(i);
  					if(overlay instanceof PetaOverlay){
  						PetaOverlay petaOverlay = (PetaOverlay) overlay;
  	  					if(petaOverlay.size()>0){
	  	  					if(petaOverlay.getItem(0).getTitle().equals(MonitoringOverlayFactory.TITLE_ORANG_TUA)){
	  	  						overlays.remove(i);
	  	  					}	
  	  					}
  					}else if(overlay instanceof RadiusOverlay){
  						RadiusOverlay radius = (RadiusOverlay) overlay;
  						String id = radius.getId();
  						if(id.equals(COLOR_ORTU)){
  							overlays.remove(i);
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
  	
  	
  	
  	@Override
	protected void onStop() {
		super.onStop();
		if(bound){
			handlerBinder.unbindWaitingLocation();
			unbindService(serviceConnection);
		}
		bound = false;
	}



	private boolean						bound;
  	
  	private MonitoringOverlayFactory 	overlayFactory;
	private boolean 					ambilLokasi; 
	private MapController 				mapController;
	private MapView 					mapView;
	private GpsManager					gpsManager;
	private DatabaseManager				databaseManager;
	private SenderSMS					senderSms;
	
	private List<Anak> 					anaks;
	
	private HandlerMonakBinder			handlerBinder;
	
	private ServiceConnectionPeta		serviceConnection;
	
	private final static String ID_ORANGTUA		= "orang_tua";
	private final static String ID_ANAK			= "anak";
	private final static String ID_PELANGGARAN 	= "pelanggaran";
	private final static String ID_SEHARUSNYA	= "seharusnya";
	private final static String ID_TERLARANG	= "terlarang";
	
	private final static int DIALOG_SEARCH		= 0;
	
	private final static int COLOR_PELANGGARAN 	= 0x99AA0000;
	private final static int COLOR_ANAK			= 0x9900AA00;
	private final static int COLOR_SEHARUSNYA	= 0x999966FF;
	private final static int COLOR_TERLARANG	= 0x99FFFF00;
	private final static int COLOR_ORTU			= 0x990000AA;
	

	private static final class ServiceConnectionPeta implements ServiceConnection{

		public ServiceConnectionPeta(Peta peta){
			this.peta = peta;
		}
		public void onServiceConnected(ComponentName name, IBinder service) {
			peta.handlerBinder = (HandlerMonakBinder) service;
			peta.bound = true;
		}

		public void onServiceDisconnected(ComponentName name) {
			peta.bound = false;
		}
		private final Peta peta;
		
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
				mPeta.changeLocationOverlayOrangtua(lokasi);
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
					Bundle bundle = msg.getData();
					Anak anak = new Anak();
					Lokasi lokasi = new Lokasi();
					lokasi.setLatitude(bundle.getDouble("latitude"));
					lokasi.setLongitude(bundle.getDouble("longitude"));
					anak.setLokasi(lokasi);
					anak.setNoHpAnak(bundle.getString("noHp"));
					peta.updateOverlaySingleAnak(anak);
					
					break;
				}case Status.FAILED:{
					
					break;
				}
			}
		}
		
		private final Peta peta;
  		 
  	}
  	
	
}
