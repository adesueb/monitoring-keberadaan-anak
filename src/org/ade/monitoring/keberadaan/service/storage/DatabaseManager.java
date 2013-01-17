package org.ade.monitoring.keberadaan.service.storage;


import java.util.ArrayList;
import java.util.List;

import org.ade.monitoring.keberadaan.entity.Anak;
import org.ade.monitoring.keberadaan.entity.DataMonitoring;
import org.ade.monitoring.keberadaan.entity.DateMonitoring;
import org.ade.monitoring.keberadaan.entity.DayMonitoring;
import org.ade.monitoring.keberadaan.entity.LogMonak;
import org.ade.monitoring.keberadaan.entity.Lokasi;
import org.ade.monitoring.keberadaan.entity.Pelanggaran;
import org.ade.monitoring.keberadaan.util.IDGenerator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DatabaseManager {

	
	public DatabaseManager (Context context) { 
		this.context = context;
		mDatabaseHelper = new DatabaseHelper(context);
	}
	  
	
	// get.................................................................
	
	
	// get (laporan).......................................................
	
	public List<Anak> getAnaksWithoutPelanggarans(){
		Cursor cursor = actionQuery("select * " +
	    		"FROM "+ANAK_TABLE_NAME+" m, "+PELANGGARAN_TABLE_NAME+" p " +
	    		"WHERE m."+COLUMN_ID_ANAK+" != p."+COLUMN_ANAK_PELANGGARAN);
		List<Anak> anaks = getAnaksFromCursor(cursor, true, true);
		return anaks;
	}
	
	public List<DataMonitoring> getDataMonitoringWithoutPelanggarans(){
		
		Cursor cursor = actionQuery(
				"select * " +
	    		"FROM "+MONITORING_TABLE_NAME+" m, "+PELANGGARAN_TABLE_NAME+" p " +
	    		"WHERE m."+COLUMN_ID_MONITORING+" != p."+COLUMN_MONITORING_PELANGGARAN);
		
		List<DataMonitoring> dataMonitorings = getDataMonitoringsFromCursor(cursor, true, true);
		
		return dataMonitorings;
	}
	//.....................................................................
	
	// get last id.........................................................
	public String getLasIdAnak(){
		Cursor cursor = 
				actionQuery("SELECT "+COLUMN_ID+" from "+ANAK_TABLE_NAME+" order by "+COLUMN_ID+" DESC limit 1");
		String id = getLastIdFromCursor(cursor);
		if(cursor!=null){
			cursor.close();
		}
		return id;
	}
	public String getLastIDLokasi(){
		Cursor cursor = 
				actionQuery("SELECT "+COLUMN_ID+" from "+LOCATION_TABLE_NAME+" order by "+COLUMN_ID_LOCATION+" DESC limit 1");
		String id = getLastIdFromCursor(cursor);
		if(cursor!=null){
			cursor.close();
		}
		
		return id;
	}
	public String getLastIdMonitoring(){
		Cursor cursor = 
				actionQuery("SELECT "+COLUMN_ID+" from "+MONITORING_TABLE_NAME+" order by "+COLUMN_ID+" DESC limit 1");
		String id = getLastIdFromCursor(cursor);
		if(cursor!=null){
			cursor.close();
		}
		
		return id;
	}
	public String getLastIdPelanggaran(){
		Cursor cursor = 
				actionQuery("SELECT "+COLUMN_ID+" from "+PELANGGARAN_TABLE_NAME+" order by "+COLUMN_ID+" DESC limit 1");
		if(cursor!=null){
			cursor.close();
		}
		String id = getLastIdFromCursor(cursor);
		return id;
	}
	//.....................................................................
	
	public List<Lokasi> getAllLokasi(){
		Cursor cursor = actionQuery(LOCATION_TABLE_NAME,null,null);
		if(cursor!=null && cursor.getCount()>0){
			List<Lokasi> lokasis = getAllLokasiFromCursor(cursor);
			if(cursor!=null){
				cursor.close();
			}
			return lokasis;
		}
		if(cursor!=null){
			cursor.close();
		}
		return null;
	}
	
	public List<Anak> getAllAnak(boolean withPelanggaran, boolean withMonitoring){
		Cursor cursor = actionQuery(ANAK_TABLE_NAME, null, null);
		if(cursor!=null && cursor.getCount()>0){
			List<Anak> anaks = getAnaksFromCursor(cursor, withPelanggaran, withMonitoring);
			if(cursor!=null){
				cursor.close();
			}
			return anaks;
		}
		if(cursor!=null){
			cursor.close();
		}
		return null;
	}
	
	public List<LogMonak> getAllLogMonak(Anak anak){
		List<LogMonak> logs = null;
		
		Cursor cursor = actionQuery(LOG_TABLE_NAME, null, COLUMN_ANAK_LOG+"='"+anak.getIdAnak()+"'");
		if(cursor!=null && cursor.getCount()>0){
			logs = getAllLogMonakFromCursor(cursor);
		}
		if(cursor!=null){
			cursor.close();
		}
		return logs;
	}
	
	public Anak getAnakById(String idAnak, boolean withPelanggaran, boolean withMonitoring){
		Cursor cursor = actionQuery(ANAK_TABLE_NAME, null,COLUMN_ID_ANAK+"='"+idAnak+"'");
		if(cursor!=null && cursor.getCount()>0){			
			Anak anak = getAnakFromCursor(cursor, withPelanggaran, withMonitoring);
			if(cursor!=null){
				cursor.close();
			}
			return anak;
		}else{
			if(cursor!=null){
				cursor.close();
			}
			return null;
		}
	}
	
	
	public List<DataMonitoring> getAllDataMonitorings
		(boolean withAnak, boolean withWaktuMonitoring){
		Cursor cursor = actionQuery(MONITORING_TABLE_NAME, null,null);
		if(cursor != null && cursor.getCount()>0){
			List<DataMonitoring> dataMonitorings = 
					getDataMonitoringsFromCursor(cursor, withAnak, withWaktuMonitoring);;
			if(cursor!=null){
				cursor.close();
			}
			return dataMonitorings;
		}else{
			if(cursor!=null){
				cursor.close();
			}
			return null;
		}
		
	}
	
	public DataMonitoring getDataMonitoringByIdMonitoring
		(String idDataMonitoring, boolean withAnak, boolean withWaktuMonitoring){
		Cursor cursor = actionQuery(MONITORING_TABLE_NAME, null, COLUMN_ID_MONITORING+"='"+idDataMonitoring+"'");
		if(cursor!=null && cursor.getCount()>0){
			DataMonitoring dataMonitoring = 
					getDataMonitoringFromCursor(cursor, withAnak, withWaktuMonitoring);
			if(cursor!=null){
				cursor.close();
			}
			return dataMonitoring;
		}else{
			if(cursor!=null){
				cursor.close();
			}
			return null;
		}
	}
		
	public List<DataMonitoring> getDataMonitoringsByAnak(String idAnak){
		Cursor cursor = actionQuery(MONITORING_TABLE_NAME, null, COLUMN_ANAK_MONITORING+"='"+idAnak+"'");
		if(cursor!=null && cursor.getCount()>0){
			List<DataMonitoring> dataMonitorings = getDataMonitoringsFromCursor(cursor, false, true);
			if(cursor!=null){
				cursor.close();
			}
			return dataMonitorings;
		}else{
			if(cursor!=null){
				cursor.close();
			}
			return null;
		}
	}
	
	public List<Pelanggaran> getDataPelanggaransByAnak(String idAnak){
		Cursor cursor = actionQuery(PELANGGARAN_TABLE_NAME, null, COLUMN_ANAK_PELANGGARAN+"='"+idAnak+"'");
		if(cursor!=null && cursor.getCount()>0){
			List<Pelanggaran> pelanggarans = getPelanggaransFromCursor(cursor, false, false);
			if(cursor!=null){
				cursor.close();
			}
			return pelanggarans;
		}else{
			if(cursor!=null){
				cursor.close();
			}
			return null;
		}
	}
	
	public List<Pelanggaran> getAllDataPelanggarans(boolean withAnak, boolean withMonitoring){
		Cursor cursor = actionQuery(PELANGGARAN_TABLE_NAME, null, null);
		if(cursor!=null && cursor.getCount()>0){
			List<Pelanggaran> pelanggarans = 
					getPelanggaransFromCursor(cursor, withAnak, withMonitoring);
			if(cursor!=null){
				cursor.close();
			}
			return pelanggarans;
		}else{
			if(cursor!=null){
				cursor.close();
			}
			return null;
		}
	}
	
	public Lokasi getLokasiByIdLokasi(String idLokasi){

		Log.d("database manager", "dapetin lokasi dari id");

		if(idLokasi!=null&&!idLokasi.equals("")){

			Log.d("database manager", "dapetin lokasi dari cursor dengan id : "+idLokasi);

			Cursor cursor = 
					actionQuery(LOCATION_TABLE_NAME, null, COLUMN_ID_LOCATION+"='"+idLokasi+"'");

			Log.d("database manager", "dapetin lokasi dari cursor dengan count : "+cursor.getCount());
			
			if(cursor!=null && cursor.getCount()>0){
				cursor.moveToFirst();
				Lokasi lokasi = 
						getLokasiFromCursor(cursor);
				if(cursor!=null){
					cursor.close();
				}
				return lokasi;
			}else{
				if(cursor!=null){
					cursor.close();
				}
				return null;
			}
		}
		return null;
	}
	
	public List<DateMonitoring> getTanggalMonitoringsByMonitoring(String idMonitoring, boolean withDataMonitoring){
		Cursor cursor = 
				actionQuery(DATE_MONITORING_TABLE_NAME, null, COLUMN_MONITORING_DATE_MONITORING+"='"+idMonitoring+"'");
		if(cursor!=null && cursor.getCount()>0){
			List<DateMonitoring> dateMonitoring = 
					getTanggalsMonitoringsFromCursor(cursor, withDataMonitoring);
			if(cursor!=null){
				cursor.close();
			}
			return dateMonitoring;
		}else{
			if(cursor!=null){
				cursor.close();
			}
			return null;
		}
	}
	
	public List<DateMonitoring> getAllTanggalMonitorings(boolean withDataMonitoring){
		Cursor cursor = 
				actionQuery(DATE_MONITORING_TABLE_NAME, null, null);
		if(cursor!=null && cursor.getCount()>0){
			List<DateMonitoring> dateMonitorings = getTanggalsMonitoringsFromCursor(cursor, withDataMonitoring);
			if(cursor!=null){
				cursor.close();
			}
			return dateMonitorings;
		}else{
			if(cursor!=null){
				cursor.close();
			}
			return null;
		}
	}
	
	public List<DayMonitoring> getHariMonitoringsByMonitoring(String idMonitoring, boolean withDataMonitoring){
		Cursor cursor = 
				actionQuery(DAY_MONITORING_TABLE_NAME, null, COLUMN_MONITORING_DAY_MONITORING+"='"+idMonitoring+"'");
		if(cursor!=null && cursor.getCount()>0){
			List<DayMonitoring> dayMonitorings = getHarisMonitoringsFromCursor(cursor, withDataMonitoring);
			if(cursor!=null){
				cursor.close();
			}
			return dayMonitorings;
		}else{
			if(cursor!=null){
				cursor.close();
			}
			return null;
		}
	}
	
	public List<DayMonitoring> getAllHariMonitorings(boolean withDataMonitoring){
		Cursor cursor = 
				actionQuery(DAY_MONITORING_TABLE_NAME, null, null);
		if(cursor!=null && cursor.getCount()>0){
			List<DayMonitoring> dayMonitorings = getHarisMonitoringsFromCursor(cursor, withDataMonitoring);
			if(cursor!=null){
				cursor.close();
			}
			return dayMonitorings;
		}else{
			if(cursor!=null){
				cursor.close();
			}
			return null;
		}
	}
	
	//...................................................................
	
	// delete............................................................
	
	public void deleteLogByLokasi(Lokasi lokasi){
		getDb().delete
			(LOG_TABLE_NAME, COLUMN_LOCATION_LOG+"='"+lokasi.getId()+"'", null);
	}
	
	public void deleteLog(LogMonak logMonak){
		deleteLokasi(logMonak.getLokasi());
		getDb().delete
			(LOG_TABLE_NAME, "_id='"+logMonak.getIdLog()+"'", null);
		
	}
	
	public void deleteLokasi(Lokasi lokasi){
		long result = getDb().delete
			(LOCATION_TABLE_NAME, COLUMN_ID_LOCATION+"='"+lokasi.getId()+"'", null);
		if(result>0){
			deleteLogByLokasi(lokasi);	
		}
		
	}
	
	public void deleteAllLokasi(){
		getDb().delete
			(LOCATION_TABLE_NAME, null, null);
	}
	
	public void deleteDateMonitoringByIdIdDate(String id){

		getDb().delete
			(DATE_MONITORING_TABLE_NAME, 
					"_id='"+id+"'", null);
	}
	
	public void deleteDayMonitoringByIdDay(String id){
		getDb().delete
			(DAY_MONITORING_TABLE_NAME, 
					"_id='"+id+"'", null);
	}
	
	public void deleteDateMonitoringByIdMonitoring(String idMonitoring){

		getDb().delete
			(DATE_MONITORING_TABLE_NAME, 
					COLUMN_MONITORING_DATE_MONITORING+"='"+idMonitoring+"'", null);
	}
	
	public void deleteDayMonitoringByIdMonitoring(String idMonitoring){
		getDb().delete
			(DAY_MONITORING_TABLE_NAME, 
				COLUMN_MONITORING_DAY_MONITORING+"='"+idMonitoring+"'", null);
	}
	
	public void deleteAllDataMonitoring(){
		getDb().delete(MONITORING_TABLE_NAME, null, null);
	}
	
	public void deleteAllAnak(){
		getDb().delete(ANAK_TABLE_NAME, null, null);
	}
	
	public void deletePelanggaran(){
		getDb().delete(PELANGGARAN_TABLE_NAME, null, null);
	}
	
	public void deleteDataMonitoring(DataMonitoring dataMonitoring){
		getDb().delete
			(MONITORING_TABLE_NAME, 
					COLUMN_ID_MONITORING+"='"+dataMonitoring.getIdMonitoring()+"'", null);
		List<DateMonitoring> tanggals = dataMonitoring.getTanggals();
		if(tanggals!=null){
			deleteDateMonitoringByIdMonitoring(dataMonitoring.getIdMonitoring());
		}
		
		List<DayMonitoring> haris = dataMonitoring.getHaris();
		if(haris!=null){
			deleteDayMonitoringByIdMonitoring(dataMonitoring.getIdMonitoring());
		}
		
		Lokasi lokasi = dataMonitoring.getLokasi();
		if(lokasi!=null){
			deleteLokasi(lokasi);
		}
	}
	
	public void deleteAnak(Anak anak){
		getDb().delete
		(ANAK_TABLE_NAME, 
				COLUMN_ID_ANAK+"='"+anak.getIdAnak()+"'", null);
		Lokasi lokasi = anak.getLokasi();
		if(lokasi!=null){
			deleteLokasi(lokasi);
		}
	}
	
	public void deletePelanggaran(Pelanggaran pelanggaran){
		getDb().delete
			(PELANGGARAN_TABLE_NAME, 
				COLUMN_ID_PELANGGARAN+"='"+pelanggaran.getIdPelanggaran()+"'", null);
		Lokasi lokasi = pelanggaran.getLokasi();
		if(lokasi!=null){
			deleteLokasi(lokasi);
		}
	}
	
	//...................................................................
	
	// update............................................................
	public void updateDateMonitoring(DateMonitoring tanggalMonitoring){
		ContentValues cv = new ContentValues();
		cv.put(COLUMN_DATE_DATE_MONITORING, tanggalMonitoring.getDate());
		getDb().update(DATE_MONITORING_TABLE_NAME, cv, "_id='"+tanggalMonitoring.getId()+"'", null);
	}
	
	public void updateDayMonitoring(DayMonitoring hariMonitoring){
		ContentValues cv = new ContentValues();
		cv.put(COLUMN_DAY_DAY_MONITORING, hariMonitoring.getHari());
		getDb().update(DAY_MONITORING_TABLE_NAME, cv, "_id='"+hariMonitoring.getId()+"'", null);
	}
	
	public void updateLokasi(Lokasi lokasi){
		ContentValues cv = new ContentValues();
		cv.put(COLUMN_ID_LOCATION, lokasi.getId());
		cv.put(COLUMN_LATITUDE, lokasi.getlatitude());
		cv.put(COLUMN_LONGITUDE, lokasi.getLongitude());
		if(lokasi.getTime()!=0)cv.put(COLUMN_TIME, lokasi.getTime());
		long result = getDb().update(LOCATION_TABLE_NAME, cv, COLUMN_ID_LOCATION+"='"+lokasi.getId()+"'", null);
		if(result>0){
			Log.d("database manager", "mengupdate lokasi berhasil..");
		}else{
			result = getDb().insert(LOCATION_TABLE_NAME,null, cv);
			if(result>0){
				Log.d("database manager", "lokasi di insertkan..");
			}
		}
	}
	
	public void updateDataMonitoring(DataMonitoring dataMonitoring){
		ContentValues cv = new ContentValues();
		cv.put(COLUMN_KET_MONITORING, dataMonitoring.getKeterangan());
		cv.put(COLUMN_STATUS_MONITORING, dataMonitoring.getStatus());
		cv.put(COLUMN_TOLERANCY_MONITORING, dataMonitoring.getTolerancy());
		cv.put(COLUMN_DATE_MULAI_MONITORING, dataMonitoring.getWaktuMulai());
		cv.put(COLUMN_DATE_SELESAI_MONITORING, dataMonitoring.getWaktuSelesai());
		long result = getDb().update(MONITORING_TABLE_NAME, cv, COLUMN_ID_MONITORING+"='"+dataMonitoring.getIdMonitoring()+"'", null);
		if(result>0){
			List<DateMonitoring> tanggals = dataMonitoring.getTanggals();
			if(tanggals!=null){
				for(DateMonitoring tanggal : tanggals){
					updateDateMonitoring(tanggal);
				}
			}
			
			List<DayMonitoring> haris = dataMonitoring.getHaris();
			if(tanggals!=null){
				for(DayMonitoring hari : haris){
					updateDayMonitoring(hari);
				}
			}
			if(dataMonitoring.getLokasi()!=null){
				updateLokasi(dataMonitoring.getLokasi());
			}
			
		}
	}
		
	public void updateAnak(Anak anak){
		
		ContentValues cv = new ContentValues();
		
		if(anak.getNamaAnak()!=null && !anak.getNamaAnak().equals("")){
			cv.put(COLUMN_NAMA_ANAK, anak.getNamaAnak());	
		}
		
		if(anak.getNoHpAnak()!=null && !anak.getNoHpAnak().equals("")){
			cv.put(COLUMN_NO_HP_ANAK, anak.getNoHpAnak());	
		}
		
		Log.d("database manager", "isi lokasi dari anak : "+anak.getLokasi().getId());
		if(anak.getLokasi()!=null){
			cv.put(COLUMN_LAST_LOCATION_ANAK, anak.getLokasi().getId());	
		}

		long result = getDb().update(ANAK_TABLE_NAME, cv, COLUMN_ID_ANAK+"='"+anak.getIdAnak()+"'", null);
		
		Log.d("database manager", "result : "+result);
		
		if(result>0 && anak.getLokasi()!=null){
			updateLokasi(anak.getLokasi());
		}
		
	}
	
	public void updateLokasiAnak(Anak anakParam){
		Anak anak = getAnakById(anakParam.getIdAnak(), false, false);
		Lokasi lokasi = anak.getLokasi();
		if(lokasi!=null){
			deleteLokasi(lokasi);
		}
		
		Lokasi lokasiParam = anakParam.getLokasi();
		if(lokasiParam==null){
			return;
		}
		
		if(lokasiParam.getId()==null || lokasiParam.getId().equals("")){
			IDGenerator idGenerator = new IDGenerator(context, this);
			anakParam.getLokasi().setId(idGenerator.getIdLocation());	
		}
		
		addLokasiToAnak(anakParam);
	}

	//...................................................................
	
	// add...............................................................
	
	public void addLokasiToAnak(Anak anak){
		updateAnak(anak);
	}
	
	public void addLocationLog(LogMonak log){
		addLokasi(log.getLokasi());
		ContentValues cv = new ContentValues();
		cv.put(COLUMN_LOCATION_LOG, log.getLokasi().getId());
		cv.put(COLUMN_ANAK_LOG, log.getAnak().getIdAnak());
		long result = getDb().insert(LOG_TABLE_NAME, null, cv);	
		if(result>0){
		}
	}
	
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
			cv.put(COLUMN_DATE_MULAI_MONITORING, dataMonitoring.getWaktuMulai());
			cv.put(COLUMN_DATE_SELESAI_MONITORING, dataMonitoring.getWaktuSelesai());			
			cv.put(COLUMN_STATUS_MONITORING, dataMonitoring.getStatus());
			if(dataMonitoring.getAnak()!=null){
				cv.put(COLUMN_ANAK_MONITORING, dataMonitoring.getAnak().getIdAnak());	
			}
			long result = getDb().insert(MONITORING_TABLE_NAME, null, cv);	
			if(result>0 && dataMonitoring.getTanggals()!=null){
				addTanggalMonitorings(dataMonitoring.getTanggals());
			}
			if(result>0 && dataMonitoring.getHaris()!=null){
				addHariMonitorings(dataMonitoring.getHaris());
			}
			if(result>0){
				if(dataMonitoring.getLokasi()!=null){
					addLokasi(dataMonitoring.getLokasi());
				}
			}
		}
	}
	
	public void addAnak(Anak anak){
		ContentValues cv = new ContentValues();
		cv.put(COLUMN_ID_ANAK, anak.getIdAnak());
		cv.put(COLUMN_ORTU_ANAK, anak.getIdOrtu());
		cv.put(COLUMN_NAMA_ANAK, anak.getNamaAnak());
		cv.put(COLUMN_NO_HP_ANAK, anak.getNoHpAnak());
		long result = getDb().insert(ANAK_TABLE_NAME, null, cv);
		
		if(result <=0 || anak.getPelanggarans()==null){
			
		}else{
			addPelanggarans(anak.getPelanggarans());
		}
		
		if(result <=0 || anak.getLokasi()==null){
			
		}else{
			Log.d("insert_anak", "lokasi dari anak adalah : "+anak.getLokasi().getlatitude()+","+anak.getLokasi().getLongitude());

			addLokasi(anak.getLokasi());
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
			
			if(pelanggaran.getDataMonitoring()!=null){
				cv.put(COLUMN_MONITORING_PELANGGARAN, pelanggaran.getDataMonitoring().getIdMonitoring());
			}
			if(pelanggaran.getAnak()!=null){
				cv.put(COLUMN_ANAK_PELANGGARAN, pelanggaran.getAnak().getIdAnak());
			}
			long result = getDb().insert(PELANGGARAN_TABLE_NAME, null, cv);
			if(result>0){
				if(pelanggaran.getLokasi()!=null){
					addLokasi(pelanggaran.getLokasi());
				}
			}
		}
	}
	
	public void addLokasi(Lokasi lokasi){
		if(lokasi!=null){
			ContentValues cv = new ContentValues();
			cv.put(COLUMN_ID_LOCATION, lokasi.getId());
			cv.put(COLUMN_LATITUDE, lokasi.getlatitude());
			cv.put(COLUMN_LONGITUDE, lokasi.getLongitude());
			cv.put(COLUMN_TIME, lokasi.getTime());
			
			long result = getDb().insert(LOCATION_TABLE_NAME, null, cv);
			if(result>0){
				Log.d("DatabaseManager", "insert anak successfully with id : "+lokasi.getId());
			}
		}
	}
	
	public void addTanggalMonitorings(List<DateMonitoring> tanggalMonitorings){
		if(tanggalMonitorings!=null){
			for(DateMonitoring tanggalMonitoring:tanggalMonitorings){
				addTanggalMonitoring(tanggalMonitoring);
			}
		}
		
	}
	
	public void addTanggalMonitoring(DateMonitoring tanggalMonitoring){
		if(tanggalMonitoring!=null){
			ContentValues cv = new ContentValues();
			cv.put(COLUMN_DATE_DATE_MONITORING, tanggalMonitoring.getDate());
			cv.put(COLUMN_MONITORING_DATE_MONITORING, tanggalMonitoring.getId());
			
			getDb().insert(DATE_MONITORING_TABLE_NAME, null, cv);	
		}
	}
	
	public void addHariMonitorings(List<DayMonitoring> hariMonitorings){
		if(hariMonitorings!=null){
			for(DayMonitoring hariMonitoring:hariMonitorings){
				addHariMonitoring(hariMonitoring);
			}
		}
		
	}
	
	public void addHariMonitoring(DayMonitoring dayMonitoring){
		if(dayMonitoring!=null){
			ContentValues cv = new ContentValues();
			cv.put(COLUMN_DAY_DAY_MONITORING, dayMonitoring.getHari());
			cv.put(COLUMN_MONITORING_DAY_MONITORING, dayMonitoring.getId());
			
			getDb().insert(DAY_MONITORING_TABLE_NAME, null, cv);	
		}
	}
	
	
	
	//............................................................................
	
	//get from Cursor.............................................................
	
	private String getLastIdFromCursor(Cursor cursor){
		if(cursor.moveToFirst()){
			String result = cursor.getString(cursor.getColumnIndex(COLUMN_ID));
			cursor.close();
			return result;
		}
		return "";
	}
	
	private List<LogMonak> getAllLogMonakFromCursor(Cursor cursor){
		List<LogMonak> logs = new ArrayList<LogMonak>();
		if(cursor.moveToFirst()){
			do{
				logs.add(getLogMonakFromCursor(cursor));
			}while(cursor.moveToNext());
		}
		return logs;
	}
	
	private List<Lokasi> getAllLokasiFromCursor(Cursor cursor){
		List<Lokasi> lokasis = new ArrayList<Lokasi>();
		if(cursor.moveToFirst()){
			do{
				lokasis.add(getLokasiFromCursor(cursor));
			}while(cursor.moveToNext());
		}
		return lokasis;
	}
	
	private List<Anak> getAnaksFromCursor(Cursor cursor, boolean withPelanggaran, boolean withMonitoring){
		List<Anak> anaks =  new ArrayList<Anak>();
		if(cursor.moveToFirst()){
			do{	anaks.add(getAnakFromCursor(cursor, withPelanggaran, withMonitoring));
			}while(cursor.moveToNext());
		}
		return anaks;
	}
	
	private LogMonak getLogMonakFromCursor(Cursor cursor){
		if(cursor!=null && cursor.getCount()>0){
			LogMonak log = new LogMonak();
			int indexIdLog = cursor.getColumnIndex("_id");
			int indexIdAnak = cursor.getColumnIndex(COLUMN_ANAK_LOG);
			int indexIdLokasi = cursor.getColumnIndex(COLUMN_LOCATION_LOG);
			
			log.setLokasi(getLokasiByIdLokasi(cursor.getString(indexIdLokasi)));
			log.setAnak(getAnakById(cursor.getString(indexIdAnak),false, false));
			log.setIdLog(""+cursor.getInt(indexIdLog));
			
			return log;
		}
		return null;
	}
	
	private Anak getAnakFromCursor(Cursor cursor, boolean withPelanggaran, boolean withMonitoring){
		if(cursor!=null && cursor.getCount()>0){
			Anak anak = new Anak();
			int indexIdAnak 	= cursor.getColumnIndex(COLUMN_ID_ANAK);
			int indexOrtuAnak	= cursor.getColumnIndex(COLUMN_ORTU_ANAK);
			int indexNamaAnak	= cursor.getColumnIndex(COLUMN_NAMA_ANAK);
			int indexPhoneAnak	= cursor.getColumnIndex(COLUMN_NO_HP_ANAK);
			int indexLocationAnak 	= cursor.getColumnIndex(COLUMN_LAST_LOCATION_ANAK);
			
			anak.setIdAnak(cursor.getString(indexIdAnak));
			anak.setIdOrtu(cursor.getString(indexOrtuAnak));
			anak.setNamaAnak(cursor.getString(indexNamaAnak));
			anak.setNoHpAnak(cursor.getString(indexPhoneAnak));
			Log.d("database manager", "get lokasi dr anak dgn id lokasi : "+cursor.getString(indexLocationAnak));
			anak.setLokasi(getLokasiByIdLokasi(cursor.getString(indexLocationAnak)));
			if(withPelanggaran){
				getDataPelanggaransByAnak(cursor.getString(indexIdAnak));
			}
			if(withMonitoring){
				anak.setDataMonitorings(getDataMonitoringsByAnak(cursor.getString(indexIdAnak)));
			}
			return anak;
		}
		return null;
		
	}
	
	
	private List<DataMonitoring> getDataMonitoringsFromCursor
		(Cursor cursor, boolean withAnak, boolean withWaktuMonitoring){
		List <DataMonitoring> dataMonitorings = new ArrayList<DataMonitoring>();
		if(cursor.moveToFirst()){
			do{
				 dataMonitorings.add
				 	(getDataMonitoringFromCursor(cursor, withAnak, withWaktuMonitoring));
			}while(cursor.moveToNext());
		}
		return dataMonitorings;
	}
	
	private DataMonitoring getDataMonitoringFromCursor
		(Cursor cursor, boolean withAnak, boolean withWaktuMonitoring){
		if(cursor!=null && cursor.getCount()>0){
			
			DataMonitoring dataMonitoring = new DataMonitoring();
			 
			int indexIdMonitoring 			= cursor.getColumnIndex(COLUMN_ID_MONITORING);
			int indexKetMonitoring			= cursor.getColumnIndex(COLUMN_KET_MONITORING);
			int indexLocationMonitoring 	= cursor.getColumnIndex(COLUMN_LOCATION_MONITORING);
			int indexDateMulaiMonitoring	= cursor.getColumnIndex(COLUMN_DATE_MULAI_MONITORING);
			int indexDateSelesaiMonitoring	= cursor.getColumnIndex(COLUMN_DATE_SELESAI_MONITORING);
			int indexStatusMonitoring 		= cursor.getColumnIndex(COLUMN_STATUS_MONITORING);
			int indexTolerancy				= cursor.getColumnIndex(COLUMN_TOLERANCY_MONITORING);
			
			dataMonitoring.setLokasi(getLokasiByIdLokasi(cursor.getString(indexLocationMonitoring)));
			dataMonitoring.setIdMonitoring(cursor.getString(indexIdMonitoring));
			dataMonitoring.setKeterangan(cursor.getString(indexKetMonitoring));
			dataMonitoring.setWaktuMulai(cursor.getLong(indexDateMulaiMonitoring));
			dataMonitoring.setWaktuSelesai(cursor.getLong(indexDateSelesaiMonitoring));
			dataMonitoring.setStatus(cursor.getInt(indexStatusMonitoring));
			dataMonitoring.setTolerancy(cursor.getInt(indexTolerancy));
			if(withWaktuMonitoring){
				dataMonitoring.setTanggals
					(getTanggalMonitoringsByMonitoring
							(dataMonitoring.getIdMonitoring(), false));
				dataMonitoring.setHaris
					(getHariMonitoringsByMonitoring
							(dataMonitoring.getIdMonitoring(), false));
			}
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
			int indexLocationPelanggaran 	= cursor.getColumnIndex(COLUMN_LOCATION_PELANGGARAN);
			int indexDatePelanggaran 		= cursor.getColumnIndex(COLUMN_DATE_PELANGGARAN);
				pelanggaran.setIdPelanggaran(cursor.getString(indexIdPelanggaran));
			pelanggaran.setWaktuPelanggaran(cursor.getLong(indexDatePelanggaran));
			pelanggaran.setLokasi(getLokasiByIdLokasi(cursor.getString(indexLocationPelanggaran)));
			if(withAnak){
				int indexAnakPelanggaran		= cursor.getColumnIndex(COLUMN_ANAK_PELANGGARAN);
				pelanggaran.setAnak(getAnakById(cursor.getString(indexAnakPelanggaran), false, false));
			}
			if(withMonitoring){
				int indexMonitoringPelanggaran	= cursor.getColumnIndex(COLUMN_MONITORING_PELANGGARAN);
				pelanggaran.setDataMonitoring(
						getDataMonitoringByIdMonitoring(cursor.getString(indexMonitoringPelanggaran), false, true));
			}
			return pelanggaran;
		}
		return null;
		
	}
	
	public List<DateMonitoring> getTanggalsMonitoringsFromCursor(Cursor cursor, boolean withDataMonitoring){
		List<DateMonitoring> waktuMonitorings = new ArrayList<DateMonitoring>();
		if(cursor.moveToFirst()){
			do{
				 waktuMonitorings.add(getTanggalMonitoringFromCursor(cursor, withDataMonitoring));
			}while(cursor.moveToNext());
		}
		return waktuMonitorings;
	}
	
	public List<DayMonitoring> getHarisMonitoringsFromCursor(Cursor cursor, boolean withDataMonitoring){
		List<DayMonitoring> hariMonitorings = new ArrayList<DayMonitoring>();
		if(cursor.moveToFirst()){
			do{
				 hariMonitorings.add(getHariMonitoringFromCursor(cursor, withDataMonitoring));
			}while(cursor.moveToNext());
		}
		return hariMonitorings;
	}
	
	public Lokasi getLokasiFromCursor(Cursor cursor){
		Log.d("database manager", "dapetin lokasi dari cursor");
		if(cursor!=null && cursor.getCount()>0){
			Log.d("database manager", "isi cursor tidak kosong");
			Lokasi lokasi = new Lokasi();
			int indexIdLokasi	= cursor.getColumnIndex(COLUMN_ID_LOCATION);
			int indexLatitude 	= cursor.getColumnIndex(COLUMN_LATITUDE);
			int indexLongitude 	= cursor.getColumnIndex(COLUMN_LONGITUDE);
			
			lokasi.setId(cursor.getString(indexIdLokasi));
			lokasi.setLatitude(cursor.getDouble(indexLatitude));
			lokasi.setLongitude(cursor.getDouble(indexLongitude));
			
			return lokasi;
		}
		return null;
	}
	
	public DateMonitoring getTanggalMonitoringFromCursor(Cursor cursor, boolean withDataMonitoring){
		DateMonitoring dateMonitoring = new DateMonitoring();
		if(cursor!=null && cursor.getCount()>0){
			int indexId		= cursor.getColumnIndex("_id");
			int indexDate 	= cursor.getColumnIndex(COLUMN_DATE_DATE_MONITORING);
			
			String idDateMonitoring = cursor.getInt(indexId)+"";
			long tanggalMonitoring = cursor.getInt(indexDate);
			dateMonitoring.setId(idDateMonitoring);
			dateMonitoring.setDate(tanggalMonitoring);
			
			if(withDataMonitoring){
				int indexMonitoring = cursor.getColumnIndex(COLUMN_MONITORING_DATE_MONITORING);
				String idMonitoring = cursor.getString(indexMonitoring);
				dateMonitoring.setDataMonitoring(getDataMonitoringByIdMonitoring(idMonitoring, false, false));
			}
			
			return dateMonitoring;
		}
		return null;
	}
	public DayMonitoring getHariMonitoringFromCursor(Cursor cursor, boolean withDataMonitoring){
		DayMonitoring dayMonitoring = new DayMonitoring();
		if(cursor!=null && cursor.getCount()>0){
			int indexDay 		= cursor.getColumnIndex(COLUMN_DAY_DAY_MONITORING);
			int indexId			= cursor.getColumnIndex("_id");
			String idHari		= cursor.getInt(indexId)+"";
			int hariMonitoring 	= cursor.getInt(indexDay);
			dayMonitoring.setId(idHari);
			dayMonitoring.setHari(hariMonitoring);
			if(withDataMonitoring){
				int indexMonitoring = cursor.getColumnIndex(COLUMN_MONITORING_DAY_MONITORING);
				String idMonitoring = cursor.getString(indexMonitoring);
				dayMonitoring.setDataMonitoring(getDataMonitoringByIdMonitoring(idMonitoring, false, false));
			}
			return dayMonitoring;
		}
		return dayMonitoring;
	}
	//............................................................................
	//method buat melakukan query.................................................
	private Cursor actionQuery(String table, String[]columns, String selection){
	  
		SQLiteDatabase db 	= getDb();
		final Cursor cursor = 
				db.query(table, columns, selection, null, null, null, null, null);
		db = null;
		return cursor;
	}
	
	private Cursor actionQuery(String query){
		SQLiteDatabase db 	= getDb();
		final Cursor cursor = 
				db.rawQuery(query, null);
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
	    	db.execSQL(CREATE_LOCATION);
			db.execSQL(CREATE_PELANGGARAN);
			db.execSQL(CREATE_MONITORING);
			db.execSQL(CREATE_LOG);
			db.execSQL(CREATE_DATE_MONITORING);
			db.execSQL(CREATE_DAY_MONITORING);
	    }
	    
	    private void dropTables(SQLiteDatabase db){
	    	db.execSQL("DROP TABLE IF EXISTS " + ANAK_TABLE_NAME);
	    	db.execSQL("DROP TABLE IF EXISTS " + LOCATION_TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + PELANGGARAN_TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + MONITORING_TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + LOG_TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + DATE_MONITORING_TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + DAY_MONITORING_TABLE_NAME);
			
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
	    private static final int DATABASE_VERSION = 4;
		
	    private static final String CREATE_ANAK = 
	    		"CREATE TABLE IF NOT EXISTS "+
	    		ANAK_TABLE_NAME+" ("+
	    		COLUMN_ID_ANAK+" VARCHAR(10) PRIMARY KEY,"+
	    		COLUMN_LAST_LOCATION_ANAK+" VARCHAR(10),"+
	    		COLUMN_ORTU_ANAK+" VARCHAR(40),"+
	    		COLUMN_NAMA_ANAK+" VARCHAR(100),"+
	    		COLUMN_NO_HP_ANAK+" VARCHAR(50))";
	    
	    private static final String CREATE_PELANGGARAN = 
	    		"CREATE TABLE IF NOT EXISTS "+
	    		PELANGGARAN_TABLE_NAME+" ("+
	    		COLUMN_ID_PELANGGARAN+" VARCHAR(10) PRIMARY KEY,"+
	    		COLUMN_ANAK_PELANGGARAN+" VARCHAR(10),"+
	    		COLUMN_MONITORING_PELANGGARAN+ " VARCHAR(10)," +
	    		COLUMN_DATE_PELANGGARAN+" INTEGER,"+
	    		COLUMN_LOCATION_PELANGGARAN+" VARCHAR(10),"+
	    		"FOREIGN KEY("+COLUMN_ANAK_PELANGGARAN+") REFERENCES "+
	    		ANAK_TABLE_NAME+"("+COLUMN_ID_ANAK+"),"+
	    		"FOREIGN KEY("+COLUMN_MONITORING_PELANGGARAN+") REFERENCES "+
	    		MONITORING_TABLE_NAME+"("+COLUMN_ID_MONITORING+"))";
	    
	    private static final String CREATE_MONITORING = 
	    		"CREATE TABLE IF NOT EXISTS "+
	    		MONITORING_TABLE_NAME+" ("+
	    		COLUMN_ID_MONITORING+" VARCHAR(10) PRIMARY KEY,"+
	    		COLUMN_KET_MONITORING+" VARCHAR(50),"+
	    		COLUMN_ANAK_MONITORING+ " VARCHAR(10),"+
	    		COLUMN_LOCATION_MONITORING+" VARCHAR(10),"+
	    		COLUMN_DATE_MULAI_MONITORING+" INTEGER,"+	    		
	    		COLUMN_DATE_SELESAI_MONITORING+" INTEGER,"+
	    		COLUMN_STATUS_MONITORING+" INTEGER,"+	    		
	    		COLUMN_TOLERANCY_MONITORING+" INTEGER,"+
	    		"FOREIGN KEY("+COLUMN_ANAK_MONITORING+") REFERENCES "+
	    		ANAK_TABLE_NAME+"("+COLUMN_ID_ANAK+"))";
	    
	    private static final String CREATE_LOCATION = 
	    		"CREATE TABLE IF NOT EXISTS "+
	    		LOCATION_TABLE_NAME+" ("+
	    		COLUMN_ID_LOCATION+" VARCHAR(10) PRIMARY KEY,"+	    	
	    		COLUMN_LONGITUDE+" REAL,"+
	    		COLUMN_LATITUDE+" REAL,"+
	    		COLUMN_TIME+" INTEGER,"+
	    		COLUMN_MONITORING_DATE_MONITORING+ " VARCHAR(10),"+			
	    		"FOREIGN KEY("+COLUMN_MONITORING_DATE_MONITORING+") REFERENCES "+
	    		MONITORING_TABLE_NAME+"("+COLUMN_ID_MONITORING+"))";
	    
	    private static final String CREATE_LOG =
	    		"CREATE TABLE IF NOT EXISTS "+
	    		LOG_TABLE_NAME+" ("+
	    		"_id INTEGER PRIMARY KEY,"+
	    		COLUMN_LOCATION_LOG+" VARCHAR(10),"+
	    		COLUMN_ANAK_LOG+" VARCHAR(10))";
	    
	    private static final String CREATE_DATE_MONITORING = 
	    		"CREATE TABLE IF NOT EXISTS "+
	    		DATE_MONITORING_TABLE_NAME+" ("+
	    		"_id INTEGER PRIMARY KEY,"+
	    		COLUMN_DATE_DATE_MONITORING+" INTEGER,"+	    	
	    		COLUMN_MONITORING_DATE_MONITORING+ " VARCHAR(10),"+			
	    		"FOREIGN KEY("+COLUMN_MONITORING_DATE_MONITORING+") REFERENCES "+
	    		MONITORING_TABLE_NAME+"("+COLUMN_ID_MONITORING+"))";
	    
	    private static final String CREATE_DAY_MONITORING = 
	    		"CREATE TABLE IF NOT EXISTS "+
	    		DAY_MONITORING_TABLE_NAME+" ("+	
	    		"_id INTEGER PRIMARY KEY,"+
	    		COLUMN_DAY_DAY_MONITORING+" INTEGER,"+	    	
	    		COLUMN_MONITORING_DAY_MONITORING+ " VARCHAR(10),"+
	    		"FOREIGN KEY("+COLUMN_MONITORING_DATE_MONITORING+") REFERENCES "+
	    		MONITORING_TABLE_NAME+"("+COLUMN_ID_MONITORING+"))";
	    
	}
	
	private final Context context;
	
	private DatabaseHelper mDatabaseHelper = null;
	
	    
	
	private static final String COLUMN_ID 		= "id";
	
	private static final String ANAK_TABLE_NAME 			= 
    		"anak";
    private static final String COLUMN_ID_ANAK 				= "id";
    private static final String COLUMN_ORTU_ANAK			= "ortu";
    private static final String COLUMN_NAMA_ANAK			= "nama";
    private static final String COLUMN_NO_HP_ANAK			= "no_hp";
    private static final String COLUMN_LAST_LOCATION_ANAK 	= "lokasi";
    
    private static final String PELANGGARAN_TABLE_NAME			=
    		"pelanggaran";
    private static final String COLUMN_ID_PELANGGARAN			= "id";
    private static final String COLUMN_ANAK_PELANGGARAN			= "anak";
    private static final String COLUMN_MONITORING_PELANGGARAN	= "monitoring";
    private static final String COLUMN_LOCATION_PELANGGARAN		= "pelanggaran";
    private static final String COLUMN_DATE_PELANGGARAN			= "waktu";

    private static final String MONITORING_TABLE_NAME			= 
    		"monitoring";
    private static final String COLUMN_ID_MONITORING			= "id";
    private static final String COLUMN_KET_MONITORING			= "ket";
    private static final String COLUMN_ANAK_MONITORING			= "anak";
    private static final String COLUMN_LOCATION_MONITORING		= "lokasi";
    private static final String COLUMN_DATE_MULAI_MONITORING	= "mulai";
    private static final String COLUMN_DATE_SELESAI_MONITORING	= "selesai";
    private static final String COLUMN_STATUS_MONITORING		= "status";
    private static final String COLUMN_TOLERANCY_MONITORING		= "tolerancy";

    private static final String DAY_MONITORING_TABLE_NAME		=
    		"day";
    private static final String COLUMN_MONITORING_DAY_MONITORING	= "monitoring";
    private static final String COLUMN_DAY_DAY_MONITORING			= "day";
    
    private static final String LOCATION_TABLE_NAME				=
    		"lokasi";
    private static final String COLUMN_ID_LOCATION	= "id";
    private static final String COLUMN_LONGITUDE 	= "longitude";
    private static final String COLUMN_LATITUDE		= "latitude";
    private static final String COLUMN_TIME			= "time";
    
    private static final String LOG_TABLE_NAME		=
    		"log";
    private static final String COLUMN_LOCATION_LOG	= "location";
    private static final String COLUMN_ANAK_LOG		= "anak";
    
    private static final String DATE_MONITORING_TABLE_NAME			=
    		"date";
    private static final String COLUMN_MONITORING_DATE_MONITORING	= "monitoring";
    private static final String COLUMN_DATE_DATE_MONITORING			= "date";
      
}
