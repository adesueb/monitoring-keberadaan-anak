package org.ade.monitoring.keberadaan.lokasi;

import org.ade.monitoring.keberadaan.entity.Lokasi;

import android.content.Context;
import android.os.Handler;


public class GpsManager {

	public GpsManager (Context mContext, Handler mHandler){
		mTracker 		= new Tracker(mContext, mHandler);
	}
	public void searchLokasi(){
		mTracker.startSinggleTrack(); 
	}
	
	// if latitude of lokasi is null or 0 then retrieving lokasi cannot success yet....
	public Lokasi getLokasi(  ){
		return mTracker.getLokasi();
	}

	private final Tracker mTracker ; 
	
}
