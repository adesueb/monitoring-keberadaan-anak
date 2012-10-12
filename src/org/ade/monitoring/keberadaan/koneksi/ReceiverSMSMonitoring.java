package org.ade.monitoring.keberadaan.koneksi;

import org.ade.monitoring.keberadaan.Variable.TipePesanData;
import org.ade.monitoring.keberadaan.entity.DataMonitoring;
import org.ade.monitoring.keberadaan.entity.Peringatan;
import org.ade.monitoring.keberadaan.entity.IPesanData;
import org.ade.monitoring.keberadaan.service.Notifikasi;
import org.ade.monitoring.keberadaan.storage.DatabaseManager;

import android.content.Context;

public class ReceiverSMSMonitoring extends ReceiverSMS{

	@Override
	public void menerimaPesanData(Context context, IPesanData pesanData) {
		if(pesanData.getTipe()==TipePesanData.DATAMONITORING_BARU){
			new DatabaseManager(context).addDataMonitoring((DataMonitoring) pesanData);
		}else{
			new Notifikasi(context).tampilkanNotifikasiPeringatan((Peringatan) pesanData);
			
		}
	}

	

}
