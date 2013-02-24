package org.ade.monak.ortu.service.gate.handler;

import org.ade.monak.ortu.Variable.Status;
import org.ade.monak.ortu.Variable.TipeKoneksi;
import org.ade.monak.ortu.service.gate.ASenderMonak;

import android.os.Handler;
import android.os.Message;

public class HandlerSenderInternetMonak extends Handler{

	public HandlerSenderInternetMonak(ASenderMonak senderMonak){
		this.senderMonak = senderMonak;
	}
	
	@Override
	public void handleMessage(Message msg) {
		switch(msg.what){
			case Status.SUCCESS:{
				senderMonak.success(TipeKoneksi.INTERNET);
				break;
			}case Status.FAILED:{
				senderMonak.failed(TipeKoneksi.INTERNET);
				break;
			}
		}
	}
	private ASenderMonak senderMonak;
	
}



