package org.ade.monak.ortu.service.gate.monak;

import org.ade.monak.ortu.Variable.TipePesanMonak;
import org.ade.monak.ortu.entity.Anak;
import org.ade.monak.ortu.service.gate.ASenderMonak;
import org.ade.monak.ortu.util.IDGenerator;

import android.content.Context;

public class SenderRequestLogMonak extends ASenderMonak{

	public SenderRequestLogMonak(Context context){
		super(context);
		this.context = context;
	}
	
	public void sendRequest(Anak anak){
		IDGenerator idGenerator = new IDGenerator(context, null);
		String pesan = TipePesanMonak.REQUEST_LOG_LOCATION+","+idGenerator.getIdOrangTua();
		kirimPesan(anak, pesan);
	}
	
	@Override
	public void onSuccess(int tipeKoneksi) {
		
	}

	@Override
	public void onFailed(int tipeKoneksi) {
		
	}
	
	private final Context context;




}
