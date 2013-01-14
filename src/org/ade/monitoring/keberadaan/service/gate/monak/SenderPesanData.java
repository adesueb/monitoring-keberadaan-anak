package org.ade.monitoring.keberadaan.service.gate.monak;

import org.ade.monitoring.keberadaan.Variable.Status;
import org.ade.monitoring.keberadaan.Variable.TipeKoneksi;
import org.ade.monitoring.keberadaan.Variable.TipePesanMonak;
import org.ade.monitoring.keberadaan.entity.DataMonitoring;
import org.ade.monitoring.keberadaan.entity.Peringatan;
import org.ade.monitoring.keberadaan.entity.IPesanData;
import org.ade.monitoring.keberadaan.service.gate.ASenderMonak;
import org.ade.monitoring.keberadaan.service.gate.SenderInternet;
import org.ade.monitoring.keberadaan.service.gate.SenderSMS;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

public class SenderPesanData extends ASenderMonak{
	
	public SenderPesanData(Context context, Handler handler){
		super(context);
		senderSMS		= getSenderSMS();
		senderInternet	= new SenderInternet(context, new HandlerSenderInternetMonitoring(this));
		this.handler	= handler;
	}
	public void sendDataMonitoringBaru(DataMonitoring dataMonitoring){
		dataMonitoring.setTipe(TipePesanMonak.DATAMONITORING_BARU);
		pesanData 		= dataMonitoring;
		kirimPesanData(pesanData);
	}
	
	public void sendPeringatanTerlarang(Peringatan peringatan){
		peringatan.setTipe(TipePesanMonak.PERINGATAN_TERLARANG);
		pesanData = peringatan;
		kirimPesanData(pesanData);
	}

	public void sendPeringatanSeharusnya(Peringatan peringatan){
		peringatan.setTipe(TipePesanMonak.PERINGATAN_SEHARUSNYA);
		pesanData = peringatan;		
		kirimPesanData(pesanData);
	}
	
	private void kirimPesanData( IPesanData pesanData ){
		String phoneNumber;
		if(pesanData.getTipe()==TipePesanMonak.DATAMONITORING_BARU){
			DataMonitoring dataMonitoring = (DataMonitoring) pesanData;
			phoneNumber = dataMonitoring.getAnak().getNoHpAnak();
			
		}else{
			Peringatan peringatan = (Peringatan) pesanData;
			phoneNumber = peringatan.getIdOrtu();
		}
		senderSMS.sendSMS(phoneNumber, pesanData.getJsonPesanData());
	}
	
	public void sendInternet(){
		senderInternet.kirimPesanData(pesanData);
	}
	
	public void success(int tipeKoneksi){
		if(handler==null) return;
		handler.sendEmptyMessage(Status.SUCCESS);	
		
		
	}
	
	public void failed(int tipeKoneksi){
		if(handler==null) return;
		if(tipeKoneksi==TipeKoneksi.INTERNET){
			handler.sendEmptyMessage(Status.FAILED);
		}else{
			sendInternet();
		}
	}
	
	private final SenderSMS			senderSMS;
	private final SenderInternet	senderInternet;
	private IPesanData 				pesanData;
	private final Handler			handler;

	private static final class HandlerSenderInternetMonitoring extends Handler{

		public HandlerSenderInternetMonitoring(SenderPesanData senderMonitoring){
			this.senderMonitoring = senderMonitoring;
		}
		
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
				case Status.SUCCESS:{
					senderMonitoring.success(TipeKoneksi.INTERNET);
					break;
				}case Status.FAILED:{
					senderMonitoring.failed(TipeKoneksi.INTERNET);
					break;
				}
			}
		}
		private SenderPesanData senderMonitoring;
		
	}

	
}
