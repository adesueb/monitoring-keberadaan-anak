package org.ade.monak.ortu.service.gate.monak;

import org.ade.monak.ortu.Variable.Status;
import org.ade.monak.ortu.entity.Anak;
import org.ade.monak.ortu.entity.Lokasi;
import org.ade.monak.ortu.service.BinderHandlerMonak;
import org.ade.monak.ortu.service.MonakService;
import org.ade.monak.ortu.service.storage.DatabaseManagerOrtu;
import org.ade.monak.ortu.service.storage.LogMonakFileManager;
import org.ade.monak.ortu.util.KontrolLog;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class ReceiverLokasi{

	public ReceiverLokasi(Context context, BinderHandlerMonak binder) {
		this.context 			= context;
		this.binderHandlerMonak = binder;
	}	
	
	public void menerimaLokasi(String[] cvs){
		this.cvs	= cvs;
		action();
	
	}
	
	private void action(){
		
		Log.d("receiver sms", "dapet lokasi dengan lokasi :"+cvs[1]);
			
		Lokasi lokasi = new Lokasi();
		lokasi.setLatitude(Double.parseDouble(cvs[1]));
		lokasi.setLongitude(Double.parseDouble(cvs[2]));
		lokasi.setTime(Long.parseLong(cvs[3]));
		lokasi.setLog(true);
		Anak anak = new Anak();
		anak.setIdAnak(cvs[4]);
		anak.setLastLokasi(lokasi);	
		lokasi.setAnak(anak);
		
		DatabaseManagerOrtu databaseManager = new DatabaseManagerOrtu(context);
		Lokasi lastLokasi = databaseManager.getAnakById(cvs[4], false, false).getLastLokasi();
		if(lastLokasi==null||lastLokasi.getlatitude()!=lastLokasi.getlatitude()||lastLokasi.getLongitude()!=lokasi.getLongitude()){
			databaseManager.addLastLokasiAnak(anak);	

		}

		KontrolLog.kontrolDeleteLogLokasi(databaseManager);
		
		if(binderHandlerMonak==null)return;
    	
		Handler handlerUI = binderHandlerMonak.getSingleBindUIHandler(MonakService.WAITING_LOCATION);
    	if(handlerUI!=null){
    		Message  message = new Message();
        	Bundle data = new Bundle();
        	data.putDouble("latitude", Double.parseDouble(cvs[1]));
        	data.putDouble("longitude", Double.parseDouble(cvs[2]));
        	data.putLong("time", Long.parseLong(cvs[3]));
        	data.putString("idAnak", cvs[4]);
        	message.setData(data);
        	message.what = Status.SUCCESS;
        	
        	handlerUI.sendMessage(message);
    	}
    	
    	Handler handler = binderHandlerMonak.getSingleBindUIHandler(MonakService.WAITING_KONFIRMASI_AKTIF);
		if(handler!=null){
			Message message = new Message();
			Bundle bundle = new Bundle();
			bundle.putBoolean("aktif", true);
			bundle.putString("idAnak", cvs[1]);
			message.setData(bundle);
			message.what = Status.SUCCESS;
			handler.sendMessage(message);
		}
	}
	
	private BinderHandlerMonak binderHandlerMonak;
	
	private final Context context;
	
	private String[] 	cvs;

	
}
