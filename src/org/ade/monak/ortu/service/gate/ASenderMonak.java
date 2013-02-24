package org.ade.monak.ortu.service.gate;

import org.ade.monak.ortu.Variable.TipeKoneksi;
import org.ade.monak.ortu.entity.Anak;
import org.ade.monak.ortu.service.gate.handler.HandlerSenderInternetMonak;
import org.ade.monak.ortu.service.gate.handler.HandlerSenderSmsMonak;
import org.ade.monak.ortu.service.storage.PreferenceMonitoringManager;

import android.content.Context;
import android.widget.Toast;

public abstract class ASenderMonak {
	
	public ASenderMonak(Context context){
		this.context	= context;
		senderSms 		= new SenderSMS(context, new HandlerSenderSmsMonak(this));
		senderInternet 	= new SenderInternetOrtu(context, new HandlerSenderInternetMonak(this));
	}
	
	public abstract void onSuccess(int tipeKoneksi);
	public abstract void onFailed	(int tipeKoneksi);
	
	public void success(int tipeKoneksi){
		onSuccess(tipeKoneksi);
	}
	
	public void failed(int tipeKoneksi){
		switch(tipeKoneksi){
			case TipeKoneksi.SMS:{
				Toast.makeText(context, "pengiriman melalui SMS gagal", Toast.LENGTH_SHORT).show();		
				break;
			}case TipeKoneksi.INTERNET:{
				Toast.makeText(context, "pengiriman melalui Internet gagal", Toast.LENGTH_SHORT).show();
				break;
			}
		}
		onFailed(tipeKoneksi);
	}

	public void kirimPesan(Anak anak, String pesan){
		PreferenceMonitoringManager pref = new PreferenceMonitoringManager(context);
		int tipeKoneksi = pref.getTipeKoneksi();
		switch(tipeKoneksi){
			case TipeKoneksi.SMS:{
				senderSms.kirimPesan(anak.getNoHpAnak(), pesan);
				break;
			}case TipeKoneksi.INTERNET:{
				senderInternet.kirimPesan(anak.getNoImeiAnak(), pesan);
				break;
			}
		}
	}
	
	public Context getContext(){
		return this.context;
	}
	
	private final Context 				context;
	private final SenderSMS 			senderSms;
	private final SenderInternetOrtu 	senderInternet;
	
}
