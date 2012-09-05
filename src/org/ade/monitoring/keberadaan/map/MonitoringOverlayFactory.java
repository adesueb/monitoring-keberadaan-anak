package org.ade.monitoring.keberadaan.map;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ade.monitoring.keberadaan.entity.Anak;
import org.ade.monitoring.keberadaan.entity.DataMonitoring;
import org.ade.monitoring.keberadaan.entity.Lokasi;
import org.ade.monitoring.keberadaan.entity.Pelanggaran;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

import android.content.Context;
import android.os.Handler;

/*
 * belum ada pembuatan overlay seharusnya dan terlarang yang satu2...
 */
public class MonitoringOverlayFactory {
	
	public MonitoringOverlayFactory(Context context, Handler handler){
		mContext = context;
		mHandler = handler;
	}
	
	public boolean anyaAnak(){
		return petaOverlays.get(ANAK)!=null;
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
	
	public void makeOverlayAnak(List<Anak> anaks, List<Lokasi> lokasis){
		PetaOverlay petaOverlay	= 
				new PetaOverlay(mContext.getResources().getDrawable(ANAK), 
							mContext, mHandler);
		
		for(int i = 0 ;i<anaks.size();i++){
			Lokasi 	lokasi 	= lokasis.get(i);
			Anak	anak	= anaks.get(i);
			
			GeoPoint point = new GeoPoint((int)(lokasi.getlatitude()*1E6),(int) (lokasi.getLongitude()*1E6));
			OverlayItem overlayItem = 
					new OverlayItem
						(point, anak.getNamaAnak(),"posisi "+anak.getNamaAnak());

			petaOverlay.addOverLay(overlayItem);
			
		}

		petaOverlays.put(ANAK, petaOverlay);
	}
	
	public void makeOverlayPelanggaran(List<Pelanggaran> pelanggarans){
		PetaOverlay petaOverlay	= 
				new PetaOverlay(mContext.getResources().getDrawable(PELANGGARAN), mContext, mHandler);
		for(Pelanggaran pelanggaran:pelanggarans){
			OverlayItem overlayItem = makeOverlayItemSingglePelanggaran(pelanggaran);
			petaOverlay.addOverLay(overlayItem);
		}
		petaOverlays.put(PELANGGARAN, petaOverlay);
	}
	
	public void makeOverlayDataMonitoring(List<DataMonitoring> dataMonitorings){
		
		PetaOverlay petaOverlaySeharusnya 	= 
				new PetaOverlay(mContext.getResources().getDrawable(SEHARUSNYA), mContext, mHandler);
		
		PetaOverlay petaOverlayTerlarang 	= 
				new PetaOverlay(mContext.getResources().getDrawable(TERLARANG), mContext, mHandler);
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
	
	
	public PetaOverlay getAnak(){
		return petaOverlays.get(ANAK);
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
	
	
	private Map<Integer,PetaOverlay> petaOverlays = new HashMap<Integer, PetaOverlay>();
	
	private final Context mContext;
	private final Handler mHandler;
	
	public final static int ANAK		= 0;
	public final static int SEHARUSNYA 	= 1;
	public final static int TERLARANG	= 2;
	public final static int PELANGGARAN	= 3;

}
