package org.ade.monak.ortu.service.gate;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import org.ade.monak.ortu.service.MonakService;
import org.ade.monak.ortu.service.storage.LogMonakFileManager;
import org.ade.monak.ortu.util.IDGenerator;

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
		boolean timeOut = false;
		try {
			IDGenerator idGenerator = new IDGenerator(context, null);
			socket = new Socket(IP_SERVER, PORT);
			socket.setSoTimeout(20000);
			dos = new DataOutputStream(socket.getOutputStream());
			BufferedReader buff = new BufferedReader(new InputStreamReader(socket.getInputStream()));	
			
			//kirim id Ortu................................
			dos.write((idGenerator.getIdOrangTua()+"\n").getBytes());
			//.............................................
			
			// start ping pong to test koneksi.............
//			new Thread(new PingPong(this)).start(); belum perlu
			//.............................................
			
			while(startConnection){
				String pesan = buff.readLine();
				if(pesan==null){
					break;
				}else if(pesan.equals(PONG)){
					LogMonakFileManager.debug("dapet pong");
				}else{
					dos.write((SUKSES+"\n").getBytes());
					Bundle bundle = new Bundle();
					bundle.putInt(MonakService.START_CALL, MonakService.RECEIVER_INTERNET);
					bundle.putString(MonakService.PESAN_INTERNET, pesan);
					Intent intent = new Intent(MonakService.MONAK_SERVICE);
					intent.putExtras(bundle);
					context.startService(intent);	
				}
			}
			buff.close();
			dos.close();
			socket.close();
					
		} catch (UnknownHostException e) {
		} catch(SocketTimeoutException e){
			timeOut = true;
		}catch (IOException e) {
		}finally{				
			if(timeOut){
				LogMonakFileManager.debug("timeout");
				
				if(startConnection){
					startConnection();
				}
			}else{
				if(startConnection){
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					startConnection();
				}
			}
		}
	}
	
	public void stopConnection(){
		startConnection = false;
		if(socket.isConnected()){
			try {
				socket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}finally{
			}
		}
	}
	
	public void startConnection(){

//		if(isNetworkAvailable()){
//			LogMonakFileManager.debug("start connection");
			startConnection = true;
			new Thread(this).start();	
//		}else{
//			LogMonakFileManager.debug("stop connection");
//			startConnection = false;
//		}
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
	public DataOutputStream getDos() {
		return dos;
	}



	private final Context context;
	private Socket socket;
	private DataOutputStream dos;
	private boolean startConnection = false;
	private static final String IP_SERVER = "49.50.8.137";
	private static final int PORT  = 4444;
	private static final String PING	= "?";
	private static final String PONG	= "Y";
	private static final String SUKSES	= "Y";

	private static final class PingPong implements Runnable{

		public PingPong(InternetPushMonak internetPushMonak){
			this.internetPushMonak = internetPushMonak;
		}
		public void run() {
			try {
				while(true){
					Thread.sleep(10000);
					internetPushMonak.getDos().write((PING+"\n").getBytes());	
					LogMonakFileManager.debug("mencoba ngeping");
				}				
			} catch (IOException e) {
			} catch (InterruptedException e) {
				e.printStackTrace();
			}finally{
				this.internetPushMonak.stopConnection();
				this.internetPushMonak.startConnection();
			}
		}
		
		private final InternetPushMonak internetPushMonak;
		
	}
}
