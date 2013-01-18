package org.ade.monitoring.keberadaan.service;

import java.util.Calendar;
import java.util.Date;
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
import org.ade.monitoring.keberadaan.service.storage.PreferenceMonitoringManager;
import org.ade.monitoring.keberadaan.util.EntityBundleMaker;
import org.ade.monitoring.keberadaan.util.IDGenerator;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

public class HandlerMainReceiveMonitoringLocation extends Handler{

	public HandlerMainReceiveMonitoringLocation(Context context){
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
					
					Date	now 	= cal.getTime();	
					int		hari 	= cal.get(Calendar.DAY_OF_WEEK);
					int 	tanggal	= cal.get(Calendar.DAY_OF_MONTH);
				
					Date mulai 		= dataMonitoring.getWaktuMulaiInDate();
					Date selesai 	= dataMonitoring.getWaktuSelesaiInDate();
				
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
						if((now.getHours()>mulai.getHours() && now.getHours()<selesai.getHours())
								||now==null){
							if((now.getMinutes()>mulai.getMinutes() && now.getMinutes()<selesai.getMinutes())
									||now==null){
								Lokasi 	lokasiMonitoring 	= dataMonitoring.getLokasi();
								
							
								
								int		tolerancy			= dataMonitoring.getTolerancy();
								locationMonitorUtil.setCurrentLocation(lokasiHp);
								locationMonitorUtil.setMonitorLocation(lokasiMonitoring);
								if(tolerancy!=0){
									locationMonitorUtil.setTolerancy(dataMonitoring.getTolerancy());
								}
								if(locationMonitorUtil.isInTolerancy()){
									if(dataMonitoring.isTerlarang()){
										//TODO : mengirim peringatan
										Peringatan peringatan = new Peringatan();
										peringatan.setIdMonitoring(dataMonitoring.getIdMonitoring());
										peringatan.setLokasiAnak(lokasiHp);
										peringatan.setTipe(TipePesanMonak.PERINGATAN_TERLARANG);
										peringatan.setIdOrtu(new IDGenerator(context, null).getIdOrangTua());
										senderMonitoring.sendPeringatanTerlarang(peringatan);
									}						
								}else{
									if(dataMonitoring.isSeharusnya()){
										//TODO : mengirim peringatan
										Peringatan peringatan = new Peringatan();
										peringatan.setIdMonitoring(dataMonitoring.getIdMonitoring());
										peringatan.setLokasiAnak(lokasiHp);
										peringatan.setTipe(TipePesanMonak.PERINGATAN_SEHARUSNYA);
										peringatan.setIdOrtu(new IDGenerator(context, null).getIdOrangTua());
										senderMonitoring.sendPeringatanSeharusnya(peringatan);
									}
								}
							}
							
						}
					}
					

				}
			}
			
		}
		
	}
	private final Context context;
	
	private SenderPesanData			senderMonitoring	= null;
	private List<DataMonitoring> 	dataMonitorings 	= null;
	private LocationMonitorUtil 	locationMonitorUtil = null;
	private PreferenceMonitoringManager pref;
	private SenderLokasi			senderLokasi		= null;
}
