package org.ade.monitoring.keberadaan.lokasi;

import org.ade.monitoring.keberadaan.entity.Lokasi;
import org.ade.monitoring.keberadaan.service.Status;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Handler;

public class Tracker implements LocationListener{
	
	
	public Tracker(Context context, Lokasi lokasi, Handler handler){
		mContext	= context;
		mLokasi		= lokasi;
		mHandler	= handler;
	}
	
	public Tracker(Context context, Handler handler){
		mContext	= context;
		mLokasi		= new Lokasi();
		mHandler	= handler;
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
		
		mlocManager.requestLocationUpdates(high.getName(), 0, 0,
				this);
		
	}
	
	public void stopTracking(){
		mlocManager.removeUpdates(this);
	}
	
	public Lokasi getLokasi(){
		return mLokasi;
	}
	
	public void onLocationChanged(Location loc) {
		mLokasi.setLatitude(loc.getLatitude());
		mLokasi.setLongitude(loc.getLongitude());
		if(mHandler != null){
			mHandler.sendEmptyMessage(Status.SUCCESS);
		}	
	}

	public void onProviderDisabled(String provider) {
	}

	public void onProviderEnabled(String provider) {
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
	
	private final Lokasi mLokasi;	
	private final Context mContext;
	private final Handler mHandler;
	private LocationManager mlocManager;

}
