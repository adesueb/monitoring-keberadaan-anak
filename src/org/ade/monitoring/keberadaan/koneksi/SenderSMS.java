package org.ade.monitoring.keberadaan.koneksi;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.ade.monitoring.keberadaan.entity.DataMonitoring;
import org.ade.monitoring.keberadaan.entity.DateMonitoring;
import org.ade.monitoring.keberadaan.entity.DayMonitoring;
import org.ade.monitoring.keberadaan.entity.Lokasi;
import org.ade.monitoring.keberadaan.entity.PesanData;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

public class SenderSMS {
	
	public SenderSMS (Context context) {
		mContext = context;
	}

	public void kirimPesanData( PesanData pesanData ){
		DataMonitoring dataMonitoring = pesanData.getDataMonitoring();
		
		String phoneNumber = dataMonitoring.getAnak().getNoHpAnak();
		sendSMS(phoneNumber, pesanData.getJsonPesanData());
	}


  	public void sendSMS(final String phoneNumber, final String message){        
	    String SENT = "SMS_SENT";
	    String DELIVERED = "SMS_DELIVERED";

	    PendingIntent sentPI = PendingIntent.getBroadcast(mContext, 0,
	        new Intent(SENT), 0);

	    PendingIntent deliveredPI = PendingIntent.getBroadcast(mContext, 0,
	        new Intent(DELIVERED), 0);

	    //---when the SMS has been sent---
	    mContext.registerReceiver(new BroadcastReceiver(){
	        @Override
	        public void onReceive(Context arg0, Intent arg1) {
	            switch (getResultCode())
	            {
	                case Activity.RESULT_OK:
	                	Log.d("sent", "terkirim ke : "+phoneNumber+
								"\n"+"pesan : "+message);
	                     break;
	                case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
	                    Toast.makeText(mContext, "Generic failure", 
	                            Toast.LENGTH_SHORT).show();
	                    break;
	                case SmsManager.RESULT_ERROR_NO_SERVICE:
	                    Toast.makeText(mContext, "No service", 
	                            Toast.LENGTH_SHORT).show();
	                    break;
	                case SmsManager.RESULT_ERROR_NULL_PDU:
	                    Toast.makeText(mContext, "Null PDU", 
	                            Toast.LENGTH_SHORT).show();
	                    break;
	                case SmsManager.RESULT_ERROR_RADIO_OFF:
	                    Toast.makeText(mContext, "Radio off", 
	                            Toast.LENGTH_SHORT).show();
	                    break;
	            }
	        }
	    }, new IntentFilter(SENT));

	    //---when the SMS has been delivered---
	    mContext.registerReceiver(new BroadcastReceiver(){
	        @Override
	        public void onReceive(Context arg0, Intent arg1) {
	            switch (getResultCode())
	            {
	                case Activity.RESULT_OK:
	                    Toast.makeText(mContext, "SMS delivered", 
	                            Toast.LENGTH_SHORT).show();
	                    break;
	                case Activity.RESULT_CANCELED:
	                    Toast.makeText(mContext, "SMS not delivered", 
	                            Toast.LENGTH_SHORT).show();
	                    break;                        
	            }
	        }
	    }, new IntentFilter(DELIVERED));   
	    
	    

	    SmsManager sms = SmsManager.getDefault();
	    ArrayList<String> parts = sms.divideMessage(message);
	    
	    ArrayList<PendingIntent> sentIntents = new ArrayList<PendingIntent>();
	    ArrayList<PendingIntent> deliveryIntents = new ArrayList<PendingIntent>();

	    for (int i = 0; i < parts.size(); i++) {
		    sentIntents.add(sentPI);
		    deliveryIntents.add(deliveredPI);
	    }
	    
	    sms.sendMultipartTextMessage(phoneNumber, null, parts, sentIntents, deliveryIntents);
//	    sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);        
	}
  	

	private Context mContext;
}
