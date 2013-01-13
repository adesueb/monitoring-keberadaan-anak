package org.ade.monitoring.keberadaan.service.gate.monak;

import org.ade.monitoring.keberadaan.Variable.Status;
import org.ade.monitoring.keberadaan.boundary.DaftarAnak;
import org.ade.monitoring.keberadaan.service.BinderHandlerMonak;
import org.ade.monitoring.keberadaan.service.IBindMonakServiceConnection;
import org.ade.monitoring.keberadaan.service.MonakService;
import org.ade.monitoring.keberadaan.service.ServiceMonakConnection;
import org.ade.monitoring.keberadaan.util.StorageHandler;

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
		ServiceMonakConnection serviceConnection = new ServiceMonakConnection(this);
		context.bindService(intent, serviceConnection, 0);
	}
	
	private void action(){
		Log.d("receiver sms", "dapet lokasi dengan lokasi :"+cvs[1]);
		if(!bound){
			return;
		}
		if(binderHandlerMonak==null)return;
		Log.d("receiver sms", "try to get handler from service with no HP :"+noHp);
    	
		Handler handlerUI = binderHandlerMonak.getSingleBindUIHandler(MonakService.WAITING_LOCATION);
    	
    	
    	StorageHandler storageHandler = 
    			binderHandlerMonak.getSingleBindStorageHandler
    				(DaftarAnak.WAITING_LOCATION_STORAGE_HANDLER_ID, cvs[3]);
    	if(storageHandler==null)return;
		Log.d("receiver sms", "accept handler");
    	Message message = new Message();
    	Bundle data = new Bundle();
    	data.putDouble("latitude", Double.parseDouble(cvs[1]));
    	data.putDouble("longitude", Double.parseDouble(cvs[2]));
    	data.putString("noHp", noHp);
    	message.setData(data);
    	message.what = Status.SUCCESS;
    	
    	storageHandler.sendMessage(message);
    	binderHandlerMonak.unbindStorageHandler(DaftarAnak.WAITING_LOCATION_STORAGE_HANDLER_ID, cvs[3]);
    	
    	Message  messageHandlerUI = new Message();
    	messageHandlerUI.copyFrom(message);
    	
    	if(handlerUI==null)return;
    	handlerUI.sendMessage(messageHandlerUI);
    	
    	binderHandlerMonak.unbindUIHandlerWaitingLocation();
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
	
	private final Context context;
	
	private String 		noHp;
	private String[] 	cvs;
	
}
