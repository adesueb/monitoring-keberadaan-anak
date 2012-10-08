package org.ade.monitoring.keberadaan.koneksi;

import org.ade.monitoring.keberadaan.Variable.TipePesanData;
import org.ade.monitoring.keberadaan.entity.DataMonitoring;
import org.ade.monitoring.keberadaan.entity.PesanData;
import org.ade.monitoring.keberadaan.storage.DatabaseManager;

import android.content.Context;

public class SenderMonitoringOrtu {
	
	public SenderMonitoringOrtu(Context context){
		databaseManager	= new DatabaseManager(context);
		senderSMS		= new SenderSMS(context);
		senderInternet	= new SenderInternet(context);
	}
	
	public void sendDataMonitoringBaru(DataMonitoring dataMonitoring){
		PesanData pesanData = createPesanData(dataMonitoring, TipePesanData.DATAMONITORING_BARU);
		senderSMS.kirimPesanData(pesanData);
		senderInternet.kirimPesanData(pesanData);
	}
	
	public void sendPeringatanTerlarang(DataMonitoring dataMonitoring){
		PesanData pesanData = createPesanData(dataMonitoring, TipePesanData.PERINGATAN_TERLARANG);
		senderSMS.kirimPesanData(pesanData);
		senderInternet.kirimPesanData(pesanData);
	}
	
	public void sendPeringatanSeharusnya(DataMonitoring dataMonitoring){
		PesanData pesanData = createPesanData(dataMonitoring, TipePesanData.PERINGATAN_SEHARUSNYA);
		senderSMS.kirimPesanData(pesanData);
		senderInternet.kirimPesanData(pesanData);
	}
	
	private PesanData createPesanData(DataMonitoring dataMonitoring, int tipe){
		PesanData pesanData = new PesanData();
		pesanData.setDataMonitoring(dataMonitoring);
		pesanData.setTipe(tipe);
		return pesanData;
	}
	
	
	private final DatabaseManager	databaseManager;
	private final SenderSMS			senderSMS;
	private final SenderInternet	senderInternet;
	
}
