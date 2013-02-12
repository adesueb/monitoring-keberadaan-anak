package org.ade.monak.ortu.map.view;

import java.util.ArrayList;
import java.util.List;

import org.ade.monak.ortu.Variable.VariableEntity;
import org.ade.monak.ortu.entity.Anak;
import org.ade.monak.ortu.entity.DataMonitoring;
import org.ade.monak.ortu.entity.Lokasi;
import org.ade.monak.ortu.entity.Pelanggaran;
import org.ade.monak.ortu.map.service.GpsManager;
import org.ade.monak.ortu.service.storage.DatabaseManagerOrtu;
import org.ade.monak.ortu.service.storage.LogMonakFileManager;
import org.ade.monak.ortu.service.storage.PreferenceMonitoringManager;
import org.ade.monak.ortu.util.BundleEntityMaker;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class OverlayControllerMonak {
	
	public OverlayControllerMonak(Context context, MonitoringOverlayFactory overlayFactory, MapView mapView, GpsManager gpsManager){
		this.context 		= context;
		this.gpsManager 	= gpsManager;
		databaseManager 	= new DatabaseManagerOrtu(context);
		this.overlayFactory = overlayFactory;
		this.mapView		= mapView;
	}
	
	public void refreshOverlay(List<Integer> pilihanOverlay){
  		// TODO : remove all overlays.......
  		for(int i:pilihanOverlay){
  			switch(i){
  				case VariableEntity.DATA_MONITORING:{
  					setOverlayDataMonitoring();
  					break;
  				}case VariableEntity.ANAK:{
  					setOverlayAnaks();
  					break;
  				}case VariableEntity.PELANGGARAN:{
  					setOverlayPelanggarans();
  					break;
  				}
  			}
  		}
  	}
	
  	private void setOverlayDataMonitoring(){
  		List<DataMonitoring> dataMonitorings = databaseManager.getAllDataMonitorings(true, true);
  		overlayFactory.makeOverlayDataMonitoring(dataMonitorings);
  		
  		if(overlayFactory.anySeharusnya()){
  			mapView.getOverlays().add(overlayFactory.getSeharusnya());
  		}
  		
  		if(overlayFactory.anyTerlarang()){
  			mapView.getOverlays().add(overlayFactory.getTerlarang());
  		}
  		
  		
  		for(DataMonitoring dataMonitoring:dataMonitorings){
  			
  			if(dataMonitoring.getStatus()==DataMonitoring.SEHARUSNYA){
  				RadiusOverlay dataMonitoringOverlay = 
  						new RadiusOverlay(ID_SEHARUSNYA, dataMonitoring.getLokasi(), dataMonitoring.getTolerancy(), COLOR_SEHARUSNYA);
  	  			mapView.getOverlays().add(dataMonitoringOverlay);
  			}else{
  				RadiusOverlay dataMonitoringOverlay = 
  						new RadiusOverlay(ID_TERLARANG, dataMonitoring.getLokasi(), dataMonitoring.getTolerancy(), COLOR_TERLARANG);
  	  			mapView.getOverlays().add(dataMonitoringOverlay);
  			}
  		}
  	}
  	
  	public void setOverlayLogLocationAnak(String idAnak, List<Lokasi> lokasis){
  		
  		Anak anak = databaseManager.getAnakById(idAnak, false, false);
  		overlayFactory.makeOverlayLogAnak(anak, lokasis);
  		
  		if(overlayFactory.anyLog()){
  			mapView.getOverlays().add(overlayFactory.getLog());
  		}
  		
  		int size = lokasis.size();
  		if(size>0){
  			anak.setLastLokasi(lokasis.get(size-1));		
  		}
  		setOverlayAnak(anak);
  		
  	}
  	
  	public void setOverlayAnak(Anak anak){
  		Lokasi lokasi = anak.getLastLokasi();
  		if(lokasi!=null){
  	  		overlayFactory.makeOverlayAnak(anak);
	  	  	if(overlayFactory.anyAnak()){
	  			mapView.getOverlays().add(overlayFactory.getAnak());	
	  		}
//  			RadiusOverlay anakOverlay = new RadiusOverlay(ID_ANAK, lokasi, 100, COLOR_ANAK);
//  			mapView.getOverlays().add(anakOverlay);  			
  		}

  	}
  	
	public void setOverlayAnaks(){
  		List<Anak> anaks = databaseManager.getAllAnak(false, false);
  		List<Lokasi> lokasis = new ArrayList<Lokasi>();
  		for(Anak anak:anaks){
  			lokasis.add(anak.getLastLokasi());
  		}
  		
  		
  		overlayFactory.makeOverlayAnaks(anaks, lokasis);
  		if(overlayFactory.anyAnak()){
  			mapView.getOverlays().add(overlayFactory.getAnak());	
  		}
  		
//  		for(Lokasi lokasi:lokasis){
//  			RadiusOverlay anakOverlay = new RadiusOverlay(ID_ANAK, lokasi, 100, COLOR_ANAK);
//  			mapView.getOverlays().add(anakOverlay);
//  		}
  	}
  	
	public void setOverlayNewPelanggaran(Handler handler){
		List<Pelanggaran> pelanggarans = databaseManager.getAllDataPelanggarans(true, true);
  		if(pelanggarans!=null){
  			int size = pelanggarans.size();
  			if(size>0){
  				
  				Pelanggaran pelanggaran = pelanggarans.get(size-1);
  				overlayFactory.makeOverlayNewPelanggaran(pelanggaran);
  				if(overlayFactory.anyPelanggaran()){
  		  			mapView.getOverlays().add(overlayFactory.getPelanggaran());
  		  		}
  				
  	  			RadiusOverlay pelanggaranOverlay = new RadiusOverlay(ID_PELANGGARAN, pelanggaran.getLokasi(), pelanggaran.getDataMonitoring().getTolerancy(), COLOR_PELANGGARAN);
  	  	  		mapView.getOverlays().add(pelanggaranOverlay);
  	  	  		Message message = new Message();
  	  	  		Bundle data = BundleEntityMaker.makeBundleFromLokasi(pelanggaran.getLokasi());
  	  	  		message.setData(data);
  	  	  		handler.sendMessage(message);
  	  		}
  		}
		
		
	}
	
  	public void setOverlayPelanggarans(){
  		List<Pelanggaran> pelanggarans = databaseManager.getAllDataPelanggarans(true, true);
  		
  		if(pelanggarans!=null){
  			overlayFactory.makeOverlayPelanggarans(pelanggarans);
  	  		
  	  		if(overlayFactory.anyPelanggaran()){
  	  			mapView.getOverlays().add(overlayFactory.getPelanggaran());
  	  		}
  	  		
  	  		for(Pelanggaran pelanggaran : pelanggarans){
  	  			RadiusOverlay pelanggaranOverlay = new RadiusOverlay(ID_PELANGGARAN, pelanggaran.getLokasi(), pelanggaran.getDataMonitoring().getTolerancy(), COLOR_PELANGGARAN);
  	  			mapView.getOverlays().add(pelanggaranOverlay);
  	  		}
  		}
  		
  		
  	}
  	
  	public void setFirstOverlayOrangTua(){
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
//  			RadiusOverlay orangTuaOverlay = new RadiusOverlay(ID_ORANGTUA,lokasi, 100, COLOR_ORTU);
//  			mapView.getOverlays().add(orangTuaOverlay);
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
  	private final DatabaseManagerOrtu databaseManager;
  	private final MonitoringOverlayFactory overlayFactory;
  	private final MapView mapView;
  	

	private final static String ID_ORANGTUA		= "orang_tua";
	private final static String ID_ANAK			= "anak";
	private final static String ID_PELANGGARAN 	= "pelanggaran";
	private final static String ID_SEHARUSNYA	= "seharusnya";
	private final static String ID_TERLARANG	= "terlarang";

	private final static int COLOR_PELANGGARAN 	= 0x55AA0000;
	private final static int COLOR_ANAK			= 0x5500AA00;
	private final static int COLOR_SEHARUSNYA	= 0x559966FF;
	private final static int COLOR_TERLARANG	= 0x55FFFF00;
	private final static int COLOR_ORTU			= 0x550000AA;
  	
}
