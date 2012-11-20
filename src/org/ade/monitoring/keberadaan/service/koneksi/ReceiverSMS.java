package org.ade.monitoring.keberadaan.service.koneksi;


import org.ade.monitoring.keberadaan.Variable.Status;
import org.ade.monitoring.keberadaan.Variable.TipePesanData;
import org.ade.monitoring.keberadaan.entity.DataMonitoring;
import org.ade.monitoring.keberadaan.entity.IPesanData;
import org.ade.monitoring.keberadaan.entity.Lokasi;
import org.ade.monitoring.keberadaan.entity.Peringatan;
import org.ade.monitoring.keberadaan.lokasi.GpsManager;
import org.ade.monitoring.keberadaan.service.MonakService;
import org.ade.monitoring.keberadaan.service.Notifikasi;
import org.ade.monitoring.keberadaan.service.storage.DatabaseManager;
import org.ade.monitoring.keberadaan.util.MonakJsonConverter;

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


/**
 * Class KoneksiSMS
 */
public class ReceiverSMS extends BroadcastReceiver {

	public ReceiverSMS (MonakService backgroundService) {
		this.backgroundService = backgroundService;
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
            if(cvs[0].equals("location")){
            	menerimaLokasi(noHP,cvs);
            	return;
            }else if(str.equals(SenderSMS.REQUEST_LOCATION_ANAK)){
            	LocationReceiverHandler locationHandler = new LocationReceiverHandler(context, noHP);
            	GpsManager gpsManager = new GpsManager(context, locationHandler);
            	gpsManager.searchLokasi();
            }else{
            	IPesanData pesanData = MonakJsonConverter.convertJsonToPesanData(str);
            	if(pesanData!=null){
            		menerimaPesanData(context, pesanData);	
            	}
            }
            
        }
	}
	
	private void menerimaPesanData(Context context, IPesanData pesanData){
		if(pesanData!=null){
			
		}else if(pesanData.getTipe()==TipePesanData.DATAMONITORING_BARU){
			new DatabaseManager(context).addDataMonitoring((DataMonitoring) pesanData);
		}else{
			new Notifikasi(context).tampilkanNotifikasiPeringatan((Peringatan) pesanData);
			
		}
	}
	
	private void menerimaLokasi(String noHp, String[] cvs){
		if(backgroundService==null)return;
    	Handler handler = backgroundService.getSingleHandler(MonakService.WAITING_LOCATION);
    	if(handler==null)return;
    	Message message = new Message();
    	Bundle data = new Bundle();
    	data.putString("latitude", cvs[1]);
    	data.putString("longitude", cvs[2]);
    	data.putString("noHp", noHp);
    	message.setData(data);
    	message.what = Status.SUCCESS;
    	handler.sendMessage(message);
    	backgroundService.removeHandleWaiting(MonakService.WAITING_LOCATION);
	}
	
	private MonakService backgroundService;
	
	private static class LocationReceiverHandler extends Handler{

		public LocationReceiverHandler(Context context, String noHp){
			this.context 	= context;
			this.noHp		= noHp;
		}
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch(msg.what){
				case Status.SUCCESS:{
					Bundle bundle = msg.getData();
					if(bundle!=null){
						Lokasi lokasi = new Lokasi();
						lokasi.setLatitude(bundle.getDouble("latitude"));
						lokasi.setLongitude(bundle.getDouble("longitude"));
						SenderSMS senderSms = new SenderSMS(context, null);
						senderSms.kirimResponseLokasiAnak(noHp, lokasi);
					}
					break;
				}case Status.FAILED:{
					break;
				}
			}
			
		}
		private final String	noHp;
		private final Context 	context;
		
	}

}
