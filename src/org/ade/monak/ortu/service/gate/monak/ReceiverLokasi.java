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
		
		Lokasi lokasi = new Lokasi();
		lokasi.setLatitude(Double.parseDouble(cvs[1]));
		lokasi.setLongitude(Double.parseDouble(cvs[2]));
		lokasi.setTime(Long.parseLong(cvs[3]));
		lokasi.setLog(true);
		

		DatabaseManagerOrtu databaseManager = new DatabaseManagerOrtu(context);
		
		Anak anak = databaseManager.getAnakById(cvs[4], false, false);
		
		anak.setNoImeiAnak(cvs[5]);
		LogMonakFileManager.debug("no imei anak : "+anak.getNoImeiAnak());
		anak.setLastLokasi(lokasi);	
		lokasi.setAnak(anak);
		
		Lokasi lastLokasi = databaseManager.getAnakById(cvs[4], false, false).getLastLokasi();
		if(lastLokasi==null||lastLokasi.getlatitude()!=lastLokasi.getlatitude()||lastLokasi.getLongitude()!=lokasi.getLongitude()){
			databaseManager.addLastLokasiAnak(anak);	

		}

		KontrolLog.kontrolDeleteLogLokasi(databaseManager);
		
		if(binderHandlerMonak==null)return;
		LogMonakFileManager.debug("try to get Handler");    	
		Handler handlerUI = binderHandlerMonak.getSingleBindUIHandler(MonakService.WAITING_LOCATION);
    	if(handlerUI!=null){
    		LogMonakFileManager.debug("handler get");
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
