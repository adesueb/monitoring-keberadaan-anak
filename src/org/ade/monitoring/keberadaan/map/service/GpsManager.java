package org.ade.monitoring.keberadaan.map.service;

import org.ade.monitoring.keberadaan.entity.Lokasi;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;


public class GpsManager {

	public GpsManager (Context context, Handler mHandler){
		mContext = context;
		mTracker = new Tracker(mContext, mHandler);
	}
	public void searchLokasi(){
		mTracker.startSinggleTrack(); 
	}
	
	public boolean isGpsActif(){
		LocationManager manager = (LocationManager) mContext.getSystemService( Context.LOCATION_SERVICE );

		return manager.isProviderEnabled( LocationManager.GPS_PROVIDER )  ;

	}
	
	// if latitude of lokasi is null or 0 then retrieving lokasi cannot success yet....
	public Lokasi getLokasi(  ){
		return mTracker.getLokasi();
	}
	
	public Lokasi getLastLokasi(){
		
		Lokasi lokasi = new Lokasi();
		
		LocationManager lokasiManager 	= (LocationManager)mContext.getSystemService(Context.LOCATION_SERVICE);
        Location 		location		= lokasiManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        
        if(location==null) {
        	return null;
        }
        
        lokasi.setLatitude(location.getLatitude());
        lokasi.setLongitude(location.getLongitude());
        lokasi.setTime(location.getTime());
       
        return lokasi;
	}

	private final Tracker mTracker; 
	private final Context mContext;
	
}
