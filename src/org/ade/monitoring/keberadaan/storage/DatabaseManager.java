package org.ade.monitoring.keberadaan.storage;


import java.util.ArrayList;
import java.util.List;

import org.ade.monitoring.keberadaan.entity.Anak;
import org.ade.monitoring.keberadaan.entity.DataMonitoring;
import org.ade.monitoring.keberadaan.entity.Lokasi;
import org.ade.monitoring.keberadaan.entity.Pelanggaran;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseManager {

	
	public DatabaseManager (Context context) { 
		mDatabaseHelper = new DatabaseHelper(context);
	}
	  
	
	// get.................................................................
	public List<Anak> getAllAnak(boolean withPelanggaran, boolean withMonitoring){
		Cursor cursor = actionQuery(ANAK_TABLE_NAME, null, null);
		if(cursor!=null && cursor.getCount()>0){
			return getAnaksFromCursor(cursor, withPelanggaran, withMonitoring);
		}
		return null;
	}
	
	public Anak getAnakById(String idAnak, boolean withPelanggaran, boolean withMonitoring){
		Cursor cursor = actionQuery(ANAK_TABLE_NAME, null,COLUMN_ID_ANAK+"='"+idAnak+"'");
		if(cursor!=null && cursor.getCount()>0){			
			return getAnakFromCursor(cursor, withPelanggaran, withMonitoring);
		}else{
			return null;
		}
	}
	
	
	public List<DataMonitoring> getAllDataMonitorings(boolean withAnak){
		Cursor cursor = actionQuery(MONITORING_TABLE_NAME, null,null);
		if(cursor != null && cursor.getCount()>0){
			return getDataMonitoringsFromCursor(cursor, withAnak);
		}else{
			return null;
		}
		
	}
	
	public DataMonitoring getDataMonitoringByIdMonitoring(String idDataMonitoring, boolean withAnak){
		Cursor cursor = actionQuery(MONITORING_TABLE_NAME, null, COLUMN_ID_MONITORING+"='"+idDataMonitoring+"'");
		if(cursor!=null && cursor.getCount()>0){
			return getDataMonitoringFromCursor(cursor, withAnak);
		}else{
			return null;
		}
	}
		
	public List<DataMonitoring> getDataMonitoringsByAnak(String idAnak){
		Cursor cursor = actionQuery(MONITORING_TABLE_NAME, null, COLUMN_ANAK_MONITORING+"='"+idAnak+"'");
		if(cursor!=null && cursor.getCount()>0){
			return getDataMonitoringsFromCursor(cursor, false);
		}else{
			return null;
		}
	}
	
	public List<Pelanggaran> getDataPelanggaransByAnak(String idAnak){
		Cursor cursor = actionQuery(PELANGGARAN_TABLE_NAME, null, COLUMN_ANAK_PELANGGARAN+"='"+idAnak+"'");
		if(cursor!=null && cursor.getCount()>0){
			return getPelanggaransFromCursor(cursor, false, false);
		}else{
			return null;
		}
	}
	
	public List<Pelanggaran> getAllDataPelanggarans(boolean withAnak, boolean withMonitoring){
		Cursor cursor = actionQuery(PELANGGARAN_TABLE_NAME, null, null);
		if(cursor!=null && cursor.getCount()>0){
			return getPelanggaransFromCursor(cursor, withAnak, withMonitoring);
		}else{
			return null;
		}
	}
	//...................................................................
	
	// add...............................................................
	public void addDataMonitorings(List<DataMonitoring> dataMonitorings){
		if(dataMonitorings!=null){
			for(DataMonitoring dataMonitoring:dataMonitorings){
				addDataMonitoring(dataMonitoring);
			}
		}
		
	}
	  
	public void addDataMonitoring( DataMonitoring dataMonitoring ){
		if(dataMonitoring!=null){
			ContentValues cv = new ContentValues();
			cv.put(COLUMN_ID_MONITORING, dataMonitoring.getIdMonitoring());
			cv.put(COLUMN_DATE_MULAI_MONITORING, dataMonitoring.getWaktuMulaiLong());
			cv.put(COLUMN_DATE_SELESAI_MONITORING, dataMonitoring.getWaktuSelesaiLong());
			cv.put(COLUMN_LATITUDE_MONITORING, dataMonitoring.getLokasi().getlatitude());
			cv.put(COLUMN_LONGITUDE_MONITORING, dataMonitoring.getLokasi().getLongitude());
			cv.put(COLUMN_STATUS_MONITORING, dataMonitoring.getStatus());
			if(dataMonitoring.getAnak()!=null){
				cv.put(COLUMN_ANAK_MONITORING, dataMonitoring.getAnak().getIdAnak());	
			}
			getDb().insert(MONITORING_TABLE_NAME, null, cv);	
		}
		
		
	}
	
	public void addAnak(Anak anak){
		ContentValues cv = new ContentValues();
		cv.put(COLUMN_ID_ANAK, anak.getIdAnak());
		cv.put(COLUMN_NAMA_ANAK, anak.getNamaAnak());
		cv.put(COLUMN_NO_HP_ANAK, anak.getNoHpAnak());
		long result = getDb().insert(ANAK_TABLE_NAME, null, cv);
		
		if(result <=0 || anak.getPelanggarans()==null){
			
		}else{
			addPelanggarans(anak.getPelanggarans());
		}
		if(result <=0 || anak.getDataMonitorings()==null){
			
		}else{
			addDataMonitorings(anak.getDataMonitorings());
		}
	}
	
	public void addPelanggarans(List<Pelanggaran> pelanggarans){
		if(pelanggarans!=null){
			for(Pelanggaran pelanggaran:pelanggarans){
				addPelanggaran(pelanggaran);
			}	
		}
		
	}
	
	public void addPelanggaran(Pelanggaran pelanggaran){
		if(pelanggaran!=null){
			ContentValues cv = new ContentValues();
			cv.put(COLUMN_ID_PELANGGARAN, pelanggaran.getIdPelanggaran());
			cv.put(COLUMN_DATE_PELANGGARAN, pelanggaran.getWaktuPelanggaran());
			cv.put(COLUMN_LATITUDE_PELANGGARAN, pelanggaran.getLokasi().getlatitude());
			cv.put(COLUMN_LONGITUDE_PELANGGARAN, pelanggaran.getLokasi().getLongitude());
			getDb().insert(PELANGGARAN_TABLE_NAME, null, cv);	
		}
	}
	//............................................................................
	
	//get from Cursor.............................................................
	private List<Anak> getAnaksFromCursor(Cursor cursor, boolean withPelanggaran, boolean withMonitoring){
		List<Anak> anaks =  new ArrayList<Anak>();
		if(cursor.moveToFirst()){
			do{	anaks.add(getAnakFromCursor(cursor, withPelanggaran, withMonitoring));
			}while(cursor.moveToNext());
		}
		return anaks;
	}
	
	private Anak getAnakFromCursor(Cursor cursor, boolean withPelanggaran, boolean withMonitoring){
		if(cursor!=null && cursor.getCount()>0){
			Anak anak = new Anak();
			int indexIdAnak 	= cursor.getColumnIndex(COLUMN_ID_ANAK);
			int indexNamaAnak	= cursor.getColumnIndex(COLUMN_NAMA_ANAK);
			int indexPhoneAnak	= cursor.getColumnIndex(COLUMN_NO_HP_ANAK);
			
			anak.setIdAnak(cursor.getString(indexIdAnak));
			anak.setNamaAnak(cursor.getString(indexNamaAnak));
			anak.setNoHpAnak(cursor.getString(indexPhoneAnak));
			if(withPelanggaran){
				//TODO : query all pelanggaran anak
				
			}
			if(withMonitoring){
				anak.setDataMonitorings(getDataMonitoringsByAnak(cursor.getString(indexIdAnak)));
			}
			return anak;
		}
		return null;
		
	}
	
	
	private List<DataMonitoring> getDataMonitoringsFromCursor(Cursor cursor, boolean withAnak){
		List <DataMonitoring> dataMonitorings = new ArrayList<DataMonitoring>();
		if(cursor.moveToFirst()){
			do{
				 dataMonitorings.add(getDataMonitoringFromCursor(cursor, withAnak));
			}while(cursor.moveToNext());
		}
		return dataMonitorings;
	}
	
	private DataMonitoring getDataMonitoringFromCursor(Cursor cursor, boolean withAnak){
		if(cursor!=null && cursor.getCount()>0){
			
			DataMonitoring dataMonitoring = new DataMonitoring();
			 
			int indexIdMonitoring 			= cursor.getColumnIndex(COLUMN_ID_MONITORING);
			int indexLatitudeMonitoring 	= cursor.getColumnIndex(COLUMN_LATITUDE_MONITORING);
			int indexLongitudeMonitoring 	= cursor.getColumnIndex(COLUMN_LONGITUDE_MONITORING);
			int indexStatusMonitoring 		= cursor.getColumnIndex(COLUMN_STATUS_MONITORING);
			int indexDateMulaiMonitoring 	= cursor.getColumnIndex(COLUMN_DATE_MULAI_MONITORING);
			int indexDateSelesaiMonitoring = cursor.getColumnIndex(COLUMN_DATE_SELESAI_MONITORING);
			 
		 
			dataMonitoring.setIdMonitoring(cursor.getString(indexIdMonitoring));
			dataMonitoring.setLokasi(
				new Lokasi(cursor.getDouble(indexLatitudeMonitoring), 
						 cursor.getDouble(indexLongitudeMonitoring)));
			dataMonitoring.setStatus(cursor.getInt(indexStatusMonitoring));
		 
			dataMonitoring.setWaktuMulai(cursor.getLong(indexDateMulaiMonitoring));
		 
			dataMonitoring.setWaktuSelesai(cursor.getLong(indexDateSelesaiMonitoring));
		 
			if(withAnak){
			
				int indexAnakMonitoring		= cursor.getColumnIndex(COLUMN_ANAK_MONITORING);
				dataMonitoring.setAnak(getAnakById(cursor.getString(indexAnakMonitoring), false, false)); 
			}
			return dataMonitoring;
		}
		return null;
		
	}
	
	public List<Pelanggaran> getPelanggaransFromCursor(Cursor cursor, boolean withAnak, boolean withMonitoring){
		List<Pelanggaran> pelanggarans = new ArrayList<Pelanggaran>();
		if(cursor.moveToFirst()){
			do{
				 pelanggarans.add(getPelanggaranFromCursor(cursor, withAnak, withMonitoring));
			}while(cursor.moveToNext());
		}
		return pelanggarans;
	}
	public Pelanggaran getPelanggaranFromCursor(Cursor cursor, boolean withAnak, boolean withMonitoring){
		if(cursor!=null && cursor.getCount()>0){
			Pelanggaran pelanggaran = new Pelanggaran();
			int indexIdPelanggaran 			= cursor.getColumnIndex(COLUMN_ID_PELANGGARAN);
			int indexLatitudePelanggaran 	= cursor.getColumnIndex(COLUMN_LATITUDE_PELANGGARAN);
			int indexLongitudePelanggaran 	= cursor.getColumnIndex(COLUMN_LONGITUDE_PELANGGARAN);
			int indexDatePelanggaran 		= cursor.getColumnIndex(COLUMN_DATE_PELANGGARAN);
				pelanggaran.setIdPelanggaran(cursor.getString(indexIdPelanggaran));
			pelanggaran.setWaktuPelanggaran(cursor.getLong(indexDatePelanggaran));
			Lokasi lokasi = new Lokasi();
			lokasi.setLatitude(cursor.getDouble(indexLatitudePelanggaran));
			lokasi.setLongitude(cursor.getDouble(indexLongitudePelanggaran));
			pelanggaran.setLokasi(lokasi);
			if(withAnak){
				int indexAnakPelanggaran		= cursor.getColumnIndex(COLUMN_ANAK_PELANGGARAN);
				pelanggaran.setAnak(getAnakById(cursor.getString(indexAnakPelanggaran), false, false));
			}
			if(withMonitoring){
				int indexMonitoringPelanggaran	= cursor.getColumnIndex(COLUMN_MONITORING_PELANGGARAN);
				pelanggaran.setDataMonitoring(
						getDataMonitoringByIdMonitoring(cursor.getString(indexMonitoringPelanggaran), false));
			}
			return pelanggaran;
		}
		return null;
		
	}
	//............................................................................
	
	private Cursor actionQuery(String table, String[]columns, String selection){
	  
		SQLiteDatabase db 	= getDb();
		final Cursor cursor = 
				db.query(table, columns, selection, null, null, null, null, null);
		db = null;
		return cursor;
	}
	  
	private SQLiteDatabase getDb(){
		return mDatabaseHelper.getWritableDatabase();
	}
	  
	private class DatabaseHelper extends SQLiteOpenHelper{
		
	    
	    public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
	
	    private void createTables(SQLiteDatabase db){
	    	db.execSQL(CREATE_ANAK);
			db.execSQL(CREATE_PELANGGARAN);
			db.execSQL(CREATE_MONITORING);
	    }
	    
	    private void dropTables(SQLiteDatabase db){
	    	db.execSQL("DROP TABLE IF EXISTS " + ANAK_TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + PELANGGARAN_TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + MONITORING_TABLE_NAME);

	    }
	    
		@Override
		public void onCreate(SQLiteDatabase db) {
			createTables(db);
		}
	
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			dropTables(db);
			createTables(db);
		}
		
		private static final String DATABASE_NAME = "monitoring_keberadaan.db";
	    private static final int DATABASE_VERSION = 16;
		
	    
	    private static final String CREATE_ANAK = 
	    		"CREATE TABLE IF NOT EXISTS "+
	    		ANAK_TABLE_NAME+" ("+
	    		COLUMN_ID_ANAK+" VARCHAR(10) PRIMARY KEY,"+
	    		COLUMN_NAMA_ANAK+" VARCHAR(100),"+
	    		COLUMN_NO_HP_ANAK+" VARCHAR(50))";
	    
	    private static final String CREATE_PELANGGARAN = 
	    		"CREATE TABLE IF NOT EXISTS "+
	    		PELANGGARAN_TABLE_NAME+" ("+
	    		COLUMN_ID_PELANGGARAN+" VARCHAR(10) PRIMARY KEY,"+
	    		COLUMN_ANAK_PELANGGARAN+" VARCHAR(10),"+
	    		COLUMN_MONITORING_PELANGGARAN+ " VARCHAR(10)," +
	    		COLUMN_DATE_PELANGGARAN+" INTEGER,"+
	    		COLUMN_LATITUDE_PELANGGARAN+" VARCHAR(50),"+
	    		COLUMN_LONGITUDE_PELANGGARAN+" VARCHAR(50),"+
	    		"FOREIGN KEY("+COLUMN_ANAK_PELANGGARAN+") REFERENCES "+
	    		ANAK_TABLE_NAME+"("+COLUMN_ID_ANAK+"),"+
	    		"FOREIGN KEY("+COLUMN_MONITORING_PELANGGARAN+") REFERENCES "+
	    		ANAK_TABLE_NAME+"("+COLUMN_ID_MONITORING+"))";
	    
	    private static final String CREATE_MONITORING = 
	    		"CREATE TABLE IF NOT EXISTS "+
	    		MONITORING_TABLE_NAME+" ("+
	    		COLUMN_ID_MONITORING+" VARCHAR(10) PRIMARY KEY,"+
	    		COLUMN_ANAK_MONITORING+ " VARCHAR(10),"+
	    		COLUMN_LATITUDE_MONITORING+" VARCHAR(50),"+
	    		COLUMN_LONGITUDE_MONITORING+" VARCHAR(50),"+
	    		COLUMN_STATUS_MONITORING+" INTEGER,"+
	    		COLUMN_DATE_MULAI_MONITORING+" INTEGER,"+
	    		COLUMN_DATE_SELESAI_MONITORING+" INTEGER)";
	    
	}
	
	private DatabaseHelper mDatabaseHelper = null;
	
	private static final String ANAK_TABLE_NAME 	= 
    		"anak";
    private static final String COLUMN_ID_ANAK 		= "anak_id";
    private static final String COLUMN_NAMA_ANAK	= "anak_nama";
    private static final String COLUMN_NO_HP_ANAK	= "anak_no_hp";
    
    private static final String PELANGGARAN_TABLE_NAME			=
    		"pelanggaran";
    private static final String COLUMN_ID_PELANGGARAN			= "pelanggaran_id";
    private static final String COLUMN_ANAK_PELANGGARAN			= "pelanggaran_anak";
    private static final String COLUMN_MONITORING_PELANGGARAN	= "pelanggaran_monitoring";
    private static final String COLUMN_DATE_PELANGGARAN			= "pelanggaran_waktu";
    private static final String COLUMN_LONGITUDE_PELANGGARAN	= "pelanggaran_longitude";
    private static final String COLUMN_LATITUDE_PELANGGARAN		= "pelanggaran_latitude";

    private static final String MONITORING_TABLE_NAME			= 
    		"monitoring";
    private static final String COLUMN_ID_MONITORING			= "monitoring_id";
    private static final String COLUMN_ANAK_MONITORING			= "monitoring_anak";
    private static final String COLUMN_LONGITUDE_MONITORING 	= "monitoring_longitude";
    private static final String COLUMN_LATITUDE_MONITORING		= "monitoring_latitude";
    private static final String COLUMN_STATUS_MONITORING		= "monitoring_status";
    private static final String COLUMN_DATE_MULAI_MONITORING	= "monitoring_waktu_mulai";
    private static final String COLUMN_DATE_SELESAI_MONITORING	= "monitoring_waktu_selesai";

}
