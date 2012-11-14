package org.ade.monitoring.keberadaan.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class StartUpService extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent service = new Intent(context, MonakService.class);
	    context.startService(service);
	}
 
}
