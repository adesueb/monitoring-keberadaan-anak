package org.ade.monak.ortu.map.view;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ade.monak.ortu.R;
import org.ade.monak.ortu.entity.Anak;
import org.ade.monak.ortu.entity.DataMonitoring;
import org.ade.monak.ortu.entity.Lokasi;
import org.ade.monak.ortu.entity.Pelanggaran;

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
	
	private MonitoringOverlay createPetaOverlay(String id, int status){
		if(mHandler != null){
			return new PetaAmbilLokasiOverlay(mContext.getResources().getDrawable(status), mContext, mHandler);
			
		}else{
			return new PetaOverlay(id,mContext.getResources().getDrawable(status), mContext);
			
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
	
	public boolean anyLog(){
		return monitoringOverlays.get(LOG)!=null;
	}
	
	public void makeOverlayAnak(Anak anak){
		MonitoringOverlay petaOverlay	= createPetaOverlay(ID_ANAK, ANAK);
		Lokasi lokasi = anak.getLastLokasi();
		if(lokasi!=null){
			GeoPoint point = new GeoPoint((int)(lokasi.getlatitude()*1E6),(int) (lokasi.getLongitude()*1E6));
			
			String pesan = "-";
			if(lokasi.getTime()!=0){
				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(lokasi.getTime());
				pesan = "anak berada disini jam "+
						cal.get(Calendar.HOUR_OF_DAY)+":"+cal.get(Calendar.MINUTE)+":"+
						cal.get(Calendar.SECOND);
					
			}	
			
			OverlayItem overlayItem = 
					new OverlayItem
						(point, anak.getNamaAnak(),pesan);	
			petaOverlay.addOverLay(overlayItem);

			monitoringOverlays.put(ANAK, petaOverlay);
		}
		
		
	}
	
	public void makeOverlayAnaks(List<Anak> anaks, List<Lokasi> lokasis){
		MonitoringOverlay petaOverlay	= createPetaOverlay(ID_ANAK, ANAK);
		
		if(anaks!=null){
			for(int i = 0 ;i<anaks.size();i++){
				Lokasi 	lokasi 	= lokasis.get(i);
				Anak	anak	= anaks.get(i);
				if(lokasi!=null){
					GeoPoint point = new GeoPoint((int)(lokasi.getlatitude()*1E6),(int) (lokasi.getLongitude()*1E6));
					
					String pesan = "-";
					if(lokasi.getTime()!=0){
						Calendar cal = Calendar.getInstance();
						cal.setTimeInMillis(lokasi.getTime());
						pesan = "anak berada disini jam "+
								cal.get(Calendar.HOUR_OF_DAY)+":"+cal.get(Calendar.MINUTE)+":"+
								cal.get(Calendar.SECOND);
							
					}		
					
					OverlayItem overlayItem = 
							new OverlayItem
								(point, anak.getNamaAnak(),pesan);

					petaOverlay.addOverLay(overlayItem);
				}
			
				
			}

			monitoringOverlays.put(ANAK, petaOverlay);
		}
	}
	
	public void makeOverlayLogAnak(Anak anak, List<Lokasi> lokasis){
		MonitoringOverlay petaOverlay = createPetaOverlay(ID_LOG,LOG);
		for(int i=0;i<lokasis.size();i++){
			Lokasi lokasi = lokasis.get(i);
			GeoPoint point = new GeoPoint((int)(lokasi.getlatitude()*1E6),(int) (lokasi.getLongitude()*1E6));
			
			String pesan = "-";
			if(lokasi.getTime()!=0){
				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(lokasi.getTime());
				pesan = "anak berada disini jam "+
						cal.get(Calendar.HOUR_OF_DAY)+":"+cal.get(Calendar.MINUTE)+":"+
						cal.get(Calendar.SECOND);
					
			}		
					
			OverlayItem overlayItem = 
					new OverlayItem
						(point, anak.getNamaAnak(),pesan);

			petaOverlay.addOverLay(overlayItem);
		}

		monitoringOverlays.put(LOG, petaOverlay);
	}
	
	public void makeOverlayNewPelanggaran(Pelanggaran pelanggaran){
		MonitoringOverlay monitoringOverlay = createPetaOverlay(ID_PELANGGARAN, PELANGGARAN);		
		OverlayItem overlayItem = makeOverlayItemSingglePelanggaran(pelanggaran);
		monitoringOverlay.addOverLay(overlayItem);
		monitoringOverlays.put(PELANGGARAN, monitoringOverlay);
	}
	
	public void makeOverlayPelanggarans(List<Pelanggaran> pelanggarans){
		MonitoringOverlay monitoringOverlay = createPetaOverlay(ID_PELANGGARAN, PELANGGARAN);
		if(pelanggarans!=null){
			for(Pelanggaran pelanggaran:pelanggarans){
				OverlayItem overlayItem = makeOverlayItemSingglePelanggaran(pelanggaran);
				monitoringOverlay.addOverLay(overlayItem);
			}
			
			monitoringOverlays.put(PELANGGARAN, monitoringOverlay);
		}
		
	}
	
	public void makeOverlayOrtu(Lokasi lokasi){
		
		MonitoringOverlay petaOverlay	= createPetaOverlay(ID_ORANGTUA, ORANG_TUA);
		
		GeoPoint point = new GeoPoint((int)(lokasi.getlatitude()*1E6),(int) (lokasi.getLongitude()*1E6));
		
		OverlayItem overlayItem = 
				new OverlayItem
					(point, TITLE_ORANG_TUA, "anda berada di daerah ini");
		
		petaOverlay.addOverLay(overlayItem);
		
		monitoringOverlays.put(ORANG_TUA, petaOverlay);
	}
	
	public void makeOverlayDataMonitoring(List<DataMonitoring> dataMonitorings){
		
		MonitoringOverlay petaOverlaySeharusnya 	= createPetaOverlay(ID_SEHARUSNYA,SEHARUSNYA);
		
		MonitoringOverlay petaOverlayTerlarang 	= createPetaOverlay(ID_TERLARANG,TERLARANG);
		if(dataMonitorings!=null){
			for(DataMonitoring dataMonitoring: dataMonitorings){
				OverlayItem overlayItem = makeOverlayItemSinggleDataMonitoring(dataMonitoring);
				if(dataMonitoring.isSeharusnya()){
					petaOverlaySeharusnya.addOverLay(overlayItem);
				}else{
					petaOverlayTerlarang.addOverLay(overlayItem);
				}
				
			}
			if(petaOverlaySeharusnya.size()>0){
				monitoringOverlays.put(SEHARUSNYA, petaOverlaySeharusnya);		
			}
			
			if(petaOverlayTerlarang.size()>0){
				monitoringOverlays.put(TERLARANG, petaOverlayTerlarang);			
			}
		}
		
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
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(lokasi.getTime());
		String pesan = "pelanggaran terjadi jam "+
				cal.get(Calendar.HOUR_OF_DAY)+":"+cal.get(Calendar.MINUTE)+":"+cal.get(Calendar.SECOND)+
				"\npelanggaran adalah "+pelanggaran.getDataMonitoring().getKeterangan();
		String headPesan = "anak " +pelanggaran.getAnak().getNamaAnak();
		if(pelanggaran.getDataMonitoring().getStatus()==DataMonitoring.SEHARUSNYA){
			headPesan +=" melanggar pelanggaran seharusnya";
		}else{
			headPesan +=" melanggar pelanggaran terlarang";
		}
		OverlayItem overlayItem = 
				new OverlayItem
					(point, headPesan, pesan);
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
	
	public MonitoringOverlay getLog(){
		return monitoringOverlays.get(LOG);
	}
	
	public Map<Integer,MonitoringOverlay> getMonitoringOverlays(){
		return this.monitoringOverlays;
	}
	
	
	private Map<Integer,MonitoringOverlay> monitoringOverlays = 
			new HashMap<Integer, MonitoringOverlay>();
	
	private final Context mContext;
	private final Handler mHandler;
	

	public final static String ID_ORANGTUA		= "orang_tua";
	public final static String ID_ANAK			= "anak";
	public final static String ID_PELANGGARAN 	= "pelanggaran";
	public final static String ID_SEHARUSNYA	= "seharusnya";
	public final static String ID_TERLARANG		= "terlarang";
	public final static String ID_LOG			= "log";
	
	public final static int ANAK		= R.drawable.anakkecil;
	public final static int SEHARUSNYA 	= R.drawable.seharusnya;
	public final static int TERLARANG	= R.drawable.terlarang;
	public final static int PELANGGARAN	= R.drawable.pelanggaran;
	public final static int ORANG_TUA	= R.drawable.parent;
	public final static int LOG			= R.drawable.log;
	public final static String TITLE_ORANG_TUA = "posisi anda";

}
