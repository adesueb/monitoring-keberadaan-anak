package org.ade.monak.ortu.service.gate;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

import org.ade.monak.ortu.service.MonakService;
import org.ade.monak.ortu.service.storage.PreferenceMonitoringManager;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

public class InternetPushMonak implements Runnable{

	public InternetPushMonak(Context context){
		this.context = context;
		
	}
	public void run() {
		try {
			PreferenceMonitoringManager pref = new PreferenceMonitoringManager(context);
			String ip	= pref.getIp();
			int port	= pref.getPort();
		
			socket = new Socket(ip, port);
			DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
			BufferedReader buff = new BufferedReader(new InputStreamReader(socket.getInputStream()));	
			
			// TODO : isi dengan id Ortu...................
			dos.write((""+"\n").getBytes());
			//.............................................
			
			while(startConnection){
				String pesan = buff.readLine();
				Bundle bundle = new Bundle();
				bundle.putInt(MonakService.START_CALL, MonakService.RECEIVER_INTERNET);
				bundle.putString("pesan", pesan);
				Intent intent = new Intent(MonakService.MONAK_SERVICE);
				intent.putExtras(bundle);
				context.startService(intent);
			}
			buff.close();
			dos.close();
			socket.close();
					
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			if(startConnection){
				startConnection();
			}
		} finally{
		}
	}
	
	public void stopConnection(){
		startConnection = false;
	}
	
	public void startConnection(){
		if(isNetworkAvailable()){
			startConnection = true;
//			new Thread(this).start(); belum TODO:	
		}
		
	}
	
	public boolean isStart(){
		return startConnection;
	}
	private boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null;
	}
	
	private final Context context;
	private Socket socket;
	private boolean startConnection = false;
	

}
