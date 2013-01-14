package org.ade.monitoring.keberadaan.service.gate.monak;

import org.ade.monitoring.keberadaan.Variable.Status;
import org.ade.monitoring.keberadaan.Variable.TipePesanMonak;
import org.ade.monitoring.keberadaan.entity.Anak;
import org.ade.monitoring.keberadaan.service.gate.ASenderMonak;

import android.content.Context;
import android.os.Handler;

public class SenderPendaftaranAnak extends ASenderMonak{

	public SenderPendaftaranAnak(Context context, Handler handler){
		super(context);
		this.handler	= handler;
	}
	
	public void success(int tipeKoneksi) {
		if(handler!=null){
			handler.sendEmptyMessage(Status.SUCCESS);
		}
	}

	public void failed(int tipeKoneksi) {
		if(handler!=null){
			handler.sendEmptyMessage(Status.FAILED);
		}
	}
	
	public void sendAnak(Anak anak){
		getSenderSMS().sendSMS
			(anak.getNoHpAnak(), TipePesanMonak.PENDAFTARAN_ANAK+","+anak.getIdAnak());
	}
	
	private final Handler handler;
	
}
