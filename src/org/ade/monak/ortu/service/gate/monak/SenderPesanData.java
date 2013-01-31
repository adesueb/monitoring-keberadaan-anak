package org.ade.monak.ortu.service.gate.monak;

import org.ade.monak.ortu.Variable.Status;
import org.ade.monak.ortu.Variable.TipeKoneksi;
import org.ade.monak.ortu.Variable.TipePesanMonak;
import org.ade.monak.ortu.entity.DataMonitoring;
import org.ade.monak.ortu.entity.IPesanData;
import org.ade.monak.ortu.service.gate.ASenderMonak;
import org.ade.monak.ortu.service.gate.SenderInternet;
import org.ade.monak.ortu.service.gate.SenderSMS;
import org.ade.monak.ortu.service.storage.LogMonakFileManager;

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
	
	public void sendDataMonitoringUpdate(DataMonitoring dataMonitoring){
		dataMonitoring.setTipe(TipePesanMonak.DATAMONITORING_UPDATE);
		pesanData 		= dataMonitoring;
		kirimPesanData(pesanData);
	}
	
	public void sendDataMonitoringDelete(DataMonitoring dataMonitoring){
		dataMonitoring.setTipe(TipePesanMonak.DATAMONITORING_DELETE);
		pesanData 		= dataMonitoring;
		kirimPesanData(pesanData);
	}
	
	
	private void kirimPesanData( IPesanData pesanData ){
		String phoneNumber = "";
		switch(pesanData.getTipe()){
			case TipePesanMonak.DATAMONITORING_BARU:{
			}case TipePesanMonak.DATAMONITORING_UPDATE:{
			}case TipePesanMonak.DATAMONITORING_DELETE:{
				DataMonitoring dataMonitoring = (DataMonitoring) pesanData;
				phoneNumber = dataMonitoring.getAnak().getNoHpAnak();
				break;
			}
		}
		LogMonakFileManager.debug("siap kirim peringatan");
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
