package org.ade.monitoring.keberadaan.service.gate.monak;

import org.ade.monitoring.keberadaan.service.StopperService;

import android.content.Context;

public class ReceiverStopMonitoring {
	public ReceiverStopMonitoring(Context context){
		this.context = context;
	}
	
	public void stopService(){
		StopperService.stopService(context);
	}
	
	private final Context context;
}
