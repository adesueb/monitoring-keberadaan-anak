package org.ade.monitoring.keberadaan.service.gate.monak;

import org.ade.monitoring.keberadaan.Variable.Status;
import org.ade.monitoring.keberadaan.Variable.TipePesanMonak;
import org.ade.monitoring.keberadaan.entity.Anak;
import org.ade.monitoring.keberadaan.service.gate.SenderSMS;

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
		kirimRequestLokasiAnak(anak);
	}
	
	private void kirimRequestLokasiAnak(Anak anak) {
		senderSMS.sendSMS(anak.getNoHpAnak(), TipePesanMonak.REQUEST_LOCATION_ANAK+","+anak.getIdAnak());
	}
	
	
	private void success(){
		handler.sendEmptyMessage(Status.SUCCESS);
	}
	
	private void failed(){
		handler.sendEmptyMessage(Status.FAILED);
	}
	
	private final SenderSMS			senderSMS;
	private final Handler			handler;
	private final Anak				anak;
	
	
	private static final class HandlerSenderSMSRequestLocation extends Handler{

		public HandlerSenderSMSRequestLocation(SenderRequestLokasiAnak senderMonitoring){
			this.senderMonitoring = senderMonitoring;
		}
		
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case Status.FAILED:{
					senderMonitoring.failed();
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
