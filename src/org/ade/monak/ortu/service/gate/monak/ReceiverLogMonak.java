package org.ade.monak.ortu.service.gate.monak;

import java.util.ArrayList;
import java.util.List;

import org.ade.monak.ortu.Variable.Status;
import org.ade.monak.ortu.entity.Anak;
import org.ade.monak.ortu.entity.Lokasi;
import org.ade.monak.ortu.service.BinderHandlerMonak;
import org.ade.monak.ortu.service.MonakService;
import org.ade.monak.ortu.service.storage.DatabaseManagerOrtu;
import org.ade.monak.ortu.util.IDGenerator;
import org.ade.monak.ortu.util.KontrolLog;
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
			this.textLogs += cvs[i]+",";
		}
		this.idAnak = idAnak;
		
		action();
	}
	
	private void action(){
		// TODO : test me!!!!
		DatabaseManagerOrtu databaseManager = new DatabaseManagerOrtu(context);
		IDGenerator idGenerator = new IDGenerator(context, databaseManager);
		
		KontrolLog.kontrolDeleteLogLokasi(databaseManager);
		
		List<Lokasi> lokasis = LokasisConverter.covertTextToLokasis(textLogs);
		if(lokasis.size()<=0)return;
		
		Anak anak = new Anak();
		anak.setIdAnak(idAnak);
		
		List<Lokasi> lokasisBaru = new ArrayList<Lokasi>();
		
		List<Lokasi>lokasisLama = databaseManager.getAllLokasiAnak(anak);
		for(Lokasi lokasi:lokasis){
			boolean sama = false;
			for(Lokasi lokasiLama:lokasisLama){
				if(lokasiLama.getlatitude()==lokasi.getlatitude()&&lokasiLama.getLongitude()==lokasi.getLongitude()){
					sama = true;
					break;
				}
			}
			if(!sama){
				lokasisBaru.add(lokasi);
			}
			
		}
		
		for(Lokasi lokasi:lokasisBaru){
			
			lokasi.setId(idGenerator.getIdLocation());
			lokasi.setAnak(anak);
			lokasi.setLog(true);
			
			databaseManager.addLokasi(lokasi);
			
		}
		if(lokasisBaru.size()>0){
			anak.setLastLokasi(lokasisBaru.get(lokasis.size()-1));		
			databaseManager.updateLastLokasiAnak(anak);
		}
		anak.setAktif(true);
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
