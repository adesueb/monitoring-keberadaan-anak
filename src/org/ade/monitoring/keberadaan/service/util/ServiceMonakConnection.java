package org.ade.monitoring.keberadaan.service.util;

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
		bind.setBound(true);
		bind.setBinderHandlerMonak( ((BinderService) service).getMonakService().getBinderHandlerMonak());

		
	}

	public void onServiceDisconnected(ComponentName name) {
		bind.setBound(false);
	}
	
	private final IBindMonakServiceConnection bind;
}
