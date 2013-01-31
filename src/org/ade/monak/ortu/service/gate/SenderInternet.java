package org.ade.monak.ortu.service.gate;

import org.ade.monak.ortu.entity.Anak;
import org.ade.monak.ortu.entity.IPesanData;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;

public class SenderInternet{
	
	public SenderInternet(Context context, Handler handler){
		this.context = context;
		this.handler = handler;
	}
	
	public void kirimPesanData(IPesanData pesanData){
		String text = pesanData.getJsonPesanData();
		
	
//		OutputStream out = socketClientGero.getOutputStream();
//		try {
//			out.write(text.getBytes());
//			handler.sendEmptyMessage(Status.SUCCESS);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		socketClientGero.tutupKoneksi();
		// TODO : send pesan data trough internet........
	}
	
	public void kirimRequestLokasiAnak(Anak anak) {
		// TODO Auto-generated method stub
		
	}
	
	private boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null;
	}

	private Handler handler;
	private Context context;
}