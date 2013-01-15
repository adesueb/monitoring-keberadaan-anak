package org.ade.monitoring.keberadaan.map.view;

import java.util.ArrayList;
import java.util.List;

import org.ade.monitoring.keberadaan.Variable.VariableEntity;
import org.ade.monitoring.keberadaan.entity.Anak;
import org.ade.monitoring.keberadaan.entity.DataMonitoring;
import org.ade.monitoring.keberadaan.entity.Lokasi;
import org.ade.monitoring.keberadaan.entity.Pelanggaran;
import org.ade.monitoring.keberadaan.map.service.GpsManager;
import org.ade.monitoring.keberadaan.service.storage.DatabaseManager;
import org.ade.monitoring.keberadaan.service.storage.PreferenceMonitoringManager;

import android.content.Context;
import android.util.Log;

import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class OverlayControllerMonak {
	
	public OverlayControllerMonak(Context context, MonitoringOverlayFactory overlayFactory, MapView mapView, GpsManager gpsManager){
		this.context = context;
		this.gpsManager = gpsManager;
		databaseManager = new DatabaseManager(context);
		this.overlayFactory = overlayFactory;
		this.mapView	= mapView;
	}
	
	public void refreshOverlay(List<Integer> pilihanOverlay){
  		// TODO : remove all overlays.......
  		for(int i:pilihanOverlay){
  			switch(i){
  				case VariableEntity.ORANG_TUA:{
  					setFirstOverlayOrangTua();
  					break;
  				}case VariableEntity.DATA_MONITORING:{
  					setOverlayDataMonitoring();
  					break;
  				}case VariableEntity.ANAK:{
  					setOverlayAnak();
  					break;
  				}case VariableEntity.PELANGGARAN:{
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
  	
  	/*
  	 * TODO : get log location n shows these....
  	 */
  	public void setOverlayLogLocationAnak(List<Lokasi> lokasis){
  		
  	}
  	
  	
	public void setOverlayAnak(){
  		List<Anak> anaks = databaseManager.getAllAnak(false, false);
  		List<Lokasi> lokasis = new ArrayList<Lokasi>();
  		for(Anak anak:anaks){
  			lokasis.add(anak.getLokasi());
  			Log.d("Peta", "lokasi dari anak adalah : "+anak.getLokasi().getlatitude()+","+anak.getLokasi().getLongitude());
  		}
  		
  		overlayFactory.makeOverlayAnak(anaks, lokasis);
  		for(Lokasi lokasi:lokasis){
  			RadiusOverlay anakOverlay = new RadiusOverlay(ID_ANAK, lokasi, 100, COLOR_ANAK);
  			mapView.getOverlays().add(anakOverlay);
  		}
  	}
  	
  	public void setOverlayPelanggaran(){
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
  			
  			PreferenceMonitoringManager prefrenceManager = new PreferenceMonitoringManager(context);
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
  			RadiusOverlay orangTuaOverlay = new RadiusOverlay(ID_ORANGTUA,lokasi, 100, COLOR_ORTU);
  			mapView.getOverlays().add(orangTuaOverlay);
  		}
  	}
  	
  	public void removeOverlayAnaks(){
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
  	
  	public void changeLocationOverlayOrangtua(Lokasi lokasi){
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
  	
  	private final GpsManager gpsManager;
  	private final Context context;
  	private final DatabaseManager databaseManager;
  	private final MonitoringOverlayFactory overlayFactory;
  	private final MapView mapView;
  	

	private final static String ID_ORANGTUA		= "orang_tua";
	private final static String ID_ANAK			= "anak";
	private final static String ID_PELANGGARAN 	= "pelanggaran";
	private final static String ID_SEHARUSNYA	= "seharusnya";
	private final static String ID_TERLARANG	= "terlarang";

	private final static int COLOR_PELANGGARAN 	= 0x99AA0000;
	private final static int COLOR_ANAK			= 0x9900AA00;
	private final static int COLOR_SEHARUSNYA	= 0x999966FF;
	private final static int COLOR_TERLARANG	= 0x99FFFF00;
	private final static int COLOR_ORTU			= 0x990000AA;
  	
}
