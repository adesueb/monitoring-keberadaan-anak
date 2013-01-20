package org.ade.monitoring.keberadaan.service.gate;

import org.ade.monitoring.keberadaan.service.MonakService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class ReceiverSMS extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
    	Log.d("emboh", "emboh kah");
		Intent intentService = new Intent(MonakService.MONAK_SERVICE);
        Bundle bundle = intent.getExtras();
		bundle.putInt(MonakService.START_CALL, MonakService.RECEIVER_SMS);
		intentService.putExtras(bundle);
		context.startService(intentService);        
	}

}
