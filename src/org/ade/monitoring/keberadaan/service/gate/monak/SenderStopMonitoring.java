package org.ade.monitoring.keberadaan.service.gate.monak;

import org.ade.monitoring.keberadaan.Variable.TipePesanMonak;
import org.ade.monitoring.keberadaan.entity.Anak;
import org.ade.monitoring.keberadaan.service.gate.ASenderMonak;

import android.content.Context;

public class SenderStopMonitoring extends ASenderMonak{

	public SenderStopMonitoring(Context context){
		super(context);

	}
	
	public void sendStopMonitoring(Anak anak){
		getSenderSMS().sendSMS(anak.getNoHpAnak(), TipePesanMonak.STOP_MONITORING+","+anak.getIdAnak());
	}
	
	@Override
	public void success(int tipeKoneksi) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void failed(int tipeKoneksi) {
		// TODO Auto-generated method stub
		
	}
}
