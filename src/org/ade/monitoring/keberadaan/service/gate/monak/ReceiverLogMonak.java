package org.ade.monitoring.keberadaan.service.gate.monak;

import java.util.List;

import org.ade.monitoring.keberadaan.Variable.Status;
import org.ade.monitoring.keberadaan.entity.Anak;
import org.ade.monitoring.keberadaan.entity.LogMonak;
import org.ade.monitoring.keberadaan.entity.Lokasi;
import org.ade.monitoring.keberadaan.service.BinderHandlerMonak;
import org.ade.monitoring.keberadaan.service.MonakService;
import org.ade.monitoring.keberadaan.service.storage.DatabaseManager;
import org.ade.monitoring.keberadaan.service.util.IBindMonakServiceConnection;
import org.ade.monitoring.keberadaan.service.util.ServiceMonakConnection;
import org.ade.monitoring.keberadaan.util.IDGenerator;
import org.ade.monitoring.keberadaan.util.LokasisConverter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class ReceiverLogMonak implements IBindMonakServiceConnection{

	public ReceiverLogMonak(Context context){
		this.context = context;
	}
	public void receiveLogMonak(String idAnak, String[] cvs){

		this.textLogs = "";
		for(int i=2;i<cvs.length;i++){
			this.textLogs += cvs[i];
		}
		this.idAnak = idAnak;

		Intent intent = new Intent("monak_service");
		serviceConnection = new ServiceMonakConnection(this);
		context.bindService(intent, serviceConnection, 0);

	}
	
	private void action(){
		if(bound){
			// TODO : test me!!!!
			DatabaseManager databaseManager = new DatabaseManager(context);
			IDGenerator idGenerator = new IDGenerator(context, databaseManager);
			List<Lokasi> lokasis = LokasisConverter.covertTextToLokasis(textLogs);
			Anak anak = new Anak();
			anak.setIdAnak(idAnak);
			for(Lokasi lokasi:lokasis){
				
				lokasi.setId(idGenerator.getIdLocation());
				
				LogMonak log = new LogMonak();
				log.setLokasi(lokasi);
				log.setAnak(anak);
				
				databaseManager.addLocationLog(log);
				
			}
			Handler handler = binderHandlerMonak.getSingleBindUIHandler(MonakService.WAITING_LOG_LOCATION);
			if(handler!=null){
				Message message = new Message();
				Bundle bundle = new Bundle();
				bundle.putString("textLog", textLogs);
				message.setData(bundle);
				message.what = Status.SUCCESS;
				handler.sendMessage(message);
				
				binderHandlerMonak.unBindUIHandlerWaitingLogLocation();	
			}
			context.unbindService(serviceConnection);
		}
	}
	
	public void setBinderHandlerMonak(BinderHandlerMonak binderHandlerMonak) {
		this.binderHandlerMonak = binderHandlerMonak;
		action();
	}
	
	public void setBound(boolean bound) {
		this.bound = bound;
	}
	
	private ServiceMonakConnection serviceConnection;
	private BinderHandlerMonak binderHandlerMonak;
	private boolean bound;
	private final Context context;
	private String idAnak;
	private String textLogs;
}
