package org.ade.monak.ortu.service.gate.monak;

import org.ade.monak.ortu.Variable.TipePesanMonak;
import org.ade.monak.ortu.entity.DataMonitoring;
import org.ade.monak.ortu.service.gate.ASenderMonak;
import org.ade.monak.ortu.util.IDGenerator;

import android.content.Context;

public class SenderStopMonitoring extends ASenderMonak{

	public SenderStopMonitoring(Context context){
		super(context);

	}
	
	public void sendStopMonitoring(DataMonitoring dataMonitoring){
		IDGenerator idGenerator = new IDGenerator(getContext(), null);
		String pesan = TipePesanMonak.STOP_MONITORING+","+dataMonitoring.getIdMonitoring()+","+idGenerator.getIdOrangTua();
		kirimPesan(dataMonitoring.getAnak(),pesan);
	}

	@Override
	public void onSuccess(int tipeKoneksi) {
	}

	@Override
	public void onFailed(int tipeKoneksi) {
	}
	
	
}
