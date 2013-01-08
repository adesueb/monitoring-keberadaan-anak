package org.ade.monitoring.keberadaan.map.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ade.monitoring.keberadaan.R;
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
	
	private MonitoringOverlay createPetaOverlay(int status){
		if(mHandler != null){
			return new PetaAmbilLokasiOverlay(mContext.getResources().getDrawable(status), mContext, mHandler);
			
		}else{
			return new PetaOverlay(mContext.getResources().getDrawable(status), mContext);
			
		}
	}
	
	public boolean anyAnak(){
		return monitoringOverlays.get(ANAK)!=null;
	}
	
	public boolean anySeharusnya(){
		return monitoringOverlays.get(SEHARUSNYA)!=null;
	}
	
	public boolean anyTerlarang(){
		return monitoringOverlays.get(TERLARANG)!=null;
	}
	
	public boolean anyPelanggaran(){
		return monitoringOverlays.get(PELANGGARAN)!=null;
	}
	
	public boolean anyOrangTua(){
		return monitoringOverlays.get(ORANG_TUA)!=null;
	}
	
	public void makeOverlayAnak(List<Anak> anaks, List<Lokasi> lokasis){
		MonitoringOverlay petaOverlay	= createPetaOverlay(ANAK);
		
		for(int i = 0 ;i<anaks.size();i++){
			Lokasi 	lokasi 	= lokasis.get(i);
			Anak	anak	= anaks.get(i);
			
			GeoPoint point = new GeoPoint((int)(lokasi.getlatitude()*1E6),(int) (lokasi.getLongitude()*1E6));
			OverlayItem overlayItem = 
					new OverlayItem
						(point, anak.getNamaAnak(),"posisi "+anak.getNamaAnak());

			petaOverlay.addOverLay(overlayItem);
			
		}

		monitoringOverlays.put(ANAK, petaOverlay);
	}
	
	public void makeOverlayPelanggaran(List<Pelanggaran> pelanggarans){
		MonitoringOverlay monitoringOverlay = createPetaOverlay(PELANGGARAN);
		
		for(Pelanggaran pelanggaran:pelanggarans){
			OverlayItem overlayItem = makeOverlayItemSingglePelanggaran(pelanggaran);
			monitoringOverlay.addOverLay(overlayItem);
		}
		
		monitoringOverlays.put(PELANGGARAN, monitoringOverlay);
	}
	
	public void makeOverlayOrtu(Lokasi lokasi){
		
		MonitoringOverlay petaOverlay	= createPetaOverlay(ORANG_TUA);
		
		GeoPoint point = new GeoPoint((int)(lokasi.getlatitude()*1E6),(int) (lokasi.getLongitude()*1E6));
		
		OverlayItem overlayItem = 
				new OverlayItem
					(point, TITLE_ORANG_TUA, "anda berada di daerah ini");
		
		petaOverlay.addOverLay(overlayItem);
		
		monitoringOverlays.put(ORANG_TUA, petaOverlay);
	}
	
	public void makeOverlayDataMonitoring(List<DataMonitoring> dataMonitorings){
		
		MonitoringOverlay petaOverlaySeharusnya 	= createPetaOverlay(SEHARUSNYA);
		
		MonitoringOverlay petaOverlayTerlarang 	= createPetaOverlay(TERLARANG);
		
		for(DataMonitoring dataMonitoring: dataMonitorings){
			OverlayItem overlayItem = makeOverlayItemSinggleDataMonitoring(dataMonitoring);
			if(dataMonitoring.isSeharusnya()){
				petaOverlaySeharusnya.addOverLay(overlayItem);
			}else{
				petaOverlayTerlarang.addOverLay(overlayItem);
			}
			
		}
		
		monitoringOverlays.put(SEHARUSNYA, petaOverlaySeharusnya);
		monitoringOverlays.put(TERLARANG, petaOverlayTerlarang);
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
	
	
	public MonitoringOverlay getAnak(){
		return monitoringOverlays.get(ANAK);
	}
	
	public MonitoringOverlay getSeharusnya(){
		return monitoringOverlays.get(SEHARUSNYA);
	}
	
	public MonitoringOverlay getTerlarang(){
		return monitoringOverlays.get(TERLARANG);
	}
	
	public MonitoringOverlay getPelanggaran(){
		return monitoringOverlays.get(PELANGGARAN);
	}
	
	public MonitoringOverlay getOrangTua(){
		return monitoringOverlays.get(ORANG_TUA);
	}
	
	
	private Map<Integer,MonitoringOverlay> monitoringOverlays = 
			new HashMap<Integer, MonitoringOverlay>();
	
	private final Context mContext;
	private final Handler mHandler;
	
	public final static int ANAK		= R.drawable.anak;
	public final static int SEHARUSNYA 	= R.drawable.seharusnya;
	public final static int TERLARANG	= R.drawable.terlarang;
	public final static int PELANGGARAN	= R.drawable.pelanggaran;
	public final static int ORANG_TUA	= R.drawable.parent;
	public final static String TITLE_ORANG_TUA = "posisi anda";

}
