package org.ade.monak.ortu.service.gate.monak;

import org.ade.monak.ortu.Variable.Status;
import org.ade.monak.ortu.Variable.TipePesanMonak;
import org.ade.monak.ortu.entity.Anak;
import org.ade.monak.ortu.service.gate.ASenderMonak;
import org.ade.monak.ortu.util.IDGenerator;

import android.content.Context;
import android.os.Handler;

public class SenderRequestLokasiAnak extends ASenderMonak{

	public SenderRequestLokasiAnak(Context context, Handler handler, Anak anak){
		super(context);
		this.handler	= handler;
		this.anak		= anak;
	}
	
	public void send(){
		kirimRequestLokasiAnak(anak);
	}
	
	private void kirimRequestLokasiAnak(Anak anak) {

		IDGenerator idGenerator = new IDGenerator(getContext(), null);
		String pesan = TipePesanMonak.REQUEST_LOCATION_ANAK+","+anak.getIdAnak()+","+idGenerator.getIdOrangTua();
		kirimPesan(anak,pesan);
	}
	
	
	public void onSuccess(int tipeKoneksi) {
		if(handler!=null){
			handler.sendEmptyMessage(Status.SUCCESS);	
		}
		
	}

	public void onFailed(int tipeKoneksi) {
		if(handler!=null){
			handler.sendEmptyMessage(Status.FAILED);
		}
	}
	
	private final Handler			handler;
	private final Anak				anak;
	
}
