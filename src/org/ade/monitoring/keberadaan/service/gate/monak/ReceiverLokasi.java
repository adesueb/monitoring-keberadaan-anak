package org.ade.monitoring.keberadaan.service.gate.monak;

import org.ade.monitoring.keberadaan.Variable.Status;
import org.ade.monitoring.keberadaan.boundary.DaftarAnak;
import org.ade.monitoring.keberadaan.entity.Anak;
import org.ade.monitoring.keberadaan.entity.Lokasi;
import org.ade.monitoring.keberadaan.map.service.GpsManager;
import org.ade.monitoring.keberadaan.service.MonakService;
import org.ade.monitoring.keberadaan.service.gate.SenderSMS;
import org.ade.monitoring.keberadaan.util.StorageHandler;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class ReceiverLokasi {

	public ReceiverLokasi(MonakService backgroundService) {
		this.backgroundService = backgroundService;
	}	
	
	public void menerimaLokasi(String noHp, String[] cvs){
		Log.d("receiver sms", "dapet lokasi dengan lokasi :"+cvs[1]);
		if(backgroundService==null)return;
		Log.d("receiver sms", "try to get handler from service with no HP :"+noHp);
    	
		Handler handlerUI = backgroundService.getSingleUIHandler(MonakService.WAITING_LOCATION);
    	
    	
    	StorageHandler storageHandler = 
    			backgroundService.getSingleStorageHandler
    				(DaftarAnak.WAITING_LOCATION_STORAGE_HANDLER_ID, cvs[3]);
    	// FIXME : kenapa null?
    	if(storageHandler==null)return;
    	// FIXME : kenapa null?
		Log.d("receiver sms", "accept handler");
    	Message message = new Message();
    	Bundle data = new Bundle();
    	data.putDouble("latitude", Double.parseDouble(cvs[1]));
    	data.putDouble("longitude", Double.parseDouble(cvs[2]));
    	data.putString("noHp", noHp);
    	message.setData(data);
    	message.what = Status.SUCCESS;
    	
    	storageHandler.sendMessage(message);
    	backgroundService.removeStorageHandlerWaiting(DaftarAnak.WAITING_LOCATION_STORAGE_HANDLER_ID, cvs[3]);
    	
    	Message  messageHandlerUI = new Message();
    	messageHandlerUI.copyFrom(message);
    	
    	if(handlerUI==null)return;
    	handlerUI.sendMessage(messageHandlerUI);
    	
    	backgroundService.removeUIHandlerWaiting(MonakService.WAITING_LOCATION);
	}
	
	private MonakService backgroundService;
	
}
