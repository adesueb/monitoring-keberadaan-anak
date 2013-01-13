package org.ade.monitoring.keberadaan.service.gate.monak;

import org.ade.monitoring.keberadaan.service.storage.PreferenceMonitoringManager;

import android.content.Context;

public class ReceiverPendaftaranAnak {

	public ReceiverPendaftaranAnak(Context context){
		senderLokasi = new SenderLokasi(context);
		pref = new PreferenceMonitoringManager(context);
	}
	
	public void receivePendaftaranAnak(String noHp, String idAnak){
		pref.setNoHpOrtu(noHp);
		pref.setIdAnak(idAnak);
		senderLokasi.sendLocationSingleRequest(idAnak);
	}
	
	private final SenderLokasi senderLokasi;
	private final PreferenceMonitoringManager pref;
}