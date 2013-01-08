package org.ade.monitoring.keberadaan.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.ade.monitoring.keberadaan.Variable.TipePesanData;
import org.ade.monitoring.keberadaan.entity.DataMonitoring;
import org.ade.monitoring.keberadaan.entity.DateMonitoring;
import org.ade.monitoring.keberadaan.entity.DayMonitoring;
import org.ade.monitoring.keberadaan.entity.Lokasi;
import org.ade.monitoring.keberadaan.entity.Peringatan;
import org.ade.monitoring.keberadaan.map.service.LocationMonitorUtil;
import org.ade.monitoring.keberadaan.map.service.Tracker;
import org.ade.monitoring.keberadaan.service.koneksi.SenderMonitoring;
import org.ade.monitoring.keberadaan.service.storage.DatabaseManager;
import org.ade.monitoring.keberadaan.util.EntityBundleMaker;
import org.ade.monitoring.keberadaan.util.IDGenerator;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class HandlerMainReceiveMonitoringLocation extends Handler{

	public HandlerMainReceiveMonitoringLocation(MonakService monakService){
		this.monakService = monakService;
		dataMonitorings 	= new DatabaseManager(monakService).getAllDataMonitorings(false,true);
		locationMonitorUtil = new LocationMonitorUtil();
		senderMonitoring	= new SenderMonitoring(monakService, null);
	}
	
	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
		Lokasi 	lokasiHp = EntityBundleMaker.getLokasiFromBundle(msg.getData());
		if(lokasiHp!=null){
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
										peringatan.setTipe(TipePesanData.PERINGATAN_TERLARANG);
										peringatan.setIdOrtu(new IDGenerator(monakService, null).getIdOrangTua());
										senderMonitoring.sendPeringatanSeharusnya(peringatan);
									}						
								}else{
									if(dataMonitoring.isSeharusnya()){
										//TODO : mengirim peringatan
										Peringatan peringatan = new Peringatan();
										peringatan.setIdMonitoring(dataMonitoring.getIdMonitoring());
										peringatan.setLokasiAnak(lokasiHp);
										peringatan.setTipe(TipePesanData.PERINGATAN_SEHARUSNYA);
										peringatan.setIdOrtu(new IDGenerator(monakService, null).getIdOrangTua());
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
	private final MonakService monakService;
	

	private InternetPushMonak		internetPush		= null;
	private SenderMonitoring		senderMonitoring	= null;
	private List<DataMonitoring> 	dataMonitorings 	= null;
	private LocationMonitorUtil 	locationMonitorUtil = null;
}
