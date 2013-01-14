package org.ade.monitoring.keberadaan.service.gate;

import org.ade.monitoring.keberadaan.Variable.Status;
import org.ade.monitoring.keberadaan.Variable.TipeKoneksi;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

public abstract class ASenderMonak {
	public ASenderMonak(Context context){
		senderSms 		= new SenderSMS(context, new HandlerSenderSMS(this));
		this.context 	= context;
	}
	
	public abstract void success(int tipeKoneksi);
	public abstract void failed	(int tipeKoneksi);
	
	protected SenderSMS getSenderSMS(){
		return this.senderSms;
	}
	
	protected Context getContext(){
		return context;
	}
	
	private SenderSMS 	senderSms;
	private Context 	context;
	
	private static final class HandlerSenderSMS extends Handler{

		public HandlerSenderSMS(ASenderMonak senderMonak){
			this.senderMonak = senderMonak;
		}
		
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case Status.FAILED:{
					senderMonak.failed(TipeKoneksi.SMS);
					break;
				}case Status.SUCCESS:{
					senderMonak.success(TipeKoneksi.SMS);
					break;
				}
			}
		}
		
		private ASenderMonak senderMonak;

	}

}
