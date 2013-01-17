package org.ade.monitoring.keberadaan.service;

import org.ade.monitoring.keberadaan.Variable.TipePesanMonak;
import org.ade.monitoring.keberadaan.entity.IPesanData;
import org.ade.monitoring.keberadaan.map.service.Tracker;
import org.ade.monitoring.keberadaan.service.gate.monak.ReceiverLogMonak;
import org.ade.monitoring.keberadaan.service.gate.monak.ReceiverLokasi;
import org.ade.monitoring.keberadaan.service.gate.monak.ReceiverPendaftaranAnak;
import org.ade.monitoring.keberadaan.service.gate.monak.ReceiverPesanData;
import org.ade.monitoring.keberadaan.service.gate.monak.ReceiverRequestLogMonak;
import org.ade.monitoring.keberadaan.service.gate.monak.ReceiverRequestOnMonitoring;
import org.ade.monitoring.keberadaan.service.gate.monak.ReceiverTrackingMode;
import org.ade.monitoring.keberadaan.service.gate.monak.SenderLokasi;
import org.ade.monitoring.keberadaan.service.storage.PreferenceMonitoringManager;
import org.ade.monitoring.keberadaan.util.MonakJsonConverter;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsMessage;
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
//		if(receiver!=null){
//			unregisterReceiver(receiver);	
//		}
		super.onDestroy();
	}
	

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("background service", "service started");
        
        if(!pref.isAktifTracker()){
    		Tracker tracker = new Tracker(this, new HandlerMainReceiveMonitoringLocation(this));
        	tracker.startTracking();
        }
        
        Bundle bundle = intent.getExtras();
        if(bundle!=null){
        	switch(bundle.getInt(START_CALL)){
	        case RECEIVER_SMS:{
	        	receiveSMS(bundle);
	        	break;
	        }case RECEIVER_INTERNET:{
	        	break;
	        }
        }	
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
	
	private void receiveSMS(Bundle bundle){
		SmsMessage[] msgs = null;
        String str = "";            
        if (bundle != null)
        {
            Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];
            String noHP="";
            for (int i=0; i<msgs.length; i++){
                msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]); 
                noHP=msgs[i].getOriginatingAddress();
                str += msgs[i].getMessageBody().toString(); 
            }
            routeToGateMonak(str+","+noHP);
        }
	}
	
	private void routeToGateMonak(String str){
		String[] cvs = str.split(",");
		int status = 0;
        try{
        	status = Integer.parseInt(cvs[0]);
        }catch(NumberFormatException ex){
        	IPesanData pesanData = MonakJsonConverter.convertJsonToPesanData(str);
        	if(pesanData!=null){

        		ReceiverPesanData receiverPesanData = new ReceiverPesanData();
        		receiverPesanData.menerimaPesanData(this, pesanData);	
        	}
        }
		switch(status){
	    	case TipePesanMonak.RETRIEVE_LOCATION_ANAK:{
	    		ReceiverLokasi receiverLokasi = new ReceiverLokasi(this, getBinderHandlerMonak());
	    		receiverLokasi.menerimaLokasi(cvs);
	        	break;	
	    	}case TipePesanMonak.REQUEST_LOCATION_ANAK:{
	    		SenderLokasi sender = new SenderLokasi(this);
	    		sender.sendLocationSingleRequest(cvs[1]);
	        	break;
	    	}case TipePesanMonak.REQUEST_LOG_LOCATION:{
	    		ReceiverRequestLogMonak receiver = new ReceiverRequestLogMonak(this);
	    		receiver.receiveRequestLogLocation();
	    		break;
	    	}case TipePesanMonak.RETRIEVE_LOG_LOCATION:{
	    		ReceiverLogMonak receiver = new ReceiverLogMonak(this, getBinderHandlerMonak());
	    		receiver.receiveLogMonak(cvs[1], cvs);
	    		break;
	    	}case TipePesanMonak.REQUEST_START_TRACKING:{
	    		ReceiverTrackingMode tracking = new ReceiverTrackingMode(this);
	    		tracking.startTrackingMode();
	    		break;
	    	}case TipePesanMonak.REQUEST_STOP_TRACKING:{
	    		ReceiverTrackingMode tracking = new ReceiverTrackingMode(this);
	    		tracking.stopTrackingMode();
	    		break;
	    	}case TipePesanMonak.RETRIEVE_TRACKING:{
	    		ReceiverLokasi receiverLokasi = new ReceiverLokasi(this, getBinderHandlerMonak());
	    		receiverLokasi.menerimaLokasi(cvs);
	    		break;
	    	}case TipePesanMonak.REQUEST_ON_MONITORING:{
	    		ReceiverRequestOnMonitoring onMonitoring = new ReceiverRequestOnMonitoring(this);
	    		onMonitoring.startService();
	    		break;
	    	}case TipePesanMonak.PENDAFTARAN_ANAK:{
	    		ReceiverPendaftaranAnak receiver = new ReceiverPendaftaranAnak(this);
	    		receiver.receivePendaftaranAnak(cvs[2], cvs[1]);
	    		break;
	    	}
	    }
	}
	
//	private void daftarSmsReceiver(){
//		receiver = new ReceiverSMS(this);
//		IntentFilter intentfilter= new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
//		registerReceiver(receiver, intentfilter);
//		intentfilter = null;
//	}
	
	private PreferenceMonitoringManager 	pref;
	
	private BinderHandlerMonak handlerMonakBinder;

//	private ReceiverSMS receiver;
	
	private BinderService binderService;
	
	
	public final static String START_CALL				= "start_call";
	public final static String MONAK_SERVICE			= "monak_service";
	public final static String WAITING_LOCATION 		= "waiting_location";
	public final static String WAITING_LOG_LOCATION		= "waiting_log_location";
	public final static String STORAGE_WAITING_LOCATION = "storage_waiting_location";
	
	public final static int RECEIVER_SMS 				= 1;
	public final static int RECEIVER_INTERNET			= 2;
	
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
