package org.ade.monitoring.keberadaan.service.gate.monak;

import org.ade.monitoring.keberadaan.Variable.Status;
import org.ade.monitoring.keberadaan.Variable.TipePesanMonak;
import org.ade.monitoring.keberadaan.boundary.DaftarAnak;
import org.ade.monitoring.keberadaan.entity.Anak;
import org.ade.monitoring.keberadaan.entity.DataMonitoring;
import org.ade.monitoring.keberadaan.entity.IPesanData;
import org.ade.monitoring.keberadaan.entity.Lokasi;
import org.ade.monitoring.keberadaan.entity.Peringatan;
import org.ade.monitoring.keberadaan.service.MonakService;
import org.ade.monitoring.keberadaan.service.Notifikasi;
import org.ade.monitoring.keberadaan.service.gate.SenderSMS;
import org.ade.monitoring.keberadaan.service.storage.DatabaseManager;
import org.ade.monitoring.keberadaan.util.StorageHandler;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class ReceiverPesanData {
	
	
	public void menerimaPesanData(Context context, IPesanData pesanData){
		if(pesanData!=null){
			if(pesanData.getTipe()==TipePesanMonak.DATAMONITORING_BARU){
				new DatabaseManager(context).addDataMonitoring((DataMonitoring) pesanData);
			}else{
				new Notifikasi(context).tampilkanNotifikasiPeringatan((Peringatan) pesanData);
				
			}
		}
	}
}
