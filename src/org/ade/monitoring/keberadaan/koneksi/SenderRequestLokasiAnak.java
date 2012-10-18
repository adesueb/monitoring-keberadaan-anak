package org.ade.monitoring.keberadaan.koneksi;

import org.ade.monitoring.keberadaan.Variable.Status;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

public class SenderRequestLokasiAnak{

	public SenderRequestLokasiAnak(Context context, Handler handler){
		senderSMS		= new SenderSMS(context, new HandlerSenderSMSRequestLocation(this));
		senderInternet	= new SenderInternet(context, new HandlerSenderInternetRequestLocation(this));
		this.handler	= handler;
	}
	
	public void send(){
		
	}
	
	private final SenderSMS			senderSMS;
	private final SenderInternet	senderInternet;
	private final Handler			handler;
	
	private static final class HandlerSenderSMSRequestLocation extends Handler{

		public HandlerSenderSMSRequestLocation(SenderRequestLokasiAnak senderMonitoring){
			senderMonitoring = senderMonitoring;
		}
		
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case Status.FAILED:{
					break;
				}case Status.SUCCESS:{
					break;
				}
			}
		}
		private SenderRequestLokasiAnak senderMonitoring;
	}
	
	private static final class HandlerSenderInternetRequestLocation extends Handler{

		public HandlerSenderInternetRequestLocation(SenderRequestLokasiAnak senderMonitoring){
			senderMonitoring = senderMonitoring;
		}
		
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case Status.FAILED:{
					break;
				}case Status.SUCCESS:{
					break;
				}
			}
		}
		private SenderRequestLokasiAnak senderMonitoring;
	}
}
