package org.ade.monitoring.keberadaan.koneksi;


import org.ade.monitoring.keberadaan.entity.IPesanData;
import org.ade.monitoring.keberadaan.util.MonakJsonConverter;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;


/**
 * Class KoneksiSMS
 */
public abstract class ReceiverSMS extends BroadcastReceiver implements ReceiverKoneksi {

	public ReceiverSMS () {
		
	}

	@Override
	public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();        
        SmsMessage[] msgs = null;
        String str = "";            
        if (bundle != null)
        {
            //---retrieve the SMS message received---
            Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];
            String noHP="";
            for (int i=0; i<msgs.length; i++){
                msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]); 
                noHP=msgs[i].getOriginatingAddress();
                str += msgs[i].getMessageBody().toString(); 
            }
            
            IPesanData pesanData = MonakJsonConverter.convertJsonToPesanData(str);
            menerimaPesanData(context, pesanData);
        }
	}
	
	public abstract void menerimaPesanData(Context context, IPesanData pesanData);


}
