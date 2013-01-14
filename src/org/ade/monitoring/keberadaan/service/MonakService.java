package org.ade.monitoring.keberadaan.service;

import org.ade.monitoring.keberadaan.map.service.Tracker;
import org.ade.monitoring.keberadaan.service.gate.ReceiverSMS;
import org.ade.monitoring.keberadaan.service.storage.PreferenceMonitoringManager;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class MonakService extends Service{

    
	@Override
	public void onCreate() {
        Log.d("background service", "creating service");
		pref = new PreferenceMonitoringManager(this);
		pref.setActiveService();
//		daftarSmsReceiver();

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
	
//	private void daftarSmsReceiver(){
//		receiver = new ReceiverSMS(this);
//		IntentFilter intentfilter= new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
//		registerReceiver(receiver, intentfilter);
//		intentfilter = null;
//	}
	
	private PreferenceMonitoringManager 	pref;
	
	private BinderHandlerMonak handlerMonakBinder;

	private ReceiverSMS receiver;
	
	private BinderService binderService;
	
	public final static String MONAK_SERVICE			= "monak_service";
	public final static String WAITING_LOCATION 		= "waiting_location";
	public final static String WAITING_LOG_LOCATION		= "waiting_log_location";
	public final static String STORAGE_WAITING_LOCATION = "storage_waiting_location";
	
	
	public class BinderService extends Binder{
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
