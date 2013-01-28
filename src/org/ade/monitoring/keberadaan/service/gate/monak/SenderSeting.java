package org.ade.monitoring.keberadaan.service.gate.monak;

import org.ade.monitoring.keberadaan.Variable.TipePesanMonak;
import org.ade.monitoring.keberadaan.entity.Anak;
import org.ade.monitoring.keberadaan.service.gate.ASenderMonak;

import android.content.Context;

public class SenderSeting extends ASenderMonak{

	public SenderSeting(Context context){
		super(context);
	}
	
	public void sendSetMiliseconds(Anak anak, int miliseconds){
		getSenderSMS().sendSMS(anak.getNoHpAnak(), TipePesanMonak.SET_TRACK_MILISECONDS+","+miliseconds);
	}
	
	public void sendSetMeters(Anak anak, int meters){
		getSenderSMS().sendSMS(anak.getNoHpAnak(), TipePesanMonak.SET_TRACK_METERS+","+meters);
	}
	
	public void sendMaxLogs(Anak anak, int maxLogs){
		getSenderSMS().sendSMS(anak.getNoHpAnak(), TipePesanMonak.SET_MAX_LOGS_LOCATION+","+maxLogs);
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
