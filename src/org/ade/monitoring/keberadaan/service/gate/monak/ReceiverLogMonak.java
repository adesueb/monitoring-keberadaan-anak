package org.ade.monitoring.keberadaan.service.gate.monak;

import org.ade.monitoring.keberadaan.service.BinderHandlerMonak;
import org.ade.monitoring.keberadaan.service.IBindMonakServiceConnection;
import org.ade.monitoring.keberadaan.service.ServiceMonakConnection;

import android.content.Context;
import android.content.Intent;

public class ReceiverLogMonak implements IBindMonakServiceConnection{

	public ReceiverLogMonak(Context context){
		this.context = context;
	}
	public void receiveLogMonak(String idAnak, String textLogs){
		this.idAnak = idAnak;
		this.textLogs = textLogs;

		Intent intent = new Intent("monak_service");
		ServiceMonakConnection serviceConnection = new ServiceMonakConnection(this);
		context.bindService(intent, serviceConnection, 0);

	}
	
	private void action(){
		if(bound){
			// TODO : what will u do... when u receive text LOGs....??
		}
	}
	
	public void setBinderHandlerMonak(BinderHandlerMonak binderHandlerMonak) {
		this.binderHandlerMonak = binderHandlerMonak;
		action();
	}
	
	public void setBound(boolean bound) {
		this.bound = bound;
	}
	
	private BinderHandlerMonak binderHandlerMonak;
	private boolean bound;
	private final Context context;
	private String idAnak;
	private String textLogs;
}
