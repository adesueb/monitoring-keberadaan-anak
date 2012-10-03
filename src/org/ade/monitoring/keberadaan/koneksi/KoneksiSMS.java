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
public abstract class KoneksiSMS extends BroadcastReceiver implements Koneksi {

	public KoneksiSMS (Context context) {
		mContext = context;
	}

	
	public void kirimPesanData( PesanData pesanData ){
//		DataMonitoring dataMonitoring = pesanData.getDataMonitoring();
//		Lokasi lokasi 		= dataMonitoring.getLokasi();
//		double latitude		= lokasi.getlatitude();
//		double longitude 	= lokasi.getLongitude();
//		long date = dataMonitoring.getWaktuMulaiLong();
//		Calendar cal = Calendar.getInstance();
//		cal.setTimeInMillis(date);
//		String teksData = latitude+","+longitude+","+cal.getTimeInMillis();
//		SmsManager sms = SmsManager.getDefault();
//		sms.sendTextMessage("000", null, teksData, null, null);
		
	}


  	public void sendSMS(final String phoneNumber, final String message)
	{        
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
	    sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);        
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



	private Context mContext;

}
