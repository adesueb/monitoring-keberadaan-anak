package org.ade.monitoring.keberadaan.service.gate.monak;

import org.ade.monitoring.keberadaan.Variable.TipePesanMonak;
import org.ade.monitoring.keberadaan.entity.Anak;
import org.ade.monitoring.keberadaan.service.gate.ASenderMonak;

import android.content.Context;

public class SenderRequestLogMonak extends ASenderMonak{

	public SenderRequestLogMonak(Context context){
		super(context);
	}
	
	public void sendRequest(Anak anak){
		getSenderSMS().sendSMS(anak.getNoHpAnak(), TipePesanMonak.REQUEST_LOG_LOCATION+"");
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
