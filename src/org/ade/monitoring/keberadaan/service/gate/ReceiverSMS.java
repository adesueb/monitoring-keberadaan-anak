package org.ade.monitoring.keberadaan.service.gate;


import org.ade.monitoring.keberadaan.Variable.Status;
import org.ade.monitoring.keberadaan.Variable.TipePesanMonak;
import org.ade.monitoring.keberadaan.boundary.DaftarAnak;
import org.ade.monitoring.keberadaan.entity.Anak;
import org.ade.monitoring.keberadaan.entity.DataMonitoring;
import org.ade.monitoring.keberadaan.entity.IPesanData;
import org.ade.monitoring.keberadaan.entity.Lokasi;
import org.ade.monitoring.keberadaan.entity.Peringatan;
import org.ade.monitoring.keberadaan.map.service.GpsManager;
import org.ade.monitoring.keberadaan.service.MonakService;
import org.ade.monitoring.keberadaan.service.Notifikasi;
import org.ade.monitoring.keberadaan.service.gate.monak.ReceiverLokasi;
import org.ade.monitoring.keberadaan.service.gate.monak.ReceiverPesanData;
import org.ade.monitoring.keberadaan.service.gate.monak.SenderLokasi;
import org.ade.monitoring.keberadaan.service.storage.DatabaseManager;
import org.ade.monitoring.keberadaan.util.MonakJsonConverter;
import org.ade.monitoring.keberadaan.util.StorageHandler;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class ReceiverSMS extends BroadcastReceiver {

	public ReceiverSMS (MonakService backgroundService) {
		this.backgroundService = backgroundService;
		this.receiverPesanData = new ReceiverPesanData();

		this.receiverLokasi = new ReceiverLokasi(backgroundService);
		
		this.senderLokasi = new SenderLokasi(backgroundService);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();        
        SmsMessage[] msgs = null;
        String str = "";            
        if (bundle != null)
        {
            Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];
            String noHP="";
            for (int i=0; i<msgs.length; i++){
                msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]); 
                noHP=msgs[i].getOriginatingAddress();
                str += msgs[i].getMessageBody().toString(); 
            }
            String[] cvs = str.split(",");
            
            int status = 0;
            try{
            	status = Integer.parseInt(cvs[0]);
            }catch(NumberFormatException ex){
            	IPesanData pesanData = MonakJsonConverter.convertJsonToPesanData(str);
            	if(pesanData!=null){
            		receiverPesanData.menerimaPesanData(context, pesanData);	
            	}
            }
            
            switch(status){
            	case TipePesanMonak.RETRIEVE_LOCATION_ANAK:{
            		receiverLokasi.menerimaLokasi(noHP,cvs);
                	break;	
            	}case TipePesanMonak.REQUEST_LOCATION_ANAK:{
                	senderLokasi.sendLocation(noHP, cvs[1]);
                	break;
            	}case TipePesanMonak.REQUEST_LOG_LOCATION:{
            		break;
            	}case TipePesanMonak.RETRIEVE_LOG_LOCATION:{
            		break;
            	}case TipePesanMonak.REQUEST_START_TRACKING:{
            		break;
            	}case TipePesanMonak.REQUEST_STOP_TRACKING:{
            		break;
            	}case TipePesanMonak.RETRIEVE_TRACKING:{
            		break;
            	}
            }
            
        }
	}
	

	private MonakService backgroundService;
	
	private ReceiverPesanData receiverPesanData;
	
	private ReceiverLokasi receiverLokasi;
	
	private SenderLokasi senderLokasi;

}
