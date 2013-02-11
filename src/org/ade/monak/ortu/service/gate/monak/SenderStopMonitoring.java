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
		getSenderSMS().sendSMS(dataMonitoring.getAnak().getNoHpAnak(), TipePesanMonak.STOP_MONITORING+","+dataMonitoring.getIdMonitoring()+","+idGenerator.getIdOrangTua());
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
