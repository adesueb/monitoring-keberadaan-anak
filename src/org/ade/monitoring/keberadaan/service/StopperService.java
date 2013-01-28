package org.ade.monitoring.keberadaan.service;

import android.content.Context;
import android.content.Intent;

public class StopperService {
	public final static void stopService(Context context){
		Intent service = new Intent(MonakService.MONAK_SERVICE);
	    context.stopService(service);
	}
}
