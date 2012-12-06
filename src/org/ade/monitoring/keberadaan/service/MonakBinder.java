package org.ade.monitoring.keberadaan.service;

import android.os.Binder;
import android.os.Handler;

public class MonakBinder extends Binder{

	public MonakBinder(MonakService service){
		this.service = service;
	}
	
	public void bindHandlerStorage(String idHandler, Handler handler){
		service.addHandlerUIWaiting(idHandler, handler);
	}
	
	public void unbindHandlerStorage(String idHandler){
		service.removeUIHandlerWaiting(idHandler);
	}
	
	public void bindUIHandlerWaitingLocation(Handler handler){
		service.addHandlerUIWaiting(MonakService.WAITING_LOCATION, handler);
	}
	
	public void unbindUIHandlerWaitingLocation(){
		service.removeUIHandlerWaiting(MonakService.WAITING_LOCATION);
	}

	private final MonakService service;
}