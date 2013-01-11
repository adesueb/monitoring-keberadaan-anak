package org.ade.monitoring.keberadaan.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ade.monitoring.keberadaan.Variable.TipePesanMonak;
import org.ade.monitoring.keberadaan.entity.DataMonitoring;
import org.ade.monitoring.keberadaan.entity.DateMonitoring;
import org.ade.monitoring.keberadaan.entity.DayMonitoring;
import org.ade.monitoring.keberadaan.entity.Lokasi;
import org.ade.monitoring.keberadaan.entity.Peringatan;
import org.ade.monitoring.keberadaan.map.service.LocationMonitorUtil;
import org.ade.monitoring.keberadaan.map.service.Tracker;
import org.ade.monitoring.keberadaan.service.gate.ReceiverSMS;
import org.ade.monitoring.keberadaan.service.gate.SenderMonitoring;
import org.ade.monitoring.keberadaan.service.storage.DatabaseManager;
import org.ade.monitoring.keberadaan.service.storage.PreferenceMonitoringManager;
import org.ade.monitoring.keberadaan.util.IDGenerator;
import org.ade.monitoring.keberadaan.util.StorageHandler;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

public class MonakService extends Service{

    
	@Override
	public void onCreate() {
        Log.d("background service", "creating service");
		pref = new PreferenceMonitoringManager(this);
		pref.setActiveService();
		daftarSmsReceiver();

		handlerMonakBinder = new BinderHandlerMonak();
	}

	
	
	@Override
	public void onDestroy() {
		pref.setInActiveService();
		if(receiver!=null){
			unregisterReceiver(receiver);	
		}
		super.onDestroy();
	}
	

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("background service", "service started");
        
        if(!pref.isAktifTracker()){
    		Tracker tracker = new Tracker(this, new HandlerMainReceiveMonitoringLocation(this));
        	tracker.startTracking();
        }
        
		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		if(binderService==null){
			binderService = new BinderService(this);
		}
		return binderService;
	}
	
	public BinderHandlerMonak getBinderHandlerMonak(){
		if(handlerMonakBinder==null){
			handlerMonakBinder = new BinderHandlerMonak();
		}
		return handlerMonakBinder;
	}
	
	private void daftarSmsReceiver(){
		receiver = new ReceiverSMS(this);
		IntentFilter intentfilter= new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
		registerReceiver(receiver, intentfilter);
		intentfilter = null;
	}
	
	private PreferenceMonitoringManager 	pref;
	
	private BinderHandlerMonak handlerMonakBinder;

	private ReceiverSMS receiver;
	
	private BinderService binderService;
	
	public final static String MONAK_SERVICE			= "monak_service";
	public final static String WAITING_LOCATION 		= "waiting_location";
	public final static String STORAGE_WAITING_LOCATION = "storage_waiting_location";
	
	
	private class BinderService extends Binder{
		public BinderService(MonakService monakService){
			this.monakService = monakService;
		}
		
		public MonakService getMonakService(){
			if(monakService!=null){
				return monakService;				
			}
			return null;

		}
		private final MonakService monakService;
	}
	

}
