package org.ade.monitoring.keberadaan.service.gate.monak;

import org.ade.monitoring.keberadaan.service.storage.PreferenceMonitoringManager;

import android.content.Context;

public class ReceiverTrackingMode {

	public ReceiverTrackingMode(Context context){
		this.pref = new PreferenceMonitoringManager(context);
	}
	
	public void startTrackingMode(){
		pref.setActiveTrackingMode();
	}
	
	public void stopTrackingMode(){
		pref.setInActiveTrackingMode();
	}
	
	private final PreferenceMonitoringManager pref;
}
