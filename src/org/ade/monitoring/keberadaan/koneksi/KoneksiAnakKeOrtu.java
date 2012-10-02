package org.ade.monitoring.keberadaan.koneksi;

import org.ade.monitoring.keberadaan.entity.Peringatan;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class KoneksiAnakKeOrtu{
	public KoneksiAnakKeOrtu(){
		
	}
	public boolean sendPeringatan(Peringatan peringatan){
		//TODO : send peringatan ke ortu.....
		return true;
	}

	private KoneksiSMS koneksiSms;
}
