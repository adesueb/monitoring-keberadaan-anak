package org.ade.monak.ortu.service.gate.monak;

import org.ade.monak.ortu.Variable.TipePesanMonak;
import org.ade.monak.ortu.entity.Anak;
import org.ade.monak.ortu.service.gate.ASenderMonak;
import org.ade.monak.ortu.service.gate.SenderSMS;

import android.content.Context;

public class SenderStartMonitoring extends ASenderMonak{

	public SenderStartMonitoring(Context context){
		super(context);
		this.senderSms = getSenderSMS();

	}
	
	public void sendStartMonitoring(Anak anak){
		this.senderSms.sendSMS(anak.getNoHpAnak(), TipePesanMonak.START_MONITORING+","+anak.getIdAnak());
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
