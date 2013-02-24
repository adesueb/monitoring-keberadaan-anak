package org.ade.monak.ortu.service.storage;

import org.ade.monak.ortu.Variable.DaftarUrl;
import org.ade.monak.ortu.Variable.TipeKoneksi;
import org.ade.monak.ortu.entity.Lokasi;

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
		setPrefBool(SERVICE_STATUS, true);
	}
	
	public void setInActiveService(){
		setPrefBool(SERVICE_STATUS, false);
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

	public String getIp(){
		SharedPreferences pref = mContext.getSharedPreferences(URL, Activity.MODE_PRIVATE);
		return pref.getString(URL_IP, DaftarUrl.URL_IP_DEFAULT);
	}
	
	public void setIp(String ip){
		setPrefString(URL_IP, ip);
	}
	
	public int getTipeKoneksi(){
		SharedPreferences pref = mContext.getSharedPreferences(KONEKSI, Activity.MODE_PRIVATE);
		return pref.getInt(TIPE_KONEKSI, TipeKoneksi.SMS);
	}
	
	public void setTipeKoneksi(int tipeKoneksi){
		SharedPreferences pref = mContext.getSharedPreferences(KONEKSI, Activity.MODE_PRIVATE); 
		Editor editor  = pref.edit();
		editor.putInt(TIPE_KONEKSI, tipeKoneksi);
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
	
	
	public String getPrefString(String id, String valueDefault){
		SharedPreferences pref = mContext.getSharedPreferences(id, Activity.MODE_PRIVATE);
		return pref.getString(id, valueDefault);
	}
	
	private void setPrefString(String id, String value){
		SharedPreferences pref = mContext.getSharedPreferences(id, Activity.MODE_PRIVATE); 
		Editor editor  = pref.edit();
		editor.putString(id,value);
		editor.commit();		
	}
	private void setPrefBool(String id, boolean bool){
		SharedPreferences pref = mContext.getSharedPreferences(id, Activity.MODE_PRIVATE); 
		Editor editor  = pref.edit();
		editor.putBoolean(id,bool);
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
    private static final String KONEKSI			= "koneksi";
    private static final String TIPE_KONEKSI	= "tipe_koneksi";
    
}
