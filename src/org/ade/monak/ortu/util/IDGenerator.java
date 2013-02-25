package org.ade.monak.ortu.util;

import java.util.Calendar;
import java.util.List;

import org.ade.monak.ortu.service.storage.DatabaseManagerOrtu;
import org.ade.monak.ortu.service.storage.LogMonakFileManager;

import android.content.Context;
import android.telephony.TelephonyManager;

public class IDGenerator {

	public IDGenerator(Context context, DatabaseManagerOrtu databaseManager){
		if(databaseManager==null){
			mDatabaseManager = new DatabaseManagerOrtu(context);
		}else{
			mDatabaseManager = databaseManager;
		}
		if(context!=null){
			mContext = context;			
		}
	}
	
	public static String getIdMultiSms(){
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, cal.get(Calendar.YEAR));
		cal.set(Calendar.MONTH, 0);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		
		Long time = System.currentTimeMillis()-cal.getTimeInMillis();
		return time+"";
	}
	
	public String getIdAnak(){
		List<String> ids = mDatabaseManager.getIdAnaks();
		String id = ID_ANAK+generateAngkaId(ids);
		return id;
	}
	
	public String getIdMonitoring(){
		List<String> ids = mDatabaseManager.getIdMonitorings();
		String id = ID_MONITORING+generateAngkaId(ids);
		return id;
	}
	
	public String getIdPelanggaran(){
		List<String> ids = mDatabaseManager.getIdPelanggarans();
		String id = ID_PELANGGARAN+generateAngkaId(ids);
		return id;
	}
	
	public String getIdLocation(){
		List<String> ids = mDatabaseManager.getIdLokasis();
		String id = ID_LOCATION+generateAngkaId(ids);
		return id;
	}
	
	public String getIdOrangTua(){
		TelephonyManager telephonyManager = (TelephonyManager)
				mContext.getSystemService(Context.TELEPHONY_SERVICE);
		return telephonyManager.getDeviceId()+"B";
	}
	
	private String generateAngkaId(List<String> ids){
		int idAngka = 0;

		boolean ada = false;
		
		if(ids!=null && ids.size()>0){
			int i=0;
			for(String id:ids){
				
				if(id!=null&&!id.equals("")){
					idAngka = Integer.parseInt(id.substring(1));
				}else{
					idAngka = 0;	
				}
				
				if(idAngka==i){
					ada = true;
					i++;
				
				}else{
					ada = false;
					idAngka = i;
					break;
				}
				
			}
		}
	
		if(ada){
			idAngka++;	
		}
		
		LogMonakFileManager.debug("angka :"+idAngka);
		
		String angka = ""+idAngka;
		int zeroLength = 9-angka.length();
		for(int i=zeroLength;i>0;i--){
			angka = "0"+angka;
		}

		return angka;
	}
	
	
	private static final char ID_ANAK 			= 'A';
	private static final char ID_MONITORING 	= 'M';
	private static final char ID_PELANGGARAN 	= 'P';
	private static final char ID_LOCATION		= 'L';
	
	private Context mContext;
	
	private DatabaseManagerOrtu mDatabaseManager;
}
