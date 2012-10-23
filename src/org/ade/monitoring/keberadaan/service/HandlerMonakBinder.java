package org.ade.monitoring.keberadaan.service;

import android.os.Binder;
import android.os.Handler;

public class HandlerMonakBinder extends Binder{

	public HandlerMonakBinder(BackgroundService service){
		this.service = service;
	}
	
	public void bindWaitingLocation(Handler handler){
		service.addHandlerWaiting(BackgroundService.WAITING_LOCATION, handler);
	}
	
	public void unbindWaitingLocation(){
		service.removeHandleWaiting(BackgroundService.WAITING_LOCATION);
	}

	private final BackgroundService service;
}