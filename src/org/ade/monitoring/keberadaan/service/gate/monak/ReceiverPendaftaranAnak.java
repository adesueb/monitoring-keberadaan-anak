package org.ade.monitoring.keberadaan.service.gate.monak;

import org.ade.monitoring.keberadaan.service.storage.PreferenceMonitoringManager;

import android.content.Context;
import android.util.Log;

public class ReceiverPendaftaranAnak {

	public ReceiverPendaftaranAnak(Context context){
		senderLokasi = new SenderLokasi(context);
		pref = new PreferenceMonitoringManager(context);
	}
	
	public void receivePendaftaranAnak(String noHp, String idAnak){
		Log.d("ReceiverPendaftaranAnak", "id anak : "+idAnak+"noHp"+noHp);
		pref.setNoHpOrtu(noHp);
		pref.setIdAnak(idAnak);
		senderLokasi.sendLocationSingleRequest(idAnak);
	}
	
	private final SenderLokasi senderLokasi;
	private final PreferenceMonitoringManager pref;
}
