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
	
//	public boolean isAktifTracker(){
//		SharedPreferences pref = mContext.getSharedPreferences(TRACKER_STATUS, Activity.MODE_PRIVATE); 	
//		return pref.getBoolean(TRACKER_STATUS, false);
//	}
//	
//	public void setActiveTracker(){
//		setPrefBool(TRACKER_STATUS, true);
//	}
//	
//	public void setInActiveTracker(){
//		setPrefBool(TRACKER_STATUS, false);
//	}
	
	public boolean isAktifTrackingMode(){
		SharedPreferences pref = mContext.getSharedPreferences(TRACKING_MODE, Activity.MODE_PRIVATE); 	
		return pref.getBoolean(TRACKING_MODE, false);
	}
	
	public void setActiveTrackingMode(){
		setPrefBool(TRACKING_MODE, true);
	}
	
	public void setInActiveTrackingMode(){
		setPrefBool(TRACKING_MODE, false);
	}
	
	public String getIp(){
		SharedPreferences pref = mContext.getSharedPreferences(URL, Activity.MODE_PRIVATE);
		return pref.getString(URL_IP, DaftarUrl.URL_IP_DEFAULT);
	}
	
	public void setIp(String ip){
		setPrefString(URL_IP, ip);
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
	
	public void setNoHpOrtu(String noHp){
		setPrefString(NO_HP_ORTU, noHp);
	}
	

	public String getNoHpOrtu(){
		return getPrefString(NO_HP_ORTU, null);
	}
	
	public void setTrackMiliseconds(int miliseconds){
		setPrefString(MONITORING_MILISECONDS, miliseconds+"");
	}
	
	public int getTrackMiliseconds(){
		return Integer.parseInt(getPrefString(MONITORING_MILISECONDS, ""+30000));
	}
	
	public void setTrackMeters(int meters){
		setPrefString(MONITORING_METERS, meters+"");
	}
	
	public int getTrackMeters(){
		return Integer.parseInt(getPrefString(MONITORING_METERS, ""+0));
	}
	
	public void setMaxLogs(int maxLogs){
		setPrefString(MAX_LOGS, maxLogs+"");
	}
	
	public int getMaxLogs(){
		return Integer.parseInt(getPrefString(MAX_LOGS, ""+0));
	}
	
	public void setIdAnak(String idAnak){
		setPrefString(ID_ANAK, idAnak);
	}
	
	public String getIdAnak(){
		return getPrefString(ID_ANAK, null);
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
//    private static final String TRACKER_STATUS	= "tracker_status";
    private static final String TRACKING_MODE	= "tracking_mode";
    private static final String NO_HP_ORTU		= "no_hp_ortu";
    private static final String ID_ANAK			= "id_anak";
    private static final String MONITORING_MILISECONDS	= "monitoring_miliseconds";
    private static final String MONITORING_METERS		= "monitoring_meters";
    private static final String MAX_LOGS		= "maxLogs";
    
}
