package org.ade.monak.ortu.service.gate.monak;

import java.util.Calendar;

import org.ade.monak.ortu.Variable.Status;
import org.ade.monak.ortu.Variable.TipePesanMonak;
import org.ade.monak.ortu.entity.Anak;
import org.ade.monak.ortu.entity.DataMonitoring;
import org.ade.monak.ortu.entity.IPesanData;
import org.ade.monak.ortu.entity.Lokasi;
import org.ade.monak.ortu.entity.Pelanggaran;
import org.ade.monak.ortu.entity.Peringatan;
import org.ade.monak.ortu.service.BinderHandlerMonak;
import org.ade.monak.ortu.service.MonakService;
import org.ade.monak.ortu.service.Notifikasi;
import org.ade.monak.ortu.service.storage.DatabaseManagerOrtu;
import org.ade.monak.ortu.util.IDGenerator;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class ReceiverPesanData {
	
	

	public void menerimaPesanData(Context context, IPesanData pesanData,  BinderHandlerMonak binder){
		this.binderHandlerMonak = binder;
		if(pesanData!=null){
			switch(pesanData.getTipe()){
				case TipePesanMonak.DATAMONITORING_BARU:{
					new DatabaseManagerOrtu(context).addDataMonitoring((DataMonitoring) pesanData);
					break;
				}case TipePesanMonak.DATAMONITORING_UPDATE:{
					new DatabaseManagerOrtu(context).updateDataMonitoring((DataMonitoring) pesanData);
					break;
				}case TipePesanMonak.DATAMONITORING_DELETE:{
					new DatabaseManagerOrtu(context).deleteDataMonitoring((DataMonitoring) pesanData);
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
		
		DatabaseManagerOrtu databaseManager = new DatabaseManagerOrtu(context);
		IDGenerator idGenerator = new IDGenerator(context, databaseManager);
		
		Pelanggaran pelanggaran = new Pelanggaran();
		pelanggaran.setIdPelanggaran(idGenerator.getIdPelanggaran());

		DataMonitoring dataMonitoring = databaseManager.getDataMonitoringByIdMonitoring(peringatan.getIdMonitoring(), true, false);
		if(dataMonitoring==null){
			return;
		}
		dataMonitoring.setAktif(false);
		databaseManager.setAktifMonitoring(dataMonitoring);
		Handler handler = binderHandlerMonak.getSingleBindUIHandler(MonakService.WAITING_KONFIRMASI_AKTIF);
		if(handler!=null){
			Message message = new Message();
			Bundle bundle = new Bundle();
			bundle.putBoolean("aktif", false);
			bundle.putString("idMonitoring", dataMonitoring.getIdMonitoring());
			message.setData(bundle);
			message.what = Status.SUCCESS;
			handler.sendMessage(message);

		}
		Anak anak = dataMonitoring.getAnak();
		
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
	

	private BinderHandlerMonak binderHandlerMonak;
	
}
