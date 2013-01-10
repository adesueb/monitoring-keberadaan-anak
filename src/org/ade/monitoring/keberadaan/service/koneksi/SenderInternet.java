package org.ade.monitoring.keberadaan.service.koneksi;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.ade.monitoring.keberadaan.Variable.Status;
import org.ade.monitoring.keberadaan.entity.Anak;
import org.ade.monitoring.keberadaan.entity.IPesanData;
import org.ade.monitoring.keberadaan.service.storage.PreferenceMonitoringManager;

import com.codegero.internet.SocketClientGero;

import android.content.Context;
import android.os.Handler;

public class SenderInternet implements ISender{
	
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

	private Handler handler;
	private Context context;
}