package org.ade.monitoring.keberadaan.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class StartUpService extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		StartUpService.startService(context);
	}
	
	public final static void startService(Context context){
		Intent service = new Intent("monak_service");
	    context.startService(service);
	}
 
}
