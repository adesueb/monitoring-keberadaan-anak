package org.ade.monitoring.keberadaan.map.service;

import org.ade.monitoring.keberadaan.Variable.Status;
import org.ade.monitoring.keberadaan.entity.Lokasi;
import org.ade.monitoring.keberadaan.service.storage.PreferenceMonitoringManager;
import org.ade.monitoring.keberadaan.util.BundleEntityMaker;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class Tracker implements LocationListener{
	
	
	public Tracker(Context context, Lokasi lokasi, Handler handler){
		mContext	= context;
		mLokasi		= lokasi;
		mHandler	= handler;
		pref		= new PreferenceMonitoringManager(context);
	}
	
	public Tracker(Context context, Handler handler){
		mContext	= context;
		mLokasi		= new Lokasi();
		mHandler	= handler;
		pref		= new PreferenceMonitoringManager(context);
	}
	
	
	public void startSinggleTrack(){
		mlocManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
		
		 if (!mlocManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
			 return ;
		 }
		LocationProvider high =
			    mlocManager.getProvider(mlocManager.getBestProvider(createFineCriteria(), true));
		
		mlocManager.requestSingleUpdate(high.getName(), this, mHandler.getLooper());
	}
	

	
	public void startTracking(){
		mlocManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
		
		 if (!mlocManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
			 return ;
		 }
		
		LocationProvider high =
			    mlocManager.getProvider(mlocManager.getBestProvider(createFineCriteria(), true));
		
		mlocManager.requestLocationUpdates(high.getName(), TIME_MIN_UPDATE_LOCATION, DISTANCE_MIN_UPDATE_LOCATION,
				this);
		pref.setActiveTracker();
		
	}
	
	public void stopTracking(){
		mlocManager.removeUpdates(this);
		pref.setInActiveTracker();
	}
	
	public Lokasi getLokasi(){
		return mLokasi;
	}
	
	public void onLocationChanged(Location loc) {
		mLokasi.setLatitude(loc.getLatitude());
		mLokasi.setLongitude(loc.getLongitude());
		mLokasi.setTime(loc.getTime());

		if(mHandler != null){
			Message message = new Message();
			message.what = Status.SUCCESS;
			message.setData(BundleEntityMaker.makeBundleFromLocation(loc));
			mHandler.sendMessage(message);
		}	
	}

	public void onProviderDisabled(String provider) {
		stopTracking();
	}

	public void onProviderEnabled(String provider) {
		startTracking();
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
	}
	
	private Criteria createFineCriteria() {

		  Criteria c = new Criteria();
		  c.setAccuracy(Criteria.ACCURACY_FINE);
		  c.setAltitudeRequired(false);
		  c.setBearingRequired(false);
		  c.setSpeedRequired(false);
		  c.setCostAllowed(true);
		  c.setPowerRequirement(Criteria.POWER_HIGH);
		  return c;

	}
	
	private final Lokasi 						mLokasi;	
	private final Context 						mContext;
	private final Handler 						mHandler;
	private LocationManager 					mlocManager;
	private final PreferenceMonitoringManager 	pref;
	
	private static final long TIME_MIN_UPDATE_LOCATION = 1000; //in milisecond
	
	private static final float DISTANCE_MIN_UPDATE_LOCATION = 1;//in meters;

}
