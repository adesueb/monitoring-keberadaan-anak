package org.ade.monitoring.keberadaan.koneksi;

import org.ade.monitoring.keberadaan.entity.PesanData;
import org.ade.monitoring.keberadaan.storage.PreferenceManager;

import android.content.Context;
import android.os.Handler;

public class SenderInternet {
	
	public SenderInternet(Context context, Handler handler){
		this.context = context;
		this.handler = handler;
	}
	
	public void kirimPesanData(PesanData pesanData){
		String text = pesanData.getJsonPesanData();
		
		PreferenceManager pref = new PreferenceManager(context);
		String url	= pref.getUrl();
		
		// TODO : send pesan data trough internet........
	}
	
	private Handler handler;
	private Context context;
}
