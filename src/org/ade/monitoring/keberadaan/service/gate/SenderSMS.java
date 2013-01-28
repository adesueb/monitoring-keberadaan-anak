package org.ade.monitoring.keberadaan.service.gate;

import java.util.ArrayList;

import org.ade.monitoring.keberadaan.Variable.Status;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.telephony.SmsManager;
import android.util.Log;

public class SenderSMS{
	
	public SenderSMS (Context context, Handler handler) {
		mContext = context;
		mHandler = handler;	
	}


  	public void sendSMS(final String phoneNumber, final String message){        
	    String SENT = "SMS_SENT";
	    String DELIVERED = "SMS_DELIVERED";

	    // menghentikan pengiriman sms saat melakukan sms ke nomor yg g da....
	    if(phoneNumber==null||phoneNumber.equals("")){
	    	return;
	    }
	    //....................................................................
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
	                	if(mHandler!=null){
	                		mHandler.sendEmptyMessage(Status.FAILED);	
	                	}
	                	break;
	                case SmsManager.RESULT_ERROR_NO_SERVICE:
	                	if(mHandler!=null){
	                		mHandler.sendEmptyMessage(Status.FAILED);	
	                	}
	                	break;
	                case SmsManager.RESULT_ERROR_NULL_PDU:
	                	if(mHandler!=null){
	                		mHandler.sendEmptyMessage(Status.FAILED);	
	                	}
	                	break;
	                case SmsManager.RESULT_ERROR_RADIO_OFF:
	                	if(mHandler!=null){
	                		mHandler.sendEmptyMessage(Status.FAILED);	
	                	}
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
	                	if(mHandler!=null){
	                		mHandler.sendEmptyMessage(Status.SUCCESS);	
	                	}
	                	break;
	                case Activity.RESULT_CANCELED:
	                	if(mHandler!=null){
		                	mHandler.sendEmptyMessage(Status.FAILED);	                		
	                	}
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
	private Handler mHandler;
	

	
}
