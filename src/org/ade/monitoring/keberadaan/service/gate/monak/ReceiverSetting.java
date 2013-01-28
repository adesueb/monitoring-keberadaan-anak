package org.ade.monitoring.keberadaan.service.gate.monak;

import org.ade.monitoring.keberadaan.service.storage.PreferenceMonitoringManager;

import android.content.Context;

public class ReceiverSetting {

	public ReceiverSetting(Context context){
		pref = new PreferenceMonitoringManager(context);
	}
	
	public void receiveSetMiliseconds(int miliseconds){
		pref.setTrackMiliseconds(miliseconds);
	}
	
	public void receiveSetMeters(int meters){
		pref.setTrackMeters(meters);
	}
	
	public void receiveSetMaxLogs(int maxLogs){
		pref.setMaxLogs(maxLogs);
	}
	
	private final PreferenceMonitoringManager pref;
}
