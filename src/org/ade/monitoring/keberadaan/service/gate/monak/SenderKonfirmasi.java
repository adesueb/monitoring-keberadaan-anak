package org.ade.monitoring.keberadaan.service.gate.monak;

import org.ade.monitoring.keberadaan.Variable.TipePesanMonak;
import org.ade.monitoring.keberadaan.service.gate.ASenderMonak;
import org.ade.monitoring.keberadaan.service.storage.PreferenceMonitoringManager;

import android.content.Context;

public class SenderKonfirmasi extends ASenderMonak{

	public SenderKonfirmasi(Context context){
		super(context);
		pref = new PreferenceMonitoringManager(context);
	}
	
	public void sendKonfirmasiDeAktifMonitoring(){
		String cvs = TipePesanMonak.KONFIRMASI_DEAKTIF_MONITORING+","+pref.getIdAnak();
		getSenderSMS().sendSMS(pref.getNoHpOrtu(), cvs);
	}
	
	public void sendKonformasiAktifMonitoring(){
		String cvs = TipePesanMonak.KONFIRMASI_AKTIF_MONITORING+","+pref.getIdAnak();
		getSenderSMS().sendSMS(pref.getNoHpOrtu(), cvs);
	}
	
	@Override
	public void success(int tipeKoneksi) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void failed(int tipeKoneksi) {
		// TODO Auto-generated method stub
		
	}
	
	private PreferenceMonitoringManager pref;
	

}
