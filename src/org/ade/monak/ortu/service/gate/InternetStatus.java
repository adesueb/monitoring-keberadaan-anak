package org.ade.monak.ortu.service.gate;

import org.ade.monak.ortu.service.MonakService;
import org.ade.monak.ortu.service.storage.LogMonakFileManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

public class InternetStatus extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {

   	 LogMonakFileManager.debug("change");
		 boolean noConnectivity = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
	     if(!noConnectivity){
	    	 LogMonakFileManager.debug("connect to internet");
	    	 Intent intentService = new Intent(MonakService.MONAK_SERVICE);
	    	 intentService.putExtra(MonakService.STATUS_INTERNET, MonakService.START);
	    	 context.startService(intentService);
	     }
	}

}
