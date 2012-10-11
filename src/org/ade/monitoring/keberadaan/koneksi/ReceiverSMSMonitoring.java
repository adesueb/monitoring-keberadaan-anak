package org.ade.monitoring.keberadaan.koneksi;

import org.ade.monitoring.keberadaan.Variable.TipePesanData;
import org.ade.monitoring.keberadaan.entity.DataMonitoring;
import org.ade.monitoring.keberadaan.entity.Peringatan;
import org.ade.monitoring.keberadaan.entity.PesanData;
import org.ade.monitoring.keberadaan.service.Notifikasi;
import org.ade.monitoring.keberadaan.storage.DatabaseManager;

import android.content.Context;

public class ReceiverSMSMonitoring extends ReceiverSMS{

	@Override
	public void menerimaPesanData(Context context, PesanData pesanData) {
		if(pesanData.getTipe()==TipePesanData.DATAMONITORING_BARU){
			DataMonitoring dataMonitoring = pesanData.getDataMonitoring();
			new DatabaseManager(context).addDataMonitoring(dataMonitoring);
		}else{
			Peringatan peringatan = pesanData.getPeringatan();
			new Notifikasi(context).tampilkanNotifikasiPeringatan(peringatan);
			
		}
	}

	

}
