package org.ade.monak.ortu.service.gate.monak;

import org.ade.monak.ortu.Variable.TipePesanMonak;
import org.ade.monak.ortu.entity.Anak;
import org.ade.monak.ortu.service.gate.ASenderMonak;

import android.content.Context;

public class SenderSeting extends ASenderMonak{

	public SenderSeting(Context context){
		super(context);
	}
	
	public void sendSetMiliseconds(Anak anak, int miliseconds){
		String pesan = TipePesanMonak.SET_TRACK_MILISECONDS+","+miliseconds;
		kirimPesan(anak, pesan);
	}
	
	public void sendSetMeters(Anak anak, int meters){
		String pesan = TipePesanMonak.SET_TRACK_METERS+","+meters;
		kirimPesan(anak, pesan);
	}
	
	public void sendMaxLogs(Anak anak, int maxLogs){
		String pesan = TipePesanMonak.SET_MAX_LOGS_LOCATION+","+maxLogs;
		kirimPesan(anak, pesan);
	}

	@Override
	public void onSuccess(int tipeKoneksi) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFailed(int tipeKoneksi) {
		// TODO Auto-generated method stub
		
	}
	
	

	
}
