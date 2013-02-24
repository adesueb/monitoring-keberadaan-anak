package org.ade.monak.ortu.service.gate.monak;

import org.ade.monak.ortu.Variable.TipePesanMonak;
import org.ade.monak.ortu.entity.Anak;
import org.ade.monak.ortu.service.gate.ASenderMonak;
import org.ade.monak.ortu.util.IDGenerator;

import android.content.Context;

public class SenderTrackingMode extends ASenderMonak{

	public SenderTrackingMode(Context context){
		super(context);
	}
	
	public void sendStartTracking(Anak anak){

		IDGenerator idGenerator = new IDGenerator(getContext(), null);
		String pesan = TipePesanMonak.REQUEST_START_TRACKING+","+idGenerator.getIdOrangTua();
		kirimPesan(anak, pesan);
	}
	
	public void sendStopTracking(Anak anak){

		IDGenerator idGenerator = new IDGenerator(getContext(), null);
		String pesan = TipePesanMonak.REQUEST_STOP_TRACKING+","+idGenerator.getIdOrangTua();
		kirimPesan(anak, pesan);
	}
	
	@Override
	public void onSuccess(int tipeKoneksi) {
	}

	@Override
	public void onFailed(int tipeKoneksi) {
	}


}
