package org.ade.monitoring.keberadaan.map;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ade.monitoring.keberadaan.entity.Anak;
import org.ade.monitoring.keberadaan.entity.DataMonitoring;
import org.ade.monitoring.keberadaan.entity.Lokasi;
import org.ade.monitoring.keberadaan.entity.Pelanggaran;
import org.ade.monitoring.keberadaan.lokasi.GpsManager;
import org.ade.monitoring.keberadaan.service.Status;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

/*
 * belum ada pembuatan overlay seharusnya dan terlarang yang satu2...
 */
public class MonitoringOverlayFactory {
	
	public MonitoringOverlayFactory(Context context){
		mContext = context;
	}
	
	public boolean anyPosition(){
		return petaOverlays.get(POSITION)!=null;
	}
	
	public boolean anySeharusnya(){
		return petaOverlays.get(SEHARUSNYA)!=null;
	}
	
	public boolean anyTerlarang(){
		return petaOverlays.get(TERLARANG)!=null;
	}
	
	public boolean anyPelanggaran(){
		return petaOverlays.get(PELANGGARAN)!=null;
	}
	
	public void makeOverlayPosition(Anak anak){
		mGpsManager = new GpsManager(mContext, new PositionHandler(anak));
		mGpsManager.searchLokasi();
	}
	
	public void makeOverlayPelanggaran(List<Pelanggaran> pelanggarans){
		PetaOverlay petaOverlay	= 
				new PetaOverlay(mContext.getResources().getDrawable(PELANGGARAN), mContext);
		for(Pelanggaran pelanggaran:pelanggarans){
			OverlayItem overlayItem = makeOverlayItemSingglePelanggaran(pelanggaran);
			petaOverlay.addOverLay(overlayItem);
		}
		petaOverlays.put(PELANGGARAN, petaOverlay);
	}
	
	public void makeOverlayDataMonitoring(List<DataMonitoring> dataMonitorings){
		
		PetaOverlay petaOverlaySeharusnya 	= 
				new PetaOverlay(mContext.getResources().getDrawable(SEHARUSNYA), mContext);
		
		PetaOverlay petaOverlayTerlarang 	= 
				new PetaOverlay(mContext.getResources().getDrawable(TERLARANG), mContext);
		for(DataMonitoring dataMonitoring: dataMonitorings){
			OverlayItem overlayItem = makeOverlayItemSinggleDataMonitoring(dataMonitoring);
			if(dataMonitoring.isSeharusnya()){
				petaOverlaySeharusnya.addOverLay(overlayItem);
			}else{
				petaOverlayTerlarang.addOverLay(overlayItem);
			}
			
		}
		petaOverlays.put(SEHARUSNYA, petaOverlaySeharusnya);
		petaOverlays.put(TERLARANG, petaOverlayTerlarang);
	}
	
	private OverlayItem makeOverlayItemSinggleDataMonitoring(DataMonitoring dataMonitoring){
		Lokasi lokasi = dataMonitoring.getLokasi();
		GeoPoint point = new GeoPoint((int)(lokasi.getlatitude()*1E6),(int) (lokasi.getLongitude()*1E6));
		OverlayItem overlayItem = 
				new OverlayItem
					(point, dataMonitoring.getAnak().getNamaAnak(), dataMonitoring.getKeterangan());
		return overlayItem;
	}
	
	private OverlayItem makeOverlayItemSingglePelanggaran(Pelanggaran pelanggaran){
		Lokasi lokasi = pelanggaran.getLokasi();
		GeoPoint point = new GeoPoint((int)(lokasi.getlatitude()*1E6),(int) (lokasi.getLongitude()*1E6));
		OverlayItem overlayItem = 
				new OverlayItem
					(point, pelanggaran.getAnak().getNamaAnak(), pelanggaran.getDataMonitoring().getKeterangan());
		return overlayItem;
	}
	
	
	public PetaOverlay getPosition(){
		return petaOverlays.get(POSITION);
	}
	
	public PetaOverlay getSeharusnya(){
		return petaOverlays.get(SEHARUSNYA);
	}
	
	public PetaOverlay getTerlarang(){
		return petaOverlays.get(TERLARANG);
	}
	
	public PetaOverlay getPelanggaran(){
		return petaOverlays.get(PELANGGARAN);
	}
	
	private GpsManager mGpsManager;
	
	private class PositionHandler extends Handler{


		public PositionHandler (Anak anak){
			this.anak = anak;
		}
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
				case Status.SUCCESS :{
				
					double latitude 	= mGpsManager.getLokasi().getlatitude();
					double longitude 	= mGpsManager.getLokasi().getLongitude();
					
					PetaOverlay petaOverlay	= 
							new PetaOverlay(mContext.getResources().getDrawable(POSITION), mContext);
					GeoPoint point = new GeoPoint((int)(latitude*1E6),(int) (longitude*1E6));
					OverlayItem overlayItem = 
							new OverlayItem
								(point, anak.getNamaAnak(),"posisi "+anak.getNamaAnak());

					petaOverlay.addOverLay(overlayItem);
					
					petaOverlays.put(POSITION, petaOverlay);
				}
			}
		}
		
		private Anak anak;
		
	}
	
	private Map<Integer,PetaOverlay> petaOverlays = new HashMap<Integer, PetaOverlay>();
	
	private final Context mContext;
	
	public final static int POSITION	= 0;
	public final static int SEHARUSNYA = 1;
	public final static int TERLARANG	= 2;
	public final static int PELANGGARAN= 3;

}
