package org.ade.monak.ortu.service.gate.monak;

import java.util.Calendar;

import org.ade.monak.ortu.Variable.TipePesanMonak;
import org.ade.monak.ortu.entity.Anak;
import org.ade.monak.ortu.entity.DataMonitoring;
import org.ade.monak.ortu.entity.IPesanData;
import org.ade.monak.ortu.entity.Lokasi;
import org.ade.monak.ortu.entity.Pelanggaran;
import org.ade.monak.ortu.entity.Peringatan;
import org.ade.monak.ortu.service.Notifikasi;
import org.ade.monak.ortu.service.storage.DatabaseManager;
import org.ade.monak.ortu.util.IDGenerator;

import android.content.Context;

public class ReceiverPesanData {
	
	
	public void menerimaPesanData(Context context, IPesanData pesanData){
		if(pesanData!=null){
			switch(pesanData.getTipe()){
				case TipePesanMonak.DATAMONITORING_BARU:{
					new DatabaseManager(context).addDataMonitoring((DataMonitoring) pesanData);
					break;
				}case TipePesanMonak.DATAMONITORING_UPDATE:{
					new DatabaseManager(context).updateDataMonitoring((DataMonitoring) pesanData);
					break;
				}case TipePesanMonak.DATAMONITORING_DELETE:{
					new DatabaseManager(context).deleteDataMonitoring((DataMonitoring) pesanData);
					break;
				}case TipePesanMonak.PERINGATAN_SEHARUSNYA:{
				}case TipePesanMonak.PERINGATAN_TERLARANG:{
					simpanPelanggaranDariPeringatan(context, (Peringatan)pesanData);
					new Notifikasi(context).tampilkanNotifikasiPeringatan((Peringatan) pesanData);
					break;
				}
			}
		} 
	}
	
	private void simpanPelanggaranDariPeringatan(Context context, Peringatan peringatan){
		
		DatabaseManager databaseManager = new DatabaseManager(context);
		IDGenerator idGenerator = new IDGenerator(context, databaseManager);
		
		Pelanggaran pelanggaran = new Pelanggaran();
		pelanggaran.setIdPelanggaran(idGenerator.getIdPelanggaran());

		DataMonitoring dataMonitoring = databaseManager.getDataMonitoringByIdMonitoring(peringatan.getIdMonitoring(), true, false);

		Anak anak = dataMonitoring.getAnak();
		anak.setAktif(false);
		databaseManager.setAktifAnak(anak);
		
		pelanggaran.setDataMonitoring(dataMonitoring);
		pelanggaran.setAnak(anak);
		
		Lokasi lokasi = peringatan.getLokasiAnak();
		lokasi.setId(idGenerator.getIdLocation());
		
		Calendar cal = Calendar.getInstance();
		long timeMilis = cal.getTimeInMillis();
		
		lokasi.setTime(timeMilis);
		
		pelanggaran.setLokasi(lokasi);
		pelanggaran.setWaktuPelanggaran(timeMilis);
		
		databaseManager.addPelanggaran(pelanggaran);
		
	}
	
}
