package org.ade.monitoring.keberadaan.service.gate.monak;

import org.ade.monitoring.keberadaan.Variable.Status;
import org.ade.monitoring.keberadaan.Variable.TipePesanMonak;
import org.ade.monitoring.keberadaan.entity.Anak;
import org.ade.monitoring.keberadaan.service.gate.ASenderMonak;
import org.ade.monitoring.keberadaan.service.gate.SenderSMS;

import android.content.Context;
import android.os.Handler;

public class SenderRequestLokasiAnak extends ASenderMonak{

	public SenderRequestLokasiAnak(Context context, Handler handler, Anak anak){
		super(context);
		senderSMS		= getSenderSMS();
		this.handler	= handler;
		this.anak		= anak;
	}
	
	public void send(){
		kirimRequestLokasiAnak(anak);
	}
	
	private void kirimRequestLokasiAnak(Anak anak) {
		senderSMS.sendSMS(anak.getNoHpAnak(), TipePesanMonak.REQUEST_LOCATION_ANAK+","+anak.getIdAnak());
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
	
	private final SenderSMS			senderSMS;
	private final Handler			handler;
	private final Anak				anak;
	
}
