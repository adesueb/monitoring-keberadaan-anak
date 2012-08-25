package org.ade.monitoring.keberadaan.service;

import java.util.Calendar;
import java.util.List;

import org.ade.monitoring.keberadaan.entity.DataMonitoring;
import org.ade.monitoring.keberadaan.entity.Lokasi;
import org.ade.monitoring.keberadaan.lokasi.Tracker;
import org.ade.monitoring.keberadaan.storage.DatabaseManager;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class BackgroundService extends Service{


    public BackgroundService () {
    	
    }
//  
//    public void aktifkanReceiveSMS(){
//    	
//    }
//
//    public void aktifkanMonitoring(){
//    	
//    }
    
	@Override
	public void onCreate() {
		mTracker = new Tracker(this, null);
		dataMonitorings = new DatabaseManager(this).getAllDataMonitorings(false);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if(mTracker !=null){
			if(dataMonitorings != null){
				for(DataMonitoring dataMonitoring:dataMonitorings){
					Calendar cal = Calendar.getInstance();
					long now = cal.getTimeInMillis();					
					cal = null;

					long mulai 		= dataMonitoring.getWaktuMulaiLong();
					long selesai 	= dataMonitoring.getWaktuSelesaiLong();
					
					if(now>mulai && now<selesai){
						Lokasi lokasiMonitoring = dataMonitoring.getLokasi();
						Lokasi lokasiHp 		= mTracker.getLokasi();
						//TODO : cocokkan lokasi
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

}
