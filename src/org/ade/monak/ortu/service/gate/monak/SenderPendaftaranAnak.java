package org.ade.monak.ortu.service.gate.monak;

import org.ade.monak.ortu.Variable.Status;
import org.ade.monak.ortu.Variable.TipePesanMonak;
import org.ade.monak.ortu.entity.Anak;
import org.ade.monak.ortu.service.gate.ASenderMonak;
import org.ade.monak.ortu.util.IDGenerator;

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
	
	public void sendPendaftarAnak(Anak anak){
		IDGenerator idGenerator = new IDGenerator(getContext(), null);
		getSenderSMS().sendSMS
			(anak.getNoHpAnak(), TipePesanMonak.PENDAFTARAN_ANAK+","+anak.getIdAnak()+","+idGenerator.getIdOrangTua());
	}
	
	public void sendUpdateAnak(Anak anak){
		IDGenerator idGenerator = new IDGenerator(getContext(), null);
		getSenderSMS().sendSMS
			(anak.getNoHpAnak(), TipePesanMonak.ANAK_UPDATE+","+anak.getIdAnak()+","+idGenerator.getIdOrangTua());

	}
	
	public void sendHapusAnak(Anak anak){
		IDGenerator idGenerator = new IDGenerator(getContext(), null);
		getSenderSMS().sendSMS
			(anak.getNoHpAnak(), TipePesanMonak.ANAK_DELETE+","+anak.getIdAnak()+","+idGenerator.getIdOrangTua());

	}
	
	private final Handler handler;
	
}
