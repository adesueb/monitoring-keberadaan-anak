package org.ade.monitoring.keberadaan.service.gate.monak;

import org.ade.monitoring.keberadaan.Variable.TipePesanMonak;
import org.ade.monitoring.keberadaan.entity.Anak;
import org.ade.monitoring.keberadaan.service.gate.ASenderMonak;
import org.ade.monitoring.keberadaan.service.gate.SenderSMS;

import android.content.Context;

public class SenderRequestOnMonitoring extends ASenderMonak{

	public SenderRequestOnMonitoring(Context context){
		super(context);
		this.senderSms = getSenderSMS();

	}
	
	public void sendRequestLocation(Anak anak){
		this.senderSms.sendSMS(anak.getNoHpAnak(), TipePesanMonak.REQUEST_ON_MONITORING+","+anak.getIdAnak());
	}
	
	@Override
	public void success(int tipeKoneksi) {
		
	}

	@Override
	public void failed(int tipeKoneksi) {
		// TODO : send internet
		
	}
	
	private final SenderSMS senderSms;

	
	
}
