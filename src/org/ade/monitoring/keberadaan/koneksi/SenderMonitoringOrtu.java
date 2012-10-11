package org.ade.monitoring.keberadaan.koneksi;

import org.ade.monitoring.keberadaan.Variable.Status;
import org.ade.monitoring.keberadaan.Variable.TipePesanData;
import org.ade.monitoring.keberadaan.entity.DataMonitoring;
import org.ade.monitoring.keberadaan.entity.PesanData;
import org.ade.monitoring.keberadaan.storage.DatabaseManager;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

public class SenderMonitoringOrtu {
	
	public SenderMonitoringOrtu(Context context, Handler handler, DataMonitoring dataMonitoring){
		senderSMS		= new SenderSMS(context, new HandlerSenderSMSMonitoring(this));
		senderInternet	= new SenderInternet(context, new HandlerSenderInternetMonitoring(this));
		this.handler	= handler;
		pesanData 		= createPesanData(dataMonitoring, TipePesanData.DATAMONITORING_BARU);
	}
	
	public void sendDataMonitoringBaru(){
		senderSMS.kirimPesanData(pesanData);
	}
	
	public void sendPeringatanTerlarang(){
		senderSMS.kirimPesanData(pesanData);
	}

	public void sendPeringatanSeharusnya(){
		senderSMS.kirimPesanData(pesanData);
	}
	
	public void sendInternet(){
		senderInternet.kirimPesanData(pesanData);
	}
	
	private PesanData createPesanData(DataMonitoring dataMonitoring, int tipe){
		PesanData pesanData = new PesanData();
		pesanData.setDataMonitoring(dataMonitoring);
		pesanData.setTipe(tipe);
		return pesanData;
	}
	
	private void success(){
		handler.sendEmptyMessage(Status.SUCCESS);
	}
	
	private final SenderSMS			senderSMS;
	private final SenderInternet	senderInternet;
	private final PesanData 		pesanData;
	private final Handler			handler;
	
	private static final class HandlerSenderSMSMonitoring extends Handler{

		public HandlerSenderSMSMonitoring(SenderMonitoringOrtu senderMonitoring){
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
		private SenderMonitoringOrtu senderMonitoring;
	}

	private static final class HandlerSenderInternetMonitoring extends Handler{

		public HandlerSenderInternetMonitoring(SenderMonitoringOrtu senderMonitoring){
			this.senderMonitoring = senderMonitoring;
		}
		
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
				case Status.SUCCESS:{
					senderMonitoring.success();
				}case Status.FAILED:{
					//TODO : when failed... what will u do?
				}
			}
		}
		private SenderMonitoringOrtu senderMonitoring;
		
	}
	
}
