package org.ade.monitoring.keberadaan.service.gate.monak;

import org.ade.monitoring.keberadaan.service.StartUpService;

import android.content.Context;

public class ReceiverRequestOnMonitoring {
	public ReceiverRequestOnMonitoring(Context context){
		this.context = context;
	}
	
	public void startService(){
		StartUpService.startService(context);
	}
	
	private final Context context;
}
