package org.ade.monitoring.keberadaan.service.gate.monak;

import java.util.List;

import org.ade.monitoring.keberadaan.Variable.TipePesanMonak;
import org.ade.monitoring.keberadaan.entity.Lokasi;
import org.ade.monitoring.keberadaan.service.storage.DatabaseManager;
import org.ade.monitoring.keberadaan.service.storage.PreferenceMonitoringManager;

import android.content.Context;

public class SenderLogMonak extends ASenderMonak{

	public SenderLogMonak(Context context){
		super(context);
		pref = new PreferenceMonitoringManager(context);
	}
	
	public void sendLogMonak(){
		getSenderSMS().sendSMS(pref.getNoHpOrtu(), TipePesanMonak.RETRIEVE_LOG_LOCATION+","+pref.getIdAnak()+","+convertAllLogMonakToText(getAllLogMonak()));
	}
	
	private List<Lokasi> getAllLogMonak(){
		DatabaseManager db = new DatabaseManager(getContext());
		List<Lokasi> lokasis = db.getAllLokasi();
		return lokasis;
	}
	
	private String convertAllLogMonakToText(List<Lokasi> logs){
		String text = "";
		for(Lokasi log:logs){
			text = text + log.toString()+",:";
		}
		return text;
	}
	
	@Override
	public void success(int tipeKoneksi) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void failed(int tipeKoneksi) {
		// TODO Auto-generated method stub
		
	}
	
	private final PreferenceMonitoringManager pref;

}
