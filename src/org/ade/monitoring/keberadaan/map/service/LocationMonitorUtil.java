package org.ade.monitoring.keberadaan.map.service;

import org.ade.monitoring.keberadaan.entity.DataMonitoring;
import org.ade.monitoring.keberadaan.entity.Lokasi;

public class LocationMonitorUtil {

	public LocationMonitorUtil(Lokasi monitorLocation, Lokasi currentLocation){
		mCurrentLocation = currentLocation;
		mMonitorLocation = monitorLocation;
	}
	
	public LocationMonitorUtil(DataMonitoring dataMonitoring){
		mMonitorLocation = dataMonitoring.getLokasi();
	}
	
	public LocationMonitorUtil(Lokasi monitorLocation){
		mMonitorLocation = monitorLocation;
	}
	
	public LocationMonitorUtil(){
	}
	
	public Lokasi getCurrentLocation() {
		return mCurrentLocation;
	}

	public void setCurrentLocation(Lokasi mCurrentLocation) {
		this.mCurrentLocation = mCurrentLocation;
	}

	public Lokasi getMonitorLocation() {
		return mMonitorLocation;
	}

	public void setMonitorLocation(Lokasi mMonitorLocation) {
		this.mMonitorLocation = mMonitorLocation;
	}

	public double getTolerancy() {
		return mTolerancy;
	}

	public void setTolerancy(double mTolerancy) {
		this.mTolerancy = mTolerancy;
	}
	
	public boolean isInTolerancy(){
		if(mCurrentLocation != null && mMonitorLocation != null){
			return LocationDistanceUtil.distanceInMeters
					(mCurrentLocation.getlatitude(), mCurrentLocation.getLongitude(), 
							mMonitorLocation.getlatitude(), mMonitorLocation.getLongitude())<mTolerancy;
			
		}
		return false;
	}

	private Lokasi mCurrentLocation;
	private Lokasi mMonitorLocation;
	private double mTolerancy = 100;
}
