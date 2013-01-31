package org.ade.monak.ortu.service.gate.monak;

import java.util.List;

import org.ade.monak.ortu.Variable.Status;
import org.ade.monak.ortu.entity.Anak;
import org.ade.monak.ortu.entity.Lokasi;
import org.ade.monak.ortu.service.BinderHandlerMonak;
import org.ade.monak.ortu.service.MonakService;
import org.ade.monak.ortu.service.storage.DatabaseManager;
import org.ade.monak.ortu.util.IDGenerator;
import org.ade.monak.ortu.util.LokasisConverter;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class ReceiverLogMonak{

	public ReceiverLogMonak(Context context, BinderHandlerMonak binder){
		this.context 			= context;
		this.binderHandlerMonak = binder;
	}
	
	public void receiveLogMonak(String idAnak, String[] cvs){

		this.textLogs = "";
		for(int i=2;i<cvs.length;i++){
			this.textLogs += cvs[i];
		}
		this.idAnak = idAnak;
		
		action();
	}
	
	private void action(){
		// TODO : test me!!!!
		DatabaseManager databaseManager = new DatabaseManager(context);
		IDGenerator idGenerator = new IDGenerator(context, databaseManager);
		
		List<Lokasi> lokasis = LokasisConverter.covertTextToLokasis(textLogs);
		if(lokasis.size()<=0)return;
		
		Anak anak = new Anak();
		anak.setIdAnak(idAnak);
		
		for(Lokasi lokasi:lokasis){
			
			lokasi.setId(idGenerator.getIdLocation());
			lokasi.setAnak(anak);
			lokasi.setLog(true);
			
			databaseManager.addLokasi(lokasi);
			
		}
		
		anak.setLastLokasi(lokasis.get(lokasis.size()-1));
		anak.setAktif(true);
		databaseManager.updateLastLokasiAnak(anak);
		databaseManager.setAktifAnak(anak);
		
		Handler handler = binderHandlerMonak.getSingleBindUIHandler(MonakService.WAITING_LOG_LOCATION);
		if(handler!=null){
			Message message = new Message();
			Bundle bundle = new Bundle();
			bundle.putString("textLog", textLogs);
			bundle.putString("idAnak", idAnak);
			message.setData(bundle);
			message.what = Status.SUCCESS;
			handler.sendMessage(message);
			
		}
		
	}
	
	private BinderHandlerMonak binderHandlerMonak;
	private final Context context;
	private String idAnak;
	private String textLogs;
}
