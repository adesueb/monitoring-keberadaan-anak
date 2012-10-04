package org.ade.monitoring.keberadaan.koneksi;


import org.ade.monitoring.keberadaan.entity.PesanData;

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
		   //---get the SMS message passed in---
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
                str += "SMS from " + msgs[i].getOriginatingAddress();                     
                str += " :";
                str += msgs[i].getMessageBody().toString();
                str += "\n";        
            }
            //---display the new SMS message---
        }
        menerimaPesanData(null);
	}
	
	public abstract void menerimaPesanData(PesanData pesanData);


}
