package org.ade.monitoring.keberadaan.service.koneksi;

import org.ade.monitoring.keberadaan.Variable.Status;
import org.ade.monitoring.keberadaan.Variable.TipePesanData;
import org.ade.monitoring.keberadaan.entity.Anak;
import org.ade.monitoring.keberadaan.entity.DataMonitoring;
import org.ade.monitoring.keberadaan.entity.Peringatan;
import org.ade.monitoring.keberadaan.entity.IPesanData;
import org.ade.monitoring.keberadaan.service.storage.DatabaseManager;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

public class SenderMonitoring {
	
	public SenderMonitoring(Context context, Handler handler){
		senderSMS		= new SenderSMS(context, new HandlerSenderSMSMonitoring(this));
		senderInternet	= new SenderInternet(context, new HandlerSenderInternetMonitoring(this));
		this.handler	= handler;
	}
	public void sendDataMonitoringBaru(DataMonitoring dataMonitoring){
		dataMonitoring.setTipe(TipePesanData.DATAMONITORING_BARU);
		pesanData 		= dataMonitoring;
		senderSMS.kirimPesanData(pesanData);
	}
	
	public void sendPeringatanTerlarang(Peringatan peringatan){
		peringatan.setTipe(TipePesanData.PERINGATAN_TERLARANG);
		pesanData = peringatan;
		senderSMS.kirimPesanData(pesanData);
	}

	public void sendPeringatanSeharusnya(Peringatan peringatan){
		peringatan.setTipe(TipePesanData.PERINGATAN_SEHARUSNYA);
		pesanData = peringatan;		
		senderSMS.kirimPesanData(pesanData);
	}
	
	public void sendInternet(){
		senderInternet.kirimPesanData(pesanData);
	}
	
	private void success(){
		handler.sendEmptyMessage(Status.SUCCESS);
	}
	
	private void failed(){
		handler.sendEmptyMessage(Status.FAILED);
	}
	
	private final SenderSMS			senderSMS;
	private final SenderInternet	senderInternet;
	private IPesanData 				pesanData;
	private final Handler			handler;
	
	private static final class HandlerSenderSMSMonitoring extends Handler{

		public HandlerSenderSMSMonitoring(SenderMonitoring senderMonitoring){
			senderMonitoring = senderMonitoring;
		}
		
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case Status.FAILED:{
					senderMonitoring.sendInternet();
					break;
				}case Status.SUCCESS:{
					senderMonitoring.success();
				}
			}
		}
		private SenderMonitoring senderMonitoring;
	}

	private static final class HandlerSenderInternetMonitoring extends Handler{

		public HandlerSenderInternetMonitoring(SenderMonitoring senderMonitoring){
			this.senderMonitoring = senderMonitoring;
		}
		
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
				case Status.SUCCESS:{
					senderMonitoring.success();
					break;
				}case Status.FAILED:{
					senderMonitoring.failed();
					break;
				}
			}
		}
		private SenderMonitoring senderMonitoring;
		
	}
	
}
