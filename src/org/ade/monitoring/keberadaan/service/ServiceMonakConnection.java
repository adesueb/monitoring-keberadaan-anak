package org.ade.monitoring.keberadaan.service;

import org.ade.monitoring.keberadaan.boundary.DaftarAnak;
import org.ade.monitoring.keberadaan.service.MonakService.BinderService;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

public class ServiceMonakConnection implements ServiceConnection{

	public ServiceMonakConnection(IBindMonakServiceConnection bind){
		this.bind = bind;
	}
	
	public void onServiceConnected(ComponentName name, IBinder service) {
		Log.d("daftar anak", "----onServiceConnected---");
		bind.setBinderHandlerMonak( ((BinderService) service).getMonakService().getBinderHandlerMonak());
		bind.setBound(true);
		
	}

	public void onServiceDisconnected(ComponentName name) {
		bind.setBound(false);
	}
	
	private final IBindMonakServiceConnection bind;
}
