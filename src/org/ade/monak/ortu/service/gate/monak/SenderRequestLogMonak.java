package org.ade.monak.ortu.service.gate.monak;

import org.ade.monak.ortu.Variable.TipePesanMonak;
import org.ade.monak.ortu.entity.Anak;
import org.ade.monak.ortu.service.gate.ASenderMonak;
import org.ade.monak.ortu.util.IDGenerator;

import android.content.Context;

public class SenderRequestLogMonak extends ASenderMonak{

	public SenderRequestLogMonak(Context context){
		super(context);
	}
	
	public void sendRequest(Anak anak){

		IDGenerator idGenerator = new IDGenerator(getContext(), null);
		getSenderSMS().sendSMS(anak.getNoHpAnak(), TipePesanMonak.REQUEST_LOG_LOCATION+","+idGenerator.getIdOrangTua());
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
