package org.ade.monak.ortu.service.gate.monak;

import org.ade.monak.ortu.Variable.Status;
import org.ade.monak.ortu.entity.Anak;
import org.ade.monak.ortu.entity.DataMonitoring;
import org.ade.monak.ortu.service.BinderHandlerMonak;
import org.ade.monak.ortu.service.MonakService;
import org.ade.monak.ortu.service.storage.DatabaseManagerOrtu;
import org.ade.monak.ortu.service.storage.LogMonakFileManager;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class ReceiverKonfirmasi {
	public ReceiverKonfirmasi(Context context, BinderHandlerMonak binder) {
		this.context 			= context;
		this.binderHandlerMonak = binder;
	}	
	
	public void receiveKonfirmasiAktifMonitoring(String [] cvs){
		DataMonitoring dataMonitoring = new DataMonitoring();
		dataMonitoring.setIdMonitoring(cvs[1]);
		dataMonitoring.setAktif(true);
		DatabaseManagerOrtu databaseManager = new DatabaseManagerOrtu(context);
		databaseManager.setAktifMonitoring(dataMonitoring);
		Handler handler = binderHandlerMonak.getSingleBindUIHandler(MonakService.WAITING_KONFIRMASI_AKTIF);
		if(handler!=null){
			LogMonakFileManager.debug("handler ada isinya");
			Message message = new Message();
			Bundle bundle = new Bundle();
			bundle.putBoolean("aktif", true);
			bundle.putString("idMonitoring", cvs[1]);
			message.setData(bundle);
			message.what = Status.SUCCESS;
			handler.sendMessage(message);
		}else{
			LogMonakFileManager.debug("handler kosong");
		}
	}
	
	public void receiveKonfirmasiDeAktifMonitoring(String [] cvs){
		DataMonitoring dataMonitoring = new DataMonitoring();
		dataMonitoring.setIdMonitoring(cvs[1]);
		dataMonitoring.setAktif(false);
		DatabaseManagerOrtu databaseManager = new DatabaseManagerOrtu(context);
		databaseManager.setAktifMonitoring(dataMonitoring);
		Handler handler = binderHandlerMonak.getSingleBindUIHandler(MonakService.WAITING_KONFIRMASI_AKTIF);
		if(handler!=null){
			LogMonakFileManager.debug("handler ada isinya");	
			Message message = new Message();
			Bundle bundle = new Bundle();
			bundle.putBoolean("aktif", false);
			bundle.putString("idMonitoring", cvs[1]);
			message.setData(bundle);
			message.what = Status.SUCCESS;
			handler.sendMessage(message);

		}else{
			LogMonakFileManager.debug("handler kosong");
		}
	}
	
	public void receiveKonfirmasiDeAktifAnak(String [] cvs){

		DatabaseManagerOrtu databaseManager = new DatabaseManagerOrtu(context);
		Anak anak = databaseManager.getAnakById(cvs[1], false, true);
		for(DataMonitoring dataMonitoring: anak.getDataMonitorings()){
			dataMonitoring.setIdMonitoring(cvs[1]);
			dataMonitoring.setAktif(false);
			databaseManager.setAktifMonitoring(dataMonitoring);
		}
		Handler handler = binderHandlerMonak.getSingleBindUIHandler(MonakService.WAITING_KONFIRMASI_AKTIF);
		if(handler!=null){
			Message message = new Message();
			Bundle bundle = new Bundle();
			bundle.putBoolean("aktif", false);
			bundle.putString("idMonitoring", cvs[1]);
			message.setData(bundle);
			message.what = Status.SUCCESS;
			handler.sendMessage(message);

		}
	}
	
	public void receiverKonfirmasiHapusAnak(String [] cvs){
		Anak anak = new Anak();
		anak.setIdAnak(cvs[1]);
		DatabaseManagerOrtu databaseManager = new DatabaseManagerOrtu(context);
		databaseManager.deleteAnak(anak);
		Handler handler = binderHandlerMonak.getSingleBindUIHandler(MonakService.WAITING_KONFIRMASI_HAPUS_ANAK);
		if(handler!=null){
			Message message = new Message();
			Bundle bundle = new Bundle();
			bundle.putString("idAnak", cvs[1]);
			message.setData(bundle);
			message.what = Status.SUCCESS;
			handler.sendMessage(message);

		}
	}
	
	public void receiveKonfirmasiStartTrack(String [] cvs){
		Anak anak = new Anak();
		anak.setIdAnak(cvs[1]);
		anak.setTrack(true);
		DatabaseManagerOrtu databaseManager = new DatabaseManagerOrtu(context);
		databaseManager.setTrackAnak(anak);
		Handler handler = binderHandlerMonak.getSingleBindUIHandler(MonakService.WAITING_KONFIRMASI_TRACK);
		if(handler!=null){
			Message message = new Message();
			Bundle bundle = new Bundle();
			bundle.putBoolean("track", true);
			bundle.putString("idAnak", cvs[1]);
			message.setData(bundle);
			message.what = Status.SUCCESS;
			handler.sendMessage(message);
		}
	}
	
	public void receiveKonfirmasiStopTrack(String [] cvs){
		Anak anak = new Anak();
		anak.setIdAnak(cvs[1]);
		anak.setTrack(false);
		DatabaseManagerOrtu databaseManager = new DatabaseManagerOrtu(context);
		databaseManager.setTrackAnak(anak);
		Handler handler = binderHandlerMonak.getSingleBindUIHandler(MonakService.WAITING_KONFIRMASI_TRACK);
		if(handler!=null){
			Message message = new Message();
			Bundle bundle = new Bundle();
			bundle.putBoolean("track", false);
			bundle.putString("idAnak", cvs[1]);
			message.setData(bundle);
			message.what = Status.SUCCESS;
			handler.sendMessage(message);

		}
	}
	
	
	private final Context context;
	private BinderHandlerMonak binderHandlerMonak;
}
