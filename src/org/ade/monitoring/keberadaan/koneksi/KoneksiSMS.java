package org.ade.monitoring.keberadaan.koneksi;

import java.util.Calendar;
import java.util.Date;

import org.ade.monitoring.keberadaan.entity.DataMonitoring;
import org.ade.monitoring.keberadaan.entity.Lokasi;
import org.ade.monitoring.keberadaan.entity.PesanData;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;


/**
 * Class KoneksiSMS
 */
public class KoneksiSMS extends BroadcastReceiver implements Koneksi {

	public KoneksiSMS () { };

	
	public void kirimPesanData( PesanData pesanData ){
		DataMonitoring dataMonitoring = pesanData.getDataMonitoring();
		Lokasi lokasi 		= dataMonitoring.getLokasi();
		double latitude		= lokasi.getlatitude();
		double longitude 	= lokasi.getLongitude();
		long date = dataMonitoring.getWaktuMulaiLong();
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(date);
		String teksData = latitude+","+longitude+","+cal.getTimeInMillis();
		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage("000", null, teksData, null, null);
		
	}


  	public PesanData menerimaPesanData(  ){
	  return null;
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
	}


}
