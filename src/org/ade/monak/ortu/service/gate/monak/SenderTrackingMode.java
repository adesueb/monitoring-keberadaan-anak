package org.ade.monak.ortu.service.gate.monak;

import org.ade.monak.ortu.Variable.TipePesanMonak;
import org.ade.monak.ortu.entity.Anak;
import org.ade.monak.ortu.service.gate.ASenderMonak;

import android.content.Context;

public class SenderTrackingMode extends ASenderMonak{

	public SenderTrackingMode(Context context){
		super(context);
	}
	
	public void sendStartTracking(Anak anak){
		getSenderSMS().sendSMS(anak.getNoHpAnak(), TipePesanMonak.REQUEST_START_TRACKING+"");
	}
	
	public void sendStopTracking(Anak anak){
		getSenderSMS().sendSMS(anak.getNoHpAnak(), TipePesanMonak.REQUEST_STOP_TRACKING+"");
	}
	
	@Override
	public void success(int tipeKoneksi) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void failed(int tipeKoneksi) {
		// TODO : sending throught internet.....
		
	}


}