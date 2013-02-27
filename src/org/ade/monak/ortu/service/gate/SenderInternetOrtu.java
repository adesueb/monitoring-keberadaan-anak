package org.ade.monak.ortu.service.gate;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

import org.ade.monak.ortu.Variable.Status;
import org.ade.monak.ortu.service.storage.LogMonakFileManager;
import org.ade.monak.ortu.service.storage.URLFileManager;

import android.content.Context;
import android.os.Handler;

public class SenderInternetOrtu{
	
	public SenderInternetOrtu(Context context, Handler handler){
		this.handler = handler;
	}
	
	public void kirimPesan(String imeiAnak, String message){
		try {
			String ip = URLFileManager.getIp();
			if(ip==null || ip.equals("")){
				ip = IP_SERVER;
			}
			Socket soc = new Socket(ip,PORT);
			soc.setSoTimeout(10000);
			DataOutputStream dos = new DataOutputStream(soc.getOutputStream());
			BufferedReader buff = new BufferedReader(new InputStreamReader(soc.getInputStream()));
			dos.write((imeiAnak+"#"+message+"\n").getBytes());
			dos.flush();
			LogMonakFileManager.debug("coba kirim");
			String response = buff.readLine();
			LogMonakFileManager.debug("terkirim");
			if(response.equals("sukses")){
				handler.sendEmptyMessage(Status.SUCCESS);
			}else{
				handler.sendEmptyMessage(Status.FAILED);
			}
			dos.close();
			buff.close();
			soc.close();
		} catch (UnknownHostException e) {

			handler.sendEmptyMessage(Status.FAILED);
			LogMonakFileManager.debug("salah host");
			e.printStackTrace();
		} catch (IOException e) {
			LogMonakFileManager.debug("io exception");
			handler.sendEmptyMessage(Status.FAILED);
			e.printStackTrace();
		}
	
	}

	private Handler handler;

	private static final String IP_SERVER = "103.11.252.4";
	private static final int PORT  = 2525;
}