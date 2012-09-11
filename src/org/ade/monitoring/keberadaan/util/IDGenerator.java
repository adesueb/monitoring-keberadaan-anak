package org.ade.monitoring.keberadaan.util;

import org.ade.monitoring.keberadaan.storage.DatabaseManager;

import android.content.Context;
import android.telephony.TelephonyManager;

public class IDGenerator {

	public IDGenerator(Context context, DatabaseManager databaseManager){
		if(databaseManager==null){
			mDatabaseManager = new DatabaseManager(context);
		}else{
			mDatabaseManager = databaseManager;
		}
		if(context!=null){
			mContext = context;			
		}
	}
	
	public String getIdAnak(){
		String id = mDatabaseManager.getLasIdAnak();		
		id = ID_ANAK+generateAngkaId(id);
		return id;
	}
	
	public String getIdMonitoring(){
		String id = mDatabaseManager.getLastIdMonitoring();
		id = ID_MONITORING+generateAngkaId(id);
		return id;
	}
	
	public String getIdPelanggaran(){
		String id = mDatabaseManager.getLastIdPelanggaran();
		id = ID_PELANGGARAN+generateAngkaId(id);
		return id;
	}
	
	public String getIdOrangTua(){
		TelephonyManager telephonyManager = (TelephonyManager)
				mContext.getSystemService(Context.TELEPHONY_SERVICE);
		return telephonyManager.getDeviceId();
	}
	
	private String generateAngkaId(String id){
		int idAngka = Integer.parseInt(id.substring(1));
		
		idAngka++;
		
		id = ""+idAngka;
		
		String angka = id;
		
		for(int i=9;i>id.length();i--){
			angka = "0"+angka;
		}
		return angka;
	}
	
	private static final char ID_ANAK = 'A';
	private static final char ID_MONITORING = 'M';
	private static final char ID_PELANGGARAN = 'P';
	
	private Context mContext;
	
	private DatabaseManager mDatabaseManager;
}
