package org.ade.monitoring.keberadaan.koneksi;

import org.ade.monitoring.keberadaan.entity.DataMonitoring;
import org.ade.monitoring.keberadaan.entity.PesanData;
import org.ade.monitoring.keberadaan.storage.DatabaseManager;

import android.content.Context;

public class SenderMonitoringOrtu {
	
	public SenderMonitoringOrtu(Context context){
		databaseManager	= new DatabaseManager(context);
	}
	
	public boolean sendDataMonitoring(DataMonitoring dataMonitoring){
		//TODO : send data monitoring sebelum disimpen di database ortu....
		return false;
	}
	
	private final DatabaseManager			databaseManager;
	
	
}
