package org.ade.monitoring.keberadaan.service;

import android.os.Binder;
import android.os.Handler;

public class HandlerMonakBinder extends Binder{

	public HandlerMonakBinder(MonakService service){
		this.service = service;
	}
	
	public void bindWaitingLocation(Handler handler){
		service.addHandlerWaiting(MonakService.WAITING_LOCATION, handler);
	}
	
	public void unbindWaitingLocation(){
		service.removeHandleWaiting(MonakService.WAITING_LOCATION);
	}

	private final MonakService service;
}