package org.ade.monitoring.keberadaan.service;

import java.util.Calendar;
import java.util.List;

import org.ade.monitoring.keberadaan.Variable.TipePesanMonak;
import org.ade.monitoring.keberadaan.entity.Anak;
import org.ade.monitoring.keberadaan.entity.DataMonitoring;
import org.ade.monitoring.keberadaan.entity.DateMonitoring;
import org.ade.monitoring.keberadaan.entity.DayMonitoring;
import org.ade.monitoring.keberadaan.entity.Lokasi;
import org.ade.monitoring.keberadaan.entity.Peringatan;
import org.ade.monitoring.keberadaan.map.service.LocationMonitorUtil;
import org.ade.monitoring.keberadaan.service.gate.monak.SenderLokasi;
import org.ade.monitoring.keberadaan.service.gate.monak.SenderPesanData;
import org.ade.monitoring.keberadaan.service.storage.DatabaseManager;
import org.ade.monitoring.keberadaan.service.storage.LogMonakFileManager;
import org.ade.monitoring.keberadaan.service.storage.PreferenceMonitoringManager;
import org.ade.monitoring.keberadaan.util.EntityBundleMaker;
import org.ade.monitoring.keberadaan.util.IDGenerator;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

public class MainMonitoring extends Handler{

	public MainMonitoring(Context context){
		this.context 	= context;
		dataMonitorings 	= new DatabaseManager(context).getAllDataMonitorings(false,true);
		locationMonitorUtil = new LocationMonitorUtil();
		senderMonitoring	= new SenderPesanData(context, null);
		pref				= new PreferenceMonitoringManager(context);
		senderLokasi		= new SenderLokasi(context);
	}
	
	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
		Lokasi 	lokasiHp = EntityBundleMaker.getLokasiFromBundle(msg.getData());
		if(lokasiHp!=null){
			
			if(pref.isAktifTrackingMode()){
				Anak anak = new Anak();
				anak.setIdAnak(pref.getIdAnak());
				anak.setLastLokasi(lokasiHp);
				senderLokasi.sendLocationModeTracking(anak);
			}
			
			if(dataMonitorings != null && locationMonitorUtil != null){
				for(DataMonitoring dataMonitoring:dataMonitorings){
					Calendar cal = Calendar.getInstance();
					
					int		hari 	= cal.get(Calendar.DAY_OF_WEEK);
					int 	tanggal	= cal.get(Calendar.DAY_OF_MONTH);
					
					int hourNow = cal.get(Calendar.HOUR_OF_DAY);
					int minuteNow = cal.get(Calendar.MINUTE);
					
					cal.setTimeInMillis(dataMonitoring.getWaktuMulai());
					int hourMulai = cal.get(Calendar.HOUR_OF_DAY);
					int minuteMulai = cal.get(Calendar.MINUTE);
					
					cal.setTimeInMillis(dataMonitoring.getWaktuSelesai());
					int hourSelesai = cal.get(Calendar.HOUR_OF_DAY);
					int minuteSelesai = cal.get(Calendar.MINUTE);
							
					cal = null;
					
					boolean then = false;
					
					List<DateMonitoring> tanggalMonitorings = dataMonitoring.getTanggals();
					List<DayMonitoring> hariMonitorings = dataMonitoring.getHaris();
					if(tanggalMonitorings!=null){
						for(DateMonitoring tanggalMonitoring:tanggalMonitorings){
							if(tanggal == tanggalMonitoring.getDate()){
								then=true;
								break;
							}
						}
					}
					if(hariMonitorings!=null || !then){
						for(DayMonitoring hariMonitoring:hariMonitorings){
							if(hari == hariMonitoring.getHari()){
								then = true;
								break;
							}
						}
						
					}
					
					if(then){
						if((hourNow>hourMulai && hourNow<hourSelesai)){
							LogMonakFileManager.debug("sama jamnya");
							cekToleransi(dataMonitoring, lokasiHp);
							
						}else if(hourNow==hourMulai){
							if(minuteNow>=minuteMulai){
								cekToleransi(dataMonitoring, lokasiHp);
							}
						}else if(hourNow==hourSelesai){
							if( minuteNow<=minuteSelesai){
								cekToleransi(dataMonitoring, lokasiHp);
							}
						}
					}
				}
			}
		}
	}
	
	private void cekToleransi(DataMonitoring dataMonitoring, Lokasi lokasiHp){
		Lokasi 	lokasiMonitoring 	= dataMonitoring.getLokasi();
		
		int		tolerancy			= dataMonitoring.getTolerancy();
		locationMonitorUtil.setCurrentLocation(lokasiHp);
		locationMonitorUtil.setMonitorLocation(lokasiMonitoring);
		if(tolerancy!=0){
			locationMonitorUtil.setTolerancy(dataMonitoring.getTolerancy());
		}
		if(locationMonitorUtil.isInTolerancy()){
			if(dataMonitoring.isTerlarang()){
				sendPeringatanTerlarang(dataMonitoring, lokasiHp);
				pref.setTrackMeters(tolerancy);
			}						
		}else{
			if(dataMonitoring.isSeharusnya()){
				sendPeringatanSeharusnya(dataMonitoring, lokasiHp);
				pref.setTrackMeters(tolerancy);
			}
		}
	}
	
	private void sendPeringatanSeharusnya(DataMonitoring dataMonitoring, Lokasi lokasiHp){
		//TODO : mengirim peringatan

		Peringatan peringatan = new Peringatan();
		peringatan.setIdMonitoring(dataMonitoring.getIdMonitoring());
		peringatan.setLokasiAnak(lokasiHp);
		peringatan.setTipe(TipePesanMonak.PERINGATAN_SEHARUSNYA);
		peringatan.setIdOrtu(new IDGenerator(context, null).getIdOrangTua());
		senderMonitoring.sendPeringatanSeharusnya(peringatan);
		
	}
	
	private void sendPeringatanTerlarang(DataMonitoring dataMonitoring, Lokasi lokasiHp){
		//TODO : mengirim peringatan
		Peringatan peringatan = new Peringatan();
		peringatan.setIdMonitoring(dataMonitoring.getIdMonitoring());
		peringatan.setLokasiAnak(lokasiHp);
		peringatan.setTipe(TipePesanMonak.PERINGATAN_TERLARANG);
		peringatan.setIdOrtu(new IDGenerator(context, null).getIdOrangTua());
		senderMonitoring.sendPeringatanTerlarang(peringatan);
	}
	
	private void saveLogLocation(Lokasi lokasi){
		
	}
	
	private final Context context;
	
	private SenderPesanData			senderMonitoring	= null;
	private List<DataMonitoring> 	dataMonitorings 	= null;
	private LocationMonitorUtil 	locationMonitorUtil = null;
	private PreferenceMonitoringManager pref;
	private SenderLokasi			senderLokasi		= null;
}
