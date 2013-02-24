package org.ade.monak.ortu.service.gate.monak;

import org.ade.monak.ortu.Variable.Status;
import org.ade.monak.ortu.Variable.TipePesanMonak;
import org.ade.monak.ortu.entity.Anak;
import org.ade.monak.ortu.service.gate.SenderSMS;
import org.ade.monak.ortu.util.IDGenerator;

import android.content.Context;
import android.os.Handler;

public class SenderPendaftaranAnak{

	public SenderPendaftaranAnak(Context context, Handler handler){
		this.context	= context;
		this.handler	= handler;
		this.senderSms 		= new SenderSMS(context, handler);
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
	
	public void sendPendaftarAnak(Anak anak){
		IDGenerator idGenerator = new IDGenerator(context, null);
		senderSms.kirimPesan
			(anak.getNoHpAnak(), TipePesanMonak.PENDAFTARAN_ANAK+","+anak.getIdAnak()+","+idGenerator.getIdOrangTua());
	}
	
	public void sendUpdateAnak(Anak anak){
		IDGenerator idGenerator = new IDGenerator(context, null);
		senderSms.kirimPesan
			(anak.getNoHpAnak(), TipePesanMonak.ANAK_UPDATE+","+anak.getIdAnak()+","+idGenerator.getIdOrangTua());

	}
	
	public void sendHapusAnak(Anak anak){
		IDGenerator idGenerator = new IDGenerator(context, null);
		senderSms.kirimPesan
			(anak.getNoHpAnak(), TipePesanMonak.ANAK_DELETE+","+anak.getIdAnak()+","+idGenerator.getIdOrangTua());

	}
	
	private final SenderSMS				senderSms;
	
	private final Handler 	handler;
	private final Context	context;
	
}
