package org.ade.monitoring.keberadaan.service.gate;

import org.ade.monitoring.keberadaan.Variable.TipePesanMonak;
import org.ade.monitoring.keberadaan.entity.IPesanData;
import org.ade.monitoring.keberadaan.service.gate.monak.ReceiverRequestLogMonak;
import org.ade.monitoring.keberadaan.service.gate.monak.ReceiverLokasi;
import org.ade.monitoring.keberadaan.service.gate.monak.ReceiverRequestOnMonitoring;
import org.ade.monitoring.keberadaan.service.gate.monak.ReceiverPendaftaranAnak;
import org.ade.monitoring.keberadaan.service.gate.monak.ReceiverPesanData;
import org.ade.monitoring.keberadaan.service.gate.monak.ReceiverTrackingMode;
import org.ade.monitoring.keberadaan.service.gate.monak.SenderLokasi;
import org.ade.monitoring.keberadaan.util.MonakJsonConverter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

public class ReceiverSMS extends BroadcastReceiver {

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

            		ReceiverPesanData receiverPesanData = new ReceiverPesanData();
            		receiverPesanData.menerimaPesanData(context, pesanData);	
            	}
            }
            
            switch(status){
            	case TipePesanMonak.RETRIEVE_LOCATION_ANAK:{
            		ReceiverLokasi receiverLokasi = new ReceiverLokasi(context);
            		receiverLokasi.menerimaLokasi(noHP,cvs);
                	break;	
            	}case TipePesanMonak.REQUEST_LOCATION_ANAK:{
            		SenderLokasi sender = new SenderLokasi(context);
            		sender.sendLocationSingleRequest(cvs[1]);
                	break;
            	}case TipePesanMonak.REQUEST_LOG_LOCATION:{
            		ReceiverRequestLogMonak receiver = new ReceiverRequestLogMonak(context);
            		receiver.receiveRequestLogLocation();
            		break;
            	}case TipePesanMonak.RETRIEVE_LOG_LOCATION:{
            		break;
            	}case TipePesanMonak.REQUEST_START_TRACKING:{
            		ReceiverTrackingMode tracking = new ReceiverTrackingMode(context);
            		tracking.startTrackingMode();
            		break;
            	}case TipePesanMonak.REQUEST_STOP_TRACKING:{
            		ReceiverTrackingMode tracking = new ReceiverTrackingMode(context);
            		tracking.stopTrackingMode();
            		break;
            	}case TipePesanMonak.RETRIEVE_TRACKING:{
            		break;
            	}case TipePesanMonak.REQUEST_ON_MONITORING:{
            		ReceiverRequestOnMonitoring onMonitoring = new ReceiverRequestOnMonitoring(context);
            		onMonitoring.startService();
            		break;
            	}case TipePesanMonak.PENDAFTARAN_ANAK:{
            		ReceiverPendaftaranAnak receiver = new ReceiverPendaftaranAnak(context);
            		receiver.receivePendaftaranAnak(noHP, cvs[1]);
            		break;
            	}
            }
            
        }
	}

}
