package org.ade.monak.ortu.service;

import org.ade.monak.ortu.Variable.TipePesanMonak;
import org.ade.monak.ortu.entity.IPesanData;
import org.ade.monak.ortu.service.gate.InternetPushMonak;
import org.ade.monak.ortu.service.gate.monak.ReceiverKonfirmasi;
import org.ade.monak.ortu.service.gate.monak.ReceiverLogMonak;
import org.ade.monak.ortu.service.gate.monak.ReceiverLokasi;
import org.ade.monak.ortu.service.gate.monak.ReceiverPesanData;
import org.ade.monak.ortu.service.storage.PreferenceMonitoringManager;
import org.ade.monak.ortu.util.MonakJsonConverter;

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

		internetPush = new InternetPushMonak(this);
		internetPush.startConnection();
	}

	
	
	@Override
	public void onDestroy() {
		pref.setInActiveService();
		if(internetPush!=null){
			internetPush.stopConnection();
			internetPush = null;
		}
		super.onDestroy();
	}
	

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) { 
        
        if(intent!=null){
        	Bundle bundle = intent.getExtras();
            if(bundle!=null){
            	switch(bundle.getInt(START_CALL)){
	    	        case RECEIVER_SMS:{
	    	        	Log.d("background service", "masuk receiver SMS");
	    	        	receiveSMS(bundle);
	    	        	break;
	    	        }case RECEIVER_INTERNET:{
	    	        	routeToGateMonak(bundle.getString(PESAN_INTERNET));
	    	        	break;
	    	        }
            	}	
            	
            	if(bundle.getInt(STATUS_INTERNET)==START){
            		if(!internetPush.isStart()){
                		internetPush.startConnection();            			
            		}

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
		Log.d("MonakService", str);
		
        try{
        	status = Integer.parseInt(cvs[0]);
        }catch(NumberFormatException ex){

			Log.d("MonakService", str);
			
        	IPesanData pesanData = MonakJsonConverter.convertJsonToPesanData(str);
        	if(pesanData!=null){

        		ReceiverPesanData receiverPesanData = new ReceiverPesanData();
        		receiverPesanData.menerimaPesanData(this, pesanData, getBinderHandlerMonak());	
        	}
        }
		switch(status){
	    	case TipePesanMonak.RETRIEVE_LOCATION_ANAK:{
	    		ReceiverLokasi receiverLokasi = new ReceiverLokasi(this, getBinderHandlerMonak());
	    		receiverLokasi.menerimaLokasi(cvs);
	        	break;	
	    	}case TipePesanMonak.RETRIEVE_LOG_LOCATION:{
	    		ReceiverLogMonak receiver = new ReceiverLogMonak(this, getBinderHandlerMonak());
	    		receiver.receiveLogMonak(cvs[1], cvs);
	    		break;
	    	}case TipePesanMonak.RETRIEVE_TRACKING:{
	    		ReceiverLokasi receiverLokasi = new ReceiverLokasi(this, getBinderHandlerMonak());
	    		receiverLokasi.menerimaLokasi(cvs);
	    		break;
	    	}case TipePesanMonak.KONFIRMASI_AKTIF_MONITORING:{
	    		ReceiverKonfirmasi receiver = new ReceiverKonfirmasi(this, getBinderHandlerMonak());
	    		receiver.receiveKonfirmasiAktifMonitoring(cvs);
	    		break;
	    	}case TipePesanMonak.KONFIRMASI_DEAKTIF_MONITORING:{
	    		ReceiverKonfirmasi receiver = new ReceiverKonfirmasi(this, getBinderHandlerMonak());
	    		receiver.receiveKonfirmasiDeAktifMonitoring(cvs);
	    		break;
	    	}case TipePesanMonak.KONFIRMASI_DEAKTIF_ANAK:{
	    		ReceiverKonfirmasi receiver = new ReceiverKonfirmasi(this, getBinderHandlerMonak());
	    		receiver.receiveKonfirmasiDeAktifAnak(cvs);
	    		break;
	    	}case TipePesanMonak.KONFIRMASI_HAPUS_ANAK:{
	    		ReceiverKonfirmasi receiver = new ReceiverKonfirmasi(this, getBinderHandlerMonak());
	    		receiver.receiverKonfirmasiHapusAnak(cvs);
	    		break;
	    	}case TipePesanMonak.KONFIRMASI_START_TRACKING:{
	    		ReceiverKonfirmasi receiver = new ReceiverKonfirmasi(this, getBinderHandlerMonak());
	    		receiver.receiveKonfirmasiStartTrack(cvs);
	    		break;
	    	}case TipePesanMonak.KONFIRMASI_STOP_TRACKING:{
	    		ReceiverKonfirmasi receiver = new ReceiverKonfirmasi(this, getBinderHandlerMonak());
	    		receiver.receiveKonfirmasiStopTrack(cvs);
	    		break;
	    	}
	    }
	}
	
	private PreferenceMonitoringManager 	pref;
	
	private BinderHandlerMonak handlerMonakBinder;

	private InternetPushMonak internetPush;
	
	private BinderService binderService;
	
	public final static String STATUS_INTERNET					= "status_internet";
	public final static String PESAN_INTERNET					= "pesan_internet";
	public final static String START_CALL						= "start_call";
	public final static String MONAK_SERVICE					= "monak_service";
	public final static String WAITING_LOCATION 				= "waiting_location";
	public final static String WAITING_LOG_LOCATION				= "waiting_log_location";
	public final static String WAITING_KONFIRMASI_AKTIF			= "waiting_konfirmasi_aktif";
	public final static String WAITING_KONFIRMASI_HAPUS_ANAK 	= "waiting_konfirmasi_hapus";
	public final static String WAITING_KONFIRMASI_TRACK			= "waiting_konfirmasi_track";
	public final static String STORAGE_WAITING_LOCATION 		= "storage_waiting_location";
	
	public final static int RECEIVER_SMS 				= 1;
	public final static int RECEIVER_INTERNET			= 2;
	

	public final static int START	= 1;
	public final static int STOP	= 2;
	
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
