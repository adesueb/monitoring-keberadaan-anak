package org.ade.monitoring.keberadaan.storage;


import java.util.List;

import org.ade.monitoring.keberadaan.entity.Anak;
import org.ade.monitoring.keberadaan.entity.DataMonitoring;
import org.ade.monitoring.keberadaan.entity.Pelanggaran;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseManager {

	
	public DatabaseManager (Context context) { 
		mDatabaseHelper = new DatabaseHelper(context);
	}
	  
	  
	public void addDataMonitoring( DataMonitoring dataMonitoring ){
		
	}
	
	private void addAnak(Anak anak){
		ContentValues cv = new ContentValues();
		cv.put(COLUMN_ID_ANAK, anak.getIdAnak());
		cv.put(COLUMN_NAMA_ANAK, anak.getNamaAnak());
		cv.put(COLUMN_NO_HP_ANAK, anak.getNoHpAnak());
		long result = getDb().insert(ANAK_TABLE_NAME, null, cv);
		
		if(result <=0 || anak.getPelanggarans()==null)return;
		addPelanggarans(anak.getPelanggarans());
	}
	
	private void addPelanggarans(List<Pelanggaran> pelanggarans){
		for(Pelanggaran pelanggaran:pelanggarans){
			addPelanggaran(pelanggaran);
		}
	}
	
	private void addPelanggaran(Pelanggaran pelanggaran){
		ContentValues cv = new ContentValues();
		cv.put(COLUMN_ID_PELANGGARAN, pelanggaran.getIdPelanggaran());
		cv.put(COLUMN_DATE_PELANGGARAN, pelanggaran.getWaktuPelanggaran());
		cv.put(COLUMN_LATITUDE_PELANGGARAN, pelanggaran.getLokasi().getlatitude());
		cv.put(COLUMN_LONGITUDE_PELANGGARAN, pelanggaran.getLokasi().getLongitude());
		getDb().insert(PELANGGARAN_TABLE_NAME, null, cv);
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
	    		COLUMN_DATE_PELANGGARAN+" INTEGER,"+
	    		COLUMN_LATITUDE_PELANGGARAN+" VARCHAR(50),"+
	    		COLUMN_LONGITUDE_PELANGGARAN+" VARCHAR(50),"+
	    		"FOREIGN KEY("+COLUMN_ANAK_PELANGGARAN+") REFERENCES "+
	    		ANAK_TABLE_NAME+"("+COLUMN_ID_ANAK+"))";
	    
	    private static final String CREATE_MONITORING = 
	    		"CREATE TABLE IF NOT EXISTS "+
	    		MONITORING_TABLE_NAME+" ("+
	    		COLUMN_ID_MONITORING+" VARCHAR(10) PRIMARY KEY,"+
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
    private static final String COLUMN_DATE_PELANGGARAN			= "pelanggaran_waktu";
    private static final String COLUMN_LONGITUDE_PELANGGARAN	= "pelanggaran_longitude";
    private static final String COLUMN_LATITUDE_PELANGGARAN		= "pelanggaran_latitude";

    private static final String MONITORING_TABLE_NAME			= 
    		"monitoring";
    private static final String COLUMN_ID_MONITORING			= "monitoring_id";
    private static final String COLUMN_LONGITUDE_MONITORING 	= "monitoring_longitude";
    private static final String COLUMN_LATITUDE_MONITORING		= "monitoring_latitude";
    private static final String COLUMN_STATUS_MONITORING		= "monitoring_status";
    private static final String COLUMN_DATE_MULAI_MONITORING	= "monitoring_waktu_mulai";
    private static final String COLUMN_DATE_SELESAI_MONITORING	= "monitoring_waktu_selesai";

}
