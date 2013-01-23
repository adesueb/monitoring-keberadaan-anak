package org.ade.monitoring.keberadaan.service.gate.monak;

import org.ade.monitoring.keberadaan.Variable.Status;
import org.ade.monitoring.keberadaan.entity.Anak;
import org.ade.monitoring.keberadaan.entity.Lokasi;
import org.ade.monitoring.keberadaan.service.BinderHandlerMonak;
import org.ade.monitoring.keberadaan.service.MonakService;
import org.ade.monitoring.keberadaan.service.storage.DatabaseManager;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class ReceiverLokasi{

	public ReceiverLokasi(Context context, BinderHandlerMonak binder) {
		this.context 			= context;
		this.binderHandlerMonak = binder;
	}	
	
	public void menerimaLokasi(String[] cvs){
		this.cvs	= cvs;
		action();
	
	}
	
	private void action(){
		
		Log.d("receiver sms", "dapet lokasi dengan lokasi :"+cvs[1]);
			
		Lokasi lokasi = new Lokasi();
		lokasi.setLatitude(Double.parseDouble(cvs[1]));
		lokasi.setLongitude(Double.parseDouble(cvs[2]));
		lokasi.setTime(Long.parseLong(cvs[3]));
		
		Anak anak = new Anak();
		anak.setIdAnak(cvs[4]);
		anak.setLastLokasi(lokasi);
		
		lokasi.setAnak(anak);
		
		DatabaseManager databaseManager = new DatabaseManager(context);
		databaseManager.addLastLokasiAnak(anak);
		
		if(binderHandlerMonak==null)return;
    	
		Handler handlerUI = binderHandlerMonak.getSingleBindUIHandler(MonakService.WAITING_LOCATION);
    	if(handlerUI!=null){
    		Message  message = new Message();
        	Bundle data = new Bundle();
        	data.putDouble("latitude", Double.parseDouble(cvs[1]));
        	data.putDouble("longitude", Double.parseDouble(cvs[2]));
        	data.putLong("time", Long.parseLong(cvs[3]));
        	data.putString("idAnak", cvs[4]);
        	message.setData(data);
        	message.what = Status.SUCCESS;
        	
        	handlerUI.sendMessage(message);
    	}
	}
	
	private BinderHandlerMonak binderHandlerMonak;
	
	private final Context context;
	
	private String[] 	cvs;
	
}
