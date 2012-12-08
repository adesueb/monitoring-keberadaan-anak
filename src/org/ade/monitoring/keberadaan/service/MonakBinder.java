package org.ade.monitoring.keberadaan.service;

import org.ade.monitoring.keberadaan.util.StorageHandler;

import android.os.Binder;
import android.os.Handler;

public class MonakBinder extends Binder{

	public MonakBinder(MonakService service){
		this.service = service;
	}
	
	public void bindStorageHandler(String idHandler, StorageHandler handler){
		service.addStorageHandlerWaiting(idHandler, handler);
	}
	
	public void unbindStorageHandler(String idHandler, StorageHandler handler){
		service.removeStorageHandlerWaiting(idHandler, handler);
	}
	
	public void bindUIHandlerWaitingLocation(Handler handler){
		service.addHandlerUIWaiting(MonakService.WAITING_LOCATION, handler);
	}
	
	public void unbindUIHandlerWaitingLocation(){
		service.removeUIHandlerWaiting(MonakService.WAITING_LOCATION);
	}

	private final MonakService service;
}