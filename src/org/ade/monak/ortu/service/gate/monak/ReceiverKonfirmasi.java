package org.ade.monak.ortu.service.gate.monak;

import org.ade.monak.ortu.Variable.Status;
import org.ade.monak.ortu.entity.Anak;
import org.ade.monak.ortu.service.BinderHandlerMonak;
import org.ade.monak.ortu.service.MonakService;
import org.ade.monak.ortu.service.storage.DatabaseManagerOrtu;

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
		Anak anak = new Anak();
		anak.setIdAnak(cvs[1]);
		anak.setAktif(true);
		DatabaseManagerOrtu databaseManager = new DatabaseManagerOrtu(context);
		databaseManager.setAktifAnak(anak);
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
	
	public void receiveKonfirmasiDeAktifMonitoring(String [] cvs){
		Anak anak = new Anak();
		anak.setIdAnak(cvs[1]);
		anak.setAktif(false);
		DatabaseManagerOrtu databaseManager = new DatabaseManagerOrtu(context);
		databaseManager.setAktifAnak(anak);
		Handler handler = binderHandlerMonak.getSingleBindUIHandler(MonakService.WAITING_KONFIRMASI_AKTIF);
		if(handler!=null){
			Message message = new Message();
			Bundle bundle = new Bundle();
			bundle.putBoolean("aktif", false);
			bundle.putString("idAnak", cvs[1]);
			message.setData(bundle);
			message.what = Status.SUCCESS;
			handler.sendMessage(message);

		}
	}
	
	public void receiverKonfirmasiHapusAnak(String [] cvs){
		Anak anak = new Anak();
		anak.setIdAnak(cvs[1]);
		anak.setAktif(false);
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
	
	private final Context context;
	private BinderHandlerMonak binderHandlerMonak;
}
