package org.ade.monak.ortu.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class StartUpService extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		StartUpService.startService(context);
	}
	
	public final static void startService(Context context){
		Intent service = new Intent(MonakService.MONAK_SERVICE);
	    context.startService(service);

	}
 
}
