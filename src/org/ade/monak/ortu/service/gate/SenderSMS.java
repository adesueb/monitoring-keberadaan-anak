package org.ade.monak.ortu.service.gate;

import org.ade.monak.ortu.Variable.Status;
import org.ade.monak.ortu.service.storage.LogMonakFileManager;
import org.ade.monak.ortu.util.IDGenerator;

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
        		mContext.unregisterReceiver(this);
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
        		mContext.unregisterReceiver(this);
	        }
	    }, new IntentFilter(DELIVERED));   
	    
	    
	    

	    SmsManager sms = SmsManager.getDefault();
//	    ArrayList<String> parts = sms.divideMessage(message);
	    
//	    ArrayList<PendingIntent> sentIntents = new ArrayList<PendingIntent>();
//	    ArrayList<PendingIntent> deliveryIntents = new ArrayList<PendingIntent>();

//	    for (int i = 0; i < parts.size(); i++) {
//		    sentIntents.add(sentPI);
//		    deliveryIntents.add(deliveredPI);
//	    }
//	    sms.sendMultipartTextMessage(phoneNumber, null, parts, sentIntents, deliveryIntents);
//	    sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);     
	    
	           
	    int countSms = (message.length()/MAX_SMS_MESSAGE_LENGTH)+1;
	    if(countSms==1){
	     	sms.sendDataMessage(phoneNumber, null, PORT_NUMBER, message.getBytes(), sentPI, deliveredPI);	
	    }else{
	    	
	    	String 	id 			= IDGenerator.getIdMultiSms();
		    int 	startbyte	= 0;
		    
	    	for(int i = 0;i<countSms;i++){
	  		    
	    		byte[] data = new byte[MAX_SMS_MESSAGE_LENGTH+2]; 
	  		    int indexData=0;
	  		    
	  		    for(int index = 0;index<id.length();index++){
	  		    	data[indexData] = (byte)id.charAt(index);
	  		    	indexData++;
	  		    }
	  		    
	  		    data[indexData] = (byte)'#';
	  		    indexData++;
	  		    
	  		    String indexPesan = ""+i;
	  		    for(int index = 0;index<indexPesan.length();index++){
		  		    data[indexData] = (byte)indexPesan.charAt(index);
		  		    indexData++;
	  		    }
	  		    

	  		    data[indexData] = (byte)'#';
	  		    indexData++;
	  		    
	  	    	for(int index=startbyte; index<message.length() && indexData < MAX_SMS_MESSAGE_LENGTH; index++){
    				data[indexData] = (byte)message.charAt(index);
  	                indexData++;	
  	                startbyte++;
	  	        }	
	  	    	
	  	    	if(i!=countSms-1){
	  	    		data[indexData]=(byte)'#';
	  	    		indexData++;
	  	    		data[indexData]=(byte)'N';
	  	    		indexData++;
	  	    		LogMonakFileManager.debug("kirim sms : "+new String(data));
		  	    	sms.sendDataMessage(phoneNumber, null, PORT_NUMBER, data, sentPI, deliveredPI);	
	  	    	}else{
	  	    		data[indexData]=(byte)'#';
	  	    		indexData++;
	  	    		data[indexData]=(byte)'Y';
	  	    		indexData++;
	  	    		byte[] dataEnd = new byte[indexData];
	  	    		for(int y=0;y<indexData;y++){
	  	    			dataEnd[y]=data[y];
	  	    		}
	  	    		LogMonakFileManager.debug("kirim sms Terakhir : "+new String(dataEnd));
		  	    	sms.sendDataMessage(phoneNumber, null, PORT_NUMBER, dataEnd, sentPI, deliveredPI);
		  	    	break;
	  	    	}
	  	    	
	  	    }
	  	    
	    }
	  

	}

    private static final int MAX_SMS_MESSAGE_LENGTH = 98;
  	private static final short PORT_NUMBER = 8081;
	private Context mContext;
	private Handler mHandler;
	

	
}
