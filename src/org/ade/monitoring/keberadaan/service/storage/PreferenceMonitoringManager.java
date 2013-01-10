package org.ade.monitoring.keberadaan.service.storage;

import org.ade.monitoring.keberadaan.Variable.DaftarUrl;
import org.ade.monitoring.keberadaan.entity.Lokasi;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PreferenceMonitoringManager {

	public PreferenceMonitoringManager(Context context){
		mContext = context;
	}
	
	public boolean isServiceActive(){
		SharedPreferences pref = mContext.getSharedPreferences(SERVICE_STATUS, Activity.MODE_PRIVATE); 
		
		return pref.getBoolean(SERVICE_STATUS, false);
	}
	
	public void setActiveService(){
		SharedPreferences pref = mContext.getSharedPreferences(SERVICE_STATUS, Activity.MODE_PRIVATE); 
		Editor editor  = pref.edit();
		editor.putBoolean(SERVICE_STATUS,true);
		editor.commit();	
	}
	
	public void setInActiveService(){
		SharedPreferences pref = mContext.getSharedPreferences(SERVICE_STATUS, Activity.MODE_PRIVATE); 
		Editor editor  = pref.edit();
		editor.putBoolean(SERVICE_STATUS,false);
		editor.commit();
	}
	
	public Lokasi getMapLokasi(){
		Lokasi lokasi = new Lokasi();
		SharedPreferences pref = mContext.getSharedPreferences(LOKASI_MAP, Activity.MODE_PRIVATE); 
		lokasi.setLatitude((double)pref.getFloat(LATITUDE, 0));
		lokasi.setLongitude((double)pref.getFloat(LONGITUDE, 0));
		return lokasi;
	}
	
	public void setMapLokasi(Lokasi lokasi){
		SharedPreferences pref = mContext.getSharedPreferences(LOKASI_MAP, Activity.MODE_PRIVATE); 
		
		Editor editor  = pref.edit();
		editor.putFloat(LATITUDE, (float)lokasi.getlatitude());
		editor.putFloat(LONGITUDE, (float)lokasi.getLongitude());
		editor.commit();
	}
	
	public boolean isAktifTracker(){
		SharedPreferences pref = mContext.getSharedPreferences(TRACKER_STATUS, Activity.MODE_PRIVATE); 	
		return pref.getBoolean(TRACKER_STATUS, false);
	}
	
	public void setActiveTracker(){
		SharedPreferences pref = mContext.getSharedPreferences(TRACKER_STATUS, Activity.MODE_PRIVATE); 
		Editor editor  = pref.edit();
		editor.putBoolean(TRACKER_STATUS,true);
		editor.commit();	
	}
	
	public void setInActiveTracker(){
		SharedPreferences pref = mContext.getSharedPreferences(TRACKER_STATUS, Activity.MODE_PRIVATE); 
		Editor editor  = pref.edit();
		editor.putBoolean(TRACKER_STATUS,false);
		editor.commit();			
	}
	
	public String getIp(){
		SharedPreferences pref = mContext.getSharedPreferences(URL, Activity.MODE_PRIVATE);
		return pref.getString(URL_IP, DaftarUrl.URL_IP_DEFAULT);
	}
	
	public void setIp(String ip){
		SharedPreferences pref = mContext.getSharedPreferences(URL, Activity.MODE_PRIVATE); 
		Editor editor  = pref.edit();
		editor.putString(URL_IP, ip);
		editor.commit();
	}
	
	public int getPort(){
		SharedPreferences pref = mContext.getSharedPreferences(URL, Activity.MODE_PRIVATE);
		return pref.getInt(URL_PORT, DaftarUrl.URL_PORT_DEFAULT);
	}
	
	public void setPort(int port){
		SharedPreferences pref = mContext.getSharedPreferences(URL, Activity.MODE_PRIVATE); 
		Editor editor  = pref.edit();
		editor.putInt(URL_PORT, port);
		editor.commit();
	}

	private final Context mContext;
    private static final String LOKASI_MAP 		= "map";
    private static final String LATITUDE		= "latitude";
    private static final String LONGITUDE		= "longitude";
    private static final String URL				= "url";
    private static final String URL_IP			= "url_ip";
    private static final String	URL_PORT		= "url_port";
    private static final String SERVICE_STATUS	= "service";
    private static final String TRACKER_STATUS	= "tracker_status";
}
