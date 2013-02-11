package org.ade.monak.ortu.service.gate.monak;

import org.ade.monak.ortu.Variable.TipePesanMonak;
import org.ade.monak.ortu.entity.DataMonitoring;
import org.ade.monak.ortu.service.gate.ASenderMonak;
import org.ade.monak.ortu.service.gate.SenderSMS;
import org.ade.monak.ortu.util.IDGenerator;

import android.content.Context;

public class SenderStartMonitoring extends ASenderMonak{

	public SenderStartMonitoring(Context context){
		super(context);
		this.senderSms = getSenderSMS();

	}
	
	public void sendStartMonitoring(DataMonitoring dataMonitoring){
		IDGenerator idGenerator = new IDGenerator(getContext(), null);
		getSenderSMS().sendSMS(dataMonitoring.getAnak().getNoHpAnak(), TipePesanMonak.START_MONITORING+","+dataMonitoring.getIdMonitoring()+","+idGenerator.getIdOrangTua());
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
