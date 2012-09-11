package org.ade.monitoring.keberadaan.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.ade.monitoring.keberadaan.entity.DataMonitoring;
import org.ade.monitoring.keberadaan.entity.DateMonitoring;
import org.ade.monitoring.keberadaan.entity.DayMonitoring;
import org.ade.monitoring.keberadaan.entity.Lokasi;
import org.ade.monitoring.keberadaan.lokasi.LocationMonitorUtil;
import org.ade.monitoring.keberadaan.lokasi.Tracker;
import org.ade.monitoring.keberadaan.storage.DatabaseManager;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class BackgroundService extends Service{


    public BackgroundService () {
    	
    }
    
	@Override
	public void onCreate() {
		mTracker = new Tracker(this, null);
		dataMonitorings = new DatabaseManager(this).getAllDataMonitorings(false,true);
		locationMonitorUtil = new LocationMonitorUtil();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if(mTracker !=null){
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
								Lokasi 	lokasiHp 			= mTracker.getLokasi();
								int		tolerancy			= dataMonitoring.getTolerancy();
								locationMonitorUtil.setCurrentLocation(lokasiHp);
								locationMonitorUtil.setMonitorLocation(lokasiMonitoring);
								if(tolerancy!=0){
									locationMonitorUtil.setTolerancy(dataMonitoring.getTolerancy());
								}
								if(locationMonitorUtil.isInTolerancy()){
									if(dataMonitoring.isTerlarang()){
										//TODO : mengirim peringatan
									}						
								}else{
									if(dataMonitoring.isSeharusnya()){
										//TODO : mengirim peringatan
									}
								}
							}
							
						}
					}
					

				}
			}
			
		}
		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	private Tracker mTracker = null;
	private List<DataMonitoring> dataMonitorings = null;
	private LocationMonitorUtil locationMonitorUtil = null;

}
