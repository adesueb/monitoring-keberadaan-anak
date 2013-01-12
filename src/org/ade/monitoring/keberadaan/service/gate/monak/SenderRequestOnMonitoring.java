package org.ade.monitoring.keberadaan.service.gate.monak;

import org.ade.monitoring.keberadaan.Variable.TipePesanMonak;
import org.ade.monitoring.keberadaan.entity.Anak;
import org.ade.monitoring.keberadaan.service.gate.SenderSMS;

import android.content.Context;

public class SenderRequestOnMonitoring {

	public SenderRequestOnMonitoring(Context context){
		this.senderSms = new SenderSMS(context, null);
	}
	
	public void sendRequestLocation(Anak anak){
		this.senderSms.sendSMS(anak.getNoHpAnak(), TipePesanMonak.REQUEST_ON_MONITORING+","+anak.getIdAnak());
	}
	
	private final SenderSMS senderSms;
	
}
