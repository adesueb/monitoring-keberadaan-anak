package org.ade.monitoring.keberadaan.service.gate.monak;

import android.content.Context;

public class ReceiverRequestLogMonak {

	public ReceiverRequestLogMonak(Context context){
		sender = new SenderLogMonak(context);
	}
	
	public void receiveRequestLogLocation(){
		sender.sendLogMonak();
	}
	
	private final SenderLogMonak sender;
}
