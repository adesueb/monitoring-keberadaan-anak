package org.ade.monitoring.keberadaan.service.koneksi;

import org.ade.monitoring.keberadaan.Variable.Status;
import org.ade.monitoring.keberadaan.entity.Anak;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

public class SenderRequestLokasiAnak{

	public SenderRequestLokasiAnak(Context context, Handler handler, Anak anak){
		senderSMS		= new SenderSMS(context, new HandlerSenderSMSRequestLocation(this));
		this.handler	= handler;
		this.anak		= anak;
	}
	
	public void send(){
		senderSMS.kirimRequestLokasiAnak(anak);
	}
	
	
	public void success(){
		handler.sendEmptyMessage(Status.SUCCESS);
	}
	
	public void failed(){
		handler.sendEmptyMessage(Status.FAILED);
	}
	
	private final SenderSMS			senderSMS;
	private final Handler			handler;
	private final Anak				anak;
	
	
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
					senderMonitoring.success();
					break;
				}
			}
		}
		private SenderRequestLokasiAnak senderMonitoring;
	}
	

}
