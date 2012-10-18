package org.ade.monitoring.keberadaan.service;

import java.io.FileDescriptor;
import java.io.InputStream;
import java.io.OutputStream;

import org.ade.monitoring.keberadaan.storage.PreferenceManager;

import com.codegero.internet.SocketClientGero;

import android.content.Context;
import android.os.Binder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public class InternetKoneksiMonakBinder extends Binder{

	private InternetKoneksiMonakBinder(Context context){
		
		PreferenceManager pref = new PreferenceManager(context);
		String ip	= pref.getIp();
		int port	= pref.getPort();
		socket = new SocketClientGero(ip, port);
		socket.bukaKoneksi();
	}
	public InputStream getInputStreamPush(){
		return socket.getInputStream();
	}
	public OutputStream getOutputStreamPush(){
		return socket.getOutputStream();
	}
	private SocketClientGero socket;

}
