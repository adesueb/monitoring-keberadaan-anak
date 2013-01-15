package org.ade.monitoring.keberadaan.service.gate.monak;

import org.ade.monitoring.keberadaan.Variable.Status;
import org.ade.monitoring.keberadaan.entity.Anak;
import org.ade.monitoring.keberadaan.entity.Lokasi;
import org.ade.monitoring.keberadaan.service.BinderHandlerMonak;
import org.ade.monitoring.keberadaan.service.MonakService;
import org.ade.monitoring.keberadaan.service.storage.DatabaseManager;
import org.ade.monitoring.keberadaan.service.util.IBindMonakServiceConnection;
import org.ade.monitoring.keberadaan.service.util.ServiceMonakConnection;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class ReceiverLokasi implements IBindMonakServiceConnection{

	public ReceiverLokasi(Context context) {
		this.context = context;
	}	
	
	public void menerimaLokasi(String noHp, String[] cvs){
		this.noHp 	= noHp;
		this.cvs	= cvs;
		
		Intent intent = new Intent("monak_service");
		serviceConnection = new ServiceMonakConnection(this);
		context.bindService(intent, serviceConnection, 0);
	}
	
	private void action(){
		
		Log.d("receiver sms", "dapet lokasi dengan lokasi :"+cvs[1]);
		if(!bound){
			return;
		}
		
		Lokasi lokasi = new Lokasi();
		lokasi.setLatitude(Double.parseDouble(cvs[1]));
		lokasi.setLongitude(Double.parseDouble(cvs[2]));
		
		Anak anak = new Anak();
		anak.setIdAnak(cvs[3]);
		anak.setLokasi(lokasi);
		
		DatabaseManager databaseManager = new DatabaseManager(context);
		databaseManager.updateLokasiAnak(anak);
		
		if(binderHandlerMonak==null)return;
		Log.d("receiver sms", "try to get handler from service with no HP :"+noHp);
    	
		Handler handlerUI = binderHandlerMonak.getSingleBindUIHandler(MonakService.WAITING_LOCATION);
    	if(handlerUI!=null){
    		Message  message = new Message();
        	Bundle data = new Bundle();
        	data.putDouble("latitude", Double.parseDouble(cvs[1]));
        	data.putDouble("longitude", Double.parseDouble(cvs[2]));
        	data.putString("noHp", noHp);
        	data.putString("idAnak", cvs[3]);
        	message.setData(data);
        	message.what = Status.SUCCESS;
        	
        	handlerUI.sendMessage(message);
        	
        	binderHandlerMonak.unBindUIHandlerWaitingLocation();
        	
    	}
    	
    	if(bound){
    		context.unbindService(serviceConnection);
    	}
	}
	
	public void setBinderHandlerMonak(BinderHandlerMonak binderHandlerMonak) {
		this.binderHandlerMonak = binderHandlerMonak;
		action();
	}

	public void setBound(boolean bound) {
		this.bound = bound;
	}
	
	private BinderHandlerMonak binderHandlerMonak;
	private boolean bound;
	
	private ServiceMonakConnection serviceConnection;
	
	private final Context context;
	
	private String 		noHp;
	private String[] 	cvs;
	
}
