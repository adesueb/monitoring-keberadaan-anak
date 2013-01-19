package org.ade.monitoring.keberadaan.service.gate.monak;

import java.util.List;

import org.ade.monitoring.keberadaan.Variable.TipePesanMonak;
import org.ade.monitoring.keberadaan.entity.Lokasi;
import org.ade.monitoring.keberadaan.service.gate.ASenderMonak;
import org.ade.monitoring.keberadaan.service.storage.DatabaseManager;
import org.ade.monitoring.keberadaan.service.storage.PreferenceMonitoringManager;
import org.ade.monitoring.keberadaan.util.LokasisConverter;

import android.content.Context;

public class SenderLogMonak extends ASenderMonak{

	public SenderLogMonak(Context context){
		super(context);
		pref = new PreferenceMonitoringManager(context);
	}
	
	public void sendLogMonak(){
		getSenderSMS().sendSMS(pref.getNoHpOrtu(), TipePesanMonak.RETRIEVE_LOG_LOCATION+","+pref.getIdAnak()+","+LokasisConverter.convertLokasisToText(getAllLogMonak()));
	}
	
	private List<Lokasi> getAllLogMonak(){
		DatabaseManager db = new DatabaseManager(getContext());
		List<Lokasi> lokasis = db.getAllLokasi();
		return lokasis;
	}
	
	private void deleteAllLocation(){
		DatabaseManager db = new DatabaseManager(getContext());
		db.deleteAllLokasi();
	}
	
	@Override
	public void success(int tipeKoneksi) {
		deleteAllLocation();
	}

	@Override
	public void failed(int tipeKoneksi) {
		// TODO Auto-generated method stub
		
	}
	
	private final PreferenceMonitoringManager pref;

}
