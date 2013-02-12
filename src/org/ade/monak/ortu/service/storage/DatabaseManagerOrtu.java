package org.ade.monak.ortu.service.storage;


import java.util.ArrayList;
import java.util.List;

import org.ade.monak.ortu.entity.Anak;
import org.ade.monak.ortu.entity.DataMonitoring;
import org.ade.monak.ortu.entity.DateMonitoring;
import org.ade.monak.ortu.entity.DayMonitoring;
import org.ade.monak.ortu.entity.Lokasi;
import org.ade.monak.ortu.entity.Pelanggaran;
import org.ade.monak.ortu.util.IDGenerator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DatabaseManagerOrtu {

	
	public DatabaseManagerOrtu (Context context) { 
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
		if(cursor!=null && !cursor.isClosed()){
			cursor.close();
			if(getDb().isOpen()){
				getDb().close();
			}
		}
		return anaks;
	}
	
	public List<DataMonitoring> getDataMonitoringWithoutPelanggarans(){
		
		Cursor cursor = actionQuery(
				"select * " +
	    		"FROM "+MONITORING_TABLE_NAME+" m, "+PELANGGARAN_TABLE_NAME+" p " +
	    		"WHERE m."+COLUMN_ID_MONITORING+" != p."+COLUMN_MONITORING_PELANGGARAN);
		
		List<DataMonitoring> dataMonitorings = getDataMonitoringsFromCursor(cursor, true, true);
		
		if(cursor!=null && !cursor.isClosed()){
			cursor.close();
			if(getDb().isOpen()){
				getDb().close();
			}
		}
		
		return dataMonitorings;
	}
	//.....................................................................
	
	// get last id.........................................................
	public List<String> getIdAnaks(){
		Cursor cursor = 
				actionQuery("SELECT "+COLUMN_ID+" from "+ANAK_TABLE_NAME+" order by "+COLUMN_ID+" ASC");
		List<String> ids = getIdsFromCursor(cursor);
		if(cursor!=null && !cursor.isClosed()){
			cursor.close();
			if(getDb().isOpen()){
				getDb().close();
			}
		}
		return ids;
	}
	public List<String> getIdLokasis(){
		Cursor cursor = 
				actionQuery("SELECT "+COLUMN_ID+" from "+LOCATION_TABLE_NAME+" order by "+COLUMN_ID_LOCATION+" ASC");
		List<String> ids = getIdsFromCursor(cursor);

		if(cursor!=null && !cursor.isClosed()){
			cursor.close();
			if(getDb().isOpen()){
				getDb().close();
			}
		}
		
		return ids;
	}
	public List<String> getIdMonitorings(){
		Cursor cursor = 
				actionQuery("SELECT "+COLUMN_ID+" from "+MONITORING_TABLE_NAME+" order by "+COLUMN_ID+" ASC");
		List<String> ids = getIdsFromCursor(cursor);
		if(cursor!=null && !cursor.isClosed()){
			cursor.close();
			if(getDb().isOpen()){
				getDb().close();
			}
		}
		
		return ids;
	}
	public List<String> getIdPelanggarans(){
		Cursor cursor = 
				actionQuery("SELECT "+COLUMN_ID+" from "+PELANGGARAN_TABLE_NAME+" order by "+COLUMN_ID+" ASC");
		
		List<String> ids = getIdsFromCursor(cursor);
		if(cursor!=null && !cursor.isClosed()){
			cursor.close();
			if(getDb().isOpen()){
				getDb().close();
			}
		}
		return ids;
	}
	//.....................................................................
	
	public List<Lokasi> getAllLokasiAnak(Anak anak){
		Cursor cursor = actionQuery(LOCATION_TABLE_NAME,null,COLUMN_ANAK_LOCATION+"='"+anak.getIdAnak()+"'");
		if(cursor!=null && cursor.getCount()>0){
			List<Lokasi> lokasis = getAllLokasiFromCursor(cursor);
			if(cursor!=null && !cursor.isClosed()){
				cursor.close();
				if(getDb().isOpen()){
					getDb().close();
				}
			}
			return lokasis;
		}
		if(cursor!=null && !cursor.isClosed()){
			cursor.close();
			if(getDb().isOpen()){
				getDb().close();
			}
		}
		return null;
	}
	
	public List<Lokasi> getAllLokasi(){
		Cursor cursor = actionQuery(LOCATION_TABLE_NAME,null,null);
		if(cursor!=null && cursor.getCount()>0){
			List<Lokasi> lokasis = getAllLokasiFromCursor(cursor);
			if(cursor!=null && !cursor.isClosed()){
				cursor.close();
				if(getDb().isOpen()){
					getDb().close();
				}
			}
			return lokasis;
		}
		if(cursor!=null && !cursor.isClosed()){
			cursor.close();
			if(getDb().isOpen()){
				getDb().close();
			}
		}
		return null;
	}
	
	public List<Anak> getAllAnakTrack(boolean withPelanggaran, boolean withMonitoring){
		Cursor cursor = actionQuery(ANAK_TABLE_NAME, null,COLUMN_TRACK_ANAK+"='1'");
		if(cursor!=null && cursor.getCount()>0){
			List<Anak> anaks = getAnaksFromCursor(cursor, withPelanggaran, withMonitoring);
			if(cursor!=null && !cursor.isClosed()){
				cursor.close();
				if(getDb().isOpen()){
					getDb().close();
				}
			}
			return anaks;
		}
		if(cursor!=null && !cursor.isClosed()){
			cursor.close();
			if(getDb().isOpen()){
				getDb().close();
			}
		}
		return null;
	}
	
	public List<Anak> getAllAnak(boolean withPelanggaran, boolean withMonitoring){
		Cursor cursor = actionQuery(ANAK_TABLE_NAME, null, null);
		if(cursor!=null && cursor.getCount()>0){
			List<Anak> anaks = getAnaksFromCursor(cursor, withPelanggaran, withMonitoring);
			if(cursor!=null && !cursor.isClosed()){
				cursor.close();
				if(getDb().isOpen()){
					getDb().close();
				}
			}
			return anaks;
		}
		if(cursor!=null && !cursor.isClosed()){
			cursor.close();
			if(getDb().isOpen()){
				getDb().close();
			}
		}
		return null;
	}
	
	
	
	public Anak getAnakById(String idAnak, boolean withPelanggaran, boolean withMonitoring){
		Cursor cursor = actionQuery(ANAK_TABLE_NAME, null,COLUMN_ID_ANAK+"='"+idAnak+"'");
		if(cursor!=null && cursor.getCount()>0 && cursor.moveToFirst()){			
			Anak anak = getAnakFromCursor(cursor, withPelanggaran, withMonitoring);
			if(cursor!=null && !cursor.isClosed()){
				cursor.close();
				if(getDb().isOpen()){
					getDb().close();
				}
			}
			return anak;
		}else{
			if(cursor!=null && !cursor.isClosed()){
				cursor.close();
				if(getDb().isOpen()){
					getDb().close();
				}
			}
			return null;
		}
	}
	
	
	public List<DataMonitoring> getAllDataMonitorings
		(boolean withAnak, boolean withWaktuMonitoring){
		Cursor cursor = actionQuery(MONITORING_TABLE_NAME, null,null);
		if(cursor != null && cursor.getCount()>0){
			List<DataMonitoring> dataMonitorings = 
					getDataMonitoringsFromCursor(cursor, withAnak, withWaktuMonitoring);
			if(cursor!=null && !cursor.isClosed()){
				cursor.close();
				if(getDb().isOpen()){
					getDb().close();
				}
			}
			return dataMonitorings;
		}else{
			if(cursor!=null && !cursor.isClosed()){
				cursor.close();
				if(getDb().isOpen()){
					getDb().close();
				}
			}
			return null;
		}
		
	}
	
	public DataMonitoring getDataMonitoringByIdMonitoring
		(String idDataMonitoring, boolean withAnak, boolean withWaktuMonitoring){
		Cursor cursor = actionQuery(MONITORING_TABLE_NAME, null, COLUMN_ID_MONITORING+"='"+idDataMonitoring+"'");
		if(cursor!=null && cursor.getCount()>0 && cursor.moveToFirst()){
			DataMonitoring dataMonitoring = 
					getDataMonitoringFromCursor(cursor, withAnak, withWaktuMonitoring);
			if(cursor!=null && !cursor.isClosed()){
				cursor.close();
				if(getDb().isOpen()){
					getDb().close();
				}
			}
			return dataMonitoring;
		}else{
			if(cursor!=null && !cursor.isClosed()){
				cursor.close();
				if(getDb().isOpen()){
					getDb().close();
				}
			}
			return null;
		}
	}
		
	public List<DataMonitoring> getDataMonitoringsByAnak(String idAnak){
		Cursor cursor = actionQuery(MONITORING_TABLE_NAME, null, COLUMN_ANAK_MONITORING+"='"+idAnak+"'");
		if(cursor!=null && cursor.getCount()>0){
			List<DataMonitoring> dataMonitorings = getDataMonitoringsFromCursor(cursor, false, true);
			if(cursor!=null && !cursor.isClosed()){
				cursor.close();
				if(getDb().isOpen()){
					getDb().close();
				}
			}
			return dataMonitorings;
		}else{
			if(cursor!=null && !cursor.isClosed()){
				cursor.close();
				if(getDb().isOpen()){
					getDb().close();
				}
			}
			return null;
		}
	}
	
	public List<Pelanggaran> getAllPelanggaranByMonitoring(String idMonitoring){
		Cursor cursor = actionQuery(PELANGGARAN_TABLE_NAME, null, COLUMN_MONITORING_PELANGGARAN+"='"+idMonitoring+"'");
		if(cursor!=null && cursor.getCount()>0){
			List<Pelanggaran> pelanggarans = getPelanggaransFromCursor(cursor, false, false);
			if(cursor!=null && !cursor.isClosed()){
				cursor.close();
				if(getDb().isOpen()){
					getDb().close();
				}
			}
			return pelanggarans;
		}else{
			if(cursor!=null && !cursor.isClosed()){
				cursor.close();
				if(getDb().isOpen()){
					getDb().close();
				}
			}
			return null;
		}
	}
	
	
	public List<Pelanggaran> getDataPelanggaransByAnak(String idAnak){
		Cursor cursor = actionQuery(PELANGGARAN_TABLE_NAME, null, COLUMN_ANAK_PELANGGARAN+"='"+idAnak+"'");
		if(cursor!=null && cursor.getCount()>0){
			List<Pelanggaran> pelanggarans = getPelanggaransFromCursor(cursor, false, false);
			if(cursor!=null && !cursor.isClosed()){
				cursor.close();
				if(getDb().isOpen()){
					getDb().close();
				}
			}
			return pelanggarans;
		}else{
			if(cursor!=null && !cursor.isClosed()){
				cursor.close();
				if(getDb().isOpen()){
					getDb().close();
				}
			}
			return null;
		}
	}
	
	public List<Pelanggaran> getAllDataPelanggarans(boolean withAnak, boolean withMonitoring){
		Cursor cursor = actionQuery(PELANGGARAN_TABLE_NAME, null, null);
		if(cursor!=null && cursor.getCount()>0){
			List<Pelanggaran> pelanggarans = 
					getPelanggaransFromCursor(cursor, withAnak, withMonitoring);
			if(cursor!=null && !cursor.isClosed()){
				cursor.close();
				if(getDb().isOpen()){
					getDb().close();
				}
			}
			return pelanggarans;
		}else{
			if(cursor!=null && !cursor.isClosed()){
				cursor.close();
				if(getDb().isOpen()){
					getDb().close();
				}
			}
			return null;
		}
	}
	
	public List<Lokasi> getAllLogLokasi(){
		Cursor cursor = 
				actionQuery(LOCATION_TABLE_NAME, null, COLUMN_LOG+"='"+1+"'");
		if(cursor!=null && cursor.getCount()>0){
			List<Lokasi> lokasis = getAllLokasiFromCursor(cursor);
			if(cursor!=null && !cursor.isClosed()){
				cursor.close();
				if(getDb().isOpen()){
					getDb().close();
				}
			}
			return lokasis;
		}
		if(cursor!=null && !cursor.isClosed()){
			cursor.close();
			if(getDb().isOpen()){
				getDb().close();
			}
		}
		return null;
	}
	
	public Lokasi getLokasiByIdLokasi(String idLokasi){

		Log.d("database manager", "dapetin lokasi dari id");

		if(idLokasi!=null&&!idLokasi.equals("")){

			Log.d("database manager", "dapetin lokasi dari cursor dengan id : "+idLokasi);

			Cursor cursor = 
					actionQuery(LOCATION_TABLE_NAME, null, COLUMN_ID_LOCATION+"='"+idLokasi+"'");

			Log.d("database manager", "dapetin lokasi dari cursor dengan count : "+cursor.getCount());
			
			if(cursor!=null && cursor.getCount()>0 && cursor.moveToFirst()){
				Lokasi lokasi = 
						getLokasiFromCursor(cursor);
				if(cursor!=null && !cursor.isClosed()){
					cursor.close();
					if(getDb().isOpen()){
						getDb().close();
					}
				}
				return lokasi;
			}else{
				if(cursor!=null && !cursor.isClosed()){
					cursor.close();
					if(getDb().isOpen()){
						getDb().close();
					}
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
			if(cursor!=null && !cursor.isClosed()){
				cursor.close();
				if(getDb().isOpen()){
					getDb().close();
				}
			}
			return dateMonitoring;
		}else{
			if(cursor!=null && !cursor.isClosed()){
				cursor.close();
				if(getDb().isOpen()){
					getDb().close();
				}
			}
			return null;
		}
	}
	
	public List<DateMonitoring> getAllTanggalMonitorings(boolean withDataMonitoring){
		Cursor cursor = 
				actionQuery(DATE_MONITORING_TABLE_NAME, null, null);
		if(cursor!=null && cursor.getCount()>0){
			List<DateMonitoring> dateMonitorings = getTanggalsMonitoringsFromCursor(cursor, withDataMonitoring);
			if(cursor!=null && !cursor.isClosed()){
				cursor.close();
				if(getDb().isOpen()){
					getDb().close();
				}
			}
			return dateMonitorings;
		}else{
			if(cursor!=null && !cursor.isClosed()){
				cursor.close();
				if(getDb().isOpen()){
					getDb().close();
				}
			}
			return null;
		}
	}
	
	public List<DayMonitoring> getHariMonitoringsByMonitoring(String idMonitoring, boolean withDataMonitoring){
		Cursor cursor = 
				actionQuery(DAY_MONITORING_TABLE_NAME, null, COLUMN_MONITORING_DAY_MONITORING+"='"+idMonitoring+"'");
		if(cursor!=null && cursor.getCount()>0){
			List<DayMonitoring> dayMonitorings = getHarisMonitoringsFromCursor(cursor, withDataMonitoring);
			
			if(cursor!=null && !cursor.isClosed()){
				cursor.close();
				if(getDb().isOpen()){
					getDb().close();
				}
			}
			return dayMonitorings;
		}else{
			if(cursor!=null && !cursor.isClosed()){
				cursor.close();
				if(getDb().isOpen()){
					getDb().close();
				}
			}
			return null;
		}
	}
	
	public List<DayMonitoring> getAllHariMonitorings(boolean withDataMonitoring){
		Cursor cursor = 
				actionQuery(DAY_MONITORING_TABLE_NAME, null, null);
		if(cursor!=null && cursor.getCount()>0){
			List<DayMonitoring> dayMonitorings = getHarisMonitoringsFromCursor(cursor, withDataMonitoring);
			if(cursor!=null && !cursor.isClosed()){
				cursor.close();
				if(getDb().isOpen()){
					getDb().close();
				}
			}
			return dayMonitorings;
		}else{
			if(cursor!=null && !cursor.isClosed()){
				cursor.close();
				if(getDb().isOpen()){
					getDb().close();
				}
			}
			return null;
		}
	}
	
	//...................................................................
	
	// delete............................................................
	
	public void deleteLokasiAnak(Anak anak){
		getDb().delete
			(LOCATION_TABLE_NAME, COLUMN_ANAK_LOCATION+"='"+anak.getIdAnak()+"'", null);
	}
	


	
	public void deleteLokasi(Lokasi lokasi){
		getDb().delete
			(LOCATION_TABLE_NAME, COLUMN_ID_LOCATION+"='"+lokasi.getId()+"'", null);
		
		
	}

	
	public void deleteAllLokasi(){
		long result = getDb().delete
			(LOCATION_TABLE_NAME, null, null);
		if(result>0){
			LocationFileManager.clearLocation();
		}
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
		List<DataMonitoring> dataMonitorings = getAllDataMonitorings(true, true);
		for(DataMonitoring dataMonitoring:dataMonitorings){
			deleteDataMonitoring(dataMonitoring);
		}
		
	}
	
	public void deleteAllAnak(){
		getDb().delete(ANAK_TABLE_NAME, null, null);
	}
	
	public void deleteAllPelanggarans(){
		List<Pelanggaran> pelanggarans = getAllDataPelanggarans(false, false);
		for(Pelanggaran pelanggaran:pelanggarans){
			deletePelanggaran(pelanggaran);
		}
	}
	
	public void deleteAllPelanggaranByMonitoring(DataMonitoring dataMonitoring){
		List<Pelanggaran> pelanggarans = getAllPelanggaranByMonitoring(dataMonitoring.getIdMonitoring()); 
		if(pelanggarans!=null){
			for(Pelanggaran pelanggaran:pelanggarans){
				deletePelanggaran(pelanggaran);
			}	
		}
		
	}
	
	public void deleteDataMonitoring(DataMonitoring dataMonitoring){
		
		dataMonitoring = getDataMonitoringByIdMonitoring(dataMonitoring.getIdMonitoring(), true, true);
		if(dataMonitoring==null){
			return;
		}
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
		
		deleteAllPelanggaranByMonitoring(dataMonitoring);
		
		Lokasi lokasi = dataMonitoring.getLokasi();
		if(lokasi!=null){
			deleteLokasi(lokasi);
		}
		
	}
	
	public void deleteAnak(Anak anak){
		List<DataMonitoring> dataMonitorings = anak.getDataMonitorings();
		if(dataMonitorings==null){
			dataMonitorings = getDataMonitoringsByAnak(anak.getIdAnak());
		}
		
		if(dataMonitorings!=null){
			for(DataMonitoring dataMonitoring: dataMonitorings){
				deleteDataMonitoring(dataMonitoring);
			}
		}
		
		getDb().delete
			(ANAK_TABLE_NAME, 
				COLUMN_ID_ANAK+"='"+anak.getIdAnak()+"'", null);
		deleteLokasiAnak(anak);
		
		
		
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
	public void updateAllDateMonitoringsByMonitoring(List<DateMonitoring> tanggals){
		if(tanggals.size()>0){
			deleteDateMonitoringByIdMonitoring(tanggals.get(0).getDataMonitoring().getIdMonitoring());
			addTanggalMonitorings(tanggals);		
		}
	
	}
	
	public void updateAllDayMonitoringsByMonitoring(List<DayMonitoring> haris){
		if(haris.size()>0){
			deleteDayMonitoringByIdMonitoring(haris.get(0).getDataMonitoring().getIdMonitoring());
			addHariMonitorings(haris);
		}
	}

	
	public void updateLokasi(Lokasi lokasi){
		ContentValues cv = new ContentValues();
		cv.put(COLUMN_ID_LOCATION, lokasi.getId());
		cv.put(COLUMN_LATITUDE, lokasi.getlatitude());
		cv.put(COLUMN_LONGITUDE, lokasi.getLongitude());
		if(lokasi.isLog()){
			cv.put(COLUMN_LOG, "1");	
		}else{
			cv.put(COLUMN_LOG, "0");
		}
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
		String idLokasi = dataMonitoring.getLokasi().getId();
		if(idLokasi==null || idLokasi.equals("")){
			IDGenerator idGenerator = new IDGenerator(context, this);
			idLokasi = idGenerator.getIdLocation();
			dataMonitoring.getLokasi().setId(idLokasi);
		}
		
		if(dataMonitoring.isAktif()){
			cv.put(COLUMN_AKTIF_MONITORING, "1");	
		}else{
			cv.put(COLUMN_AKTIF_MONITORING, "0");	
		}
		
		
		Lokasi lokasi = getLokasiByIdLokasi(idLokasi);
		if(lokasi==null){
			DataMonitoring datamonitoringLokasi = getDataMonitoringByIdMonitoring(dataMonitoring.getIdMonitoring(), false, false);
			deleteLokasi(datamonitoringLokasi.getLokasi());
		}
		
		cv.put(COLUMN_LOCATION_MONITORING, idLokasi);			
		
		long result = getDb().update(MONITORING_TABLE_NAME, cv, COLUMN_ID_MONITORING+"='"+dataMonitoring.getIdMonitoring()+"'", null);
		
		List<DateMonitoring> tanggals = dataMonitoring.getTanggals();
		if(tanggals!=null){
			updateAllDateMonitoringsByMonitoring(tanggals);
		}
		
		List<DayMonitoring> haris = dataMonitoring.getHaris();
		if(haris!=null){
			updateAllDayMonitoringsByMonitoring(haris);
		}
		
		if(result>0){
			if(dataMonitoring.getLokasi()!=null){
				updateLokasi(dataMonitoring.getLokasi());
			}
		}
	}
		
	public void updateAnak(Anak anak){
		
		if(anak!=null){
			ContentValues cv = new ContentValues();
			if(anak.getNamaAnak()!=null && !anak.getNamaAnak().equals("")){
				cv.put(COLUMN_NAMA_ANAK, anak.getNamaAnak());	
			}
			
			if(anak.getNoHpAnak()!=null && !anak.getNoHpAnak().equals("")){
				cv.put(COLUMN_NO_HP_ANAK, anak.getNoHpAnak());	
			}
			
			boolean isDeleteLokasi = true;
			
			List<Lokasi> lokasis= anak.getLokasis();
			
			if(lokasis!=null && lokasis.size()>0){
				cv.put(COLUMN_LAST_LOCATION_ANAK, lokasis.get(lokasis.size()-1).getId());
				isDeleteLokasi = false;
			}
			
			Lokasi lastLokasi = anak.getLastLokasi();
			
			if(lastLokasi!=null){
				if(lastLokasi.getId()==null||lastLokasi.getId().equals("")){
					IDGenerator idGenerator = new IDGenerator(context, this);
					lastLokasi.setId(idGenerator.getIdLocation());
				}
				cv.put(COLUMN_LAST_LOCATION_ANAK, lastLokasi.getId());
				isDeleteLokasi = false;
			}
			
			if(isDeleteLokasi){
				cv.put(COLUMN_LAST_LOCATION_ANAK, "-");
			}
			
		
			if(anak.isTrack()){
				cv.put(COLUMN_TRACK_ANAK, "1");	
			}else{
				cv.put(COLUMN_TRACK_ANAK, "0");	
			}
			
			long result = getDb().update(ANAK_TABLE_NAME, cv, COLUMN_ID_ANAK+"='"+anak.getIdAnak()+"'", null);

			if(result > 0 && anak.getLokasis()!=null){
				updateLokasisAnak(anak);	
			}
			
			if(result > 0 && anak.getLastLokasi()!=null){
				updateLastLokasiAnak(anak);	
			}
	
		}
				
	}
	
	public void updateLokasisAnak(Anak anak){
		
		ContentValues cv = new ContentValues();
		if(anak.getNamaAnak()!=null && !anak.getNamaAnak().equals("")){
			cv.put(COLUMN_NAMA_ANAK, anak.getNamaAnak());	
		}
		
		if(anak.getNoHpAnak()!=null && !anak.getNoHpAnak().equals("")){
			cv.put(COLUMN_NO_HP_ANAK, anak.getNoHpAnak());	
		}
		List<Lokasi> lokasis= anak.getLokasis();
		
		if(lokasis!=null && lokasis.size()>0){
			cv.put(COLUMN_LAST_LOCATION_ANAK, lokasis.get(lokasis.size()-1).getId());	
		}
		
		if(anak.isTrack()){
			cv.put(COLUMN_TRACK_ANAK, "1");	
		}else{
			cv.put(COLUMN_TRACK_ANAK, "0");	
		}
		
		long result = getDb().update(ANAK_TABLE_NAME, cv, COLUMN_ID_ANAK+"='"+anak.getIdAnak()+"'", null);
		
		Log.d("database manager", "result : "+result);
		
		if(result>0 && anak.getLokasis()!=null){
			Anak anakLokasi = new Anak();
			anakLokasi.setIdAnak(anak.getIdAnak());
			for(Lokasi lokasi:lokasis){
				lokasi.setAnak(anakLokasi);
			}
			addLokasis(anak.getLokasis());
		}
		
	}
	
	public void updateLastLokasiAnak(Anak anak){
		
		ContentValues cv = new ContentValues();
		
		if(anak.getNamaAnak()!=null && !anak.getNamaAnak().equals("")){
			cv.put(COLUMN_NAMA_ANAK, anak.getNamaAnak());	
		}
		
		if(anak.getNoHpAnak()!=null && !anak.getNoHpAnak().equals("")){
			cv.put(COLUMN_NO_HP_ANAK, anak.getNoHpAnak());	
		}
		
		Lokasi lastLokasi = anak.getLastLokasi();
		
		if(lastLokasi!=null){
			if(lastLokasi.getId()==null||lastLokasi.getId().equals("")){
				IDGenerator idGenerator = new IDGenerator(context, this);
				lastLokasi.setId(idGenerator.getIdLocation());		
			}
			cv.put(COLUMN_LAST_LOCATION_ANAK, lastLokasi.getId());	
		}
		
		
		if(anak.isTrack()){
			cv.put(COLUMN_TRACK_ANAK, "1");	
		}else{
			cv.put(COLUMN_TRACK_ANAK, "0");	
		}

		long result = getDb().update(ANAK_TABLE_NAME, cv, COLUMN_ID_ANAK+"='"+anak.getIdAnak()+"'", null);
	
		if(result>0 && anak.getLastLokasi()!=null){
			Anak anakLokasi = new Anak();
			anakLokasi.setIdAnak(anak.getIdAnak());
			anak.getLastLokasi().setAnak(anakLokasi);
			addLokasi(anak.getLastLokasi());
		}
		
	}

	//...................................................................
	
	// add...............................................................
	
	public void addLastLokasiAnak(Anak anak){
		
		updateLastLokasiAnak(anak);
	}
	
	public void addLokasisAnak(Anak anak){
		updateLokasisAnak(anak);
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
			cv.put(COLUMN_TOLERANCY_MONITORING, dataMonitoring.getTolerancy());
			cv.put(COLUMN_KET_MONITORING, dataMonitoring.getKeterangan());
			
			if(dataMonitoring.isAktif()){
				cv.put(COLUMN_AKTIF_MONITORING, "1");	
			}else{
				cv.put(COLUMN_AKTIF_MONITORING, "0");	
			}
			
			String idLokasi = dataMonitoring.getLokasi().getId();
			if(idLokasi==null || idLokasi.equals("")){
				IDGenerator idGenerator = new IDGenerator(context, this);
				idLokasi = idGenerator.getIdLocation();			
				dataMonitoring.getLokasi().setId(idLokasi);
			}
			cv.put(COLUMN_LOCATION_MONITORING, idLokasi);
		
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

				DataMonitoringFileManager.addDataMonitoring(dataMonitoring);
				if(dataMonitoring.getLokasi()!=null){
					addLokasi(dataMonitoring.getLokasi());
				}
			}
			
		}
	}
	
	public void setAktifMonitoring(DataMonitoring dataMonitoring){

		ContentValues cv = new ContentValues();
		if(dataMonitoring.isAktif()){
			cv.put(COLUMN_AKTIF_MONITORING, "1");	
		}else{
			cv.put(COLUMN_AKTIF_MONITORING, "0");	
		}
		
		getDb().update(MONITORING_TABLE_NAME, cv, COLUMN_ID_MONITORING+"='"+dataMonitoring.getIdMonitoring()+"'", null);
		
	}
	
	public void setTrackAnak(Anak anak){

		ContentValues cv = new ContentValues();
		if(anak.isTrack()){
			cv.put(COLUMN_TRACK_ANAK, "1");	
		}else{
			cv.put(COLUMN_TRACK_ANAK, "0");	
		}
		
		getDb().update(ANAK_TABLE_NAME, cv, COLUMN_ID_ANAK+"='"+anak.getIdAnak()+"'", null);
		
	}
	
	
	
	public void addAnak(Anak anak){
		ContentValues cv = new ContentValues();
		cv.put(COLUMN_ID_ANAK, anak.getIdAnak());
		cv.put(COLUMN_ORTU_ANAK, anak.getIdOrtu());
		cv.put(COLUMN_NAMA_ANAK, anak.getNamaAnak());
		cv.put(COLUMN_NO_HP_ANAK, anak.getNoHpAnak());
			
		if(anak.isTrack()){
			cv.put(COLUMN_TRACK_ANAK, "1");	
		}else{
			cv.put(COLUMN_TRACK_ANAK, "0");	
		}
		
		long result = getDb().insert(ANAK_TABLE_NAME, null, cv);
		
		if(result <=0 || anak.getPelanggarans()==null){
			
		}else{
			addPelanggarans(anak.getPelanggarans());
		}
		
		if(result <=0 || anak.getLastLokasi()==null){
			cv.put(COLUMN_LAST_LOCATION_ANAK, "-");
		}else{
			Log.d("insert_anak", "lokasi dari anak adalah : "+anak.getLastLokasi().getlatitude()+","+anak.getLastLokasi().getLongitude());
			Anak anakLokasi = new Anak();
			anakLokasi.setIdAnak(anak.getIdAnak());
			anak.getLastLokasi().setAnak(anakLokasi);
			addLokasi(anak.getLastLokasi());
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
			if(pelanggaran.getLokasi()!=null){
				cv.put(COLUMN_LOCATION_PELANGGARAN, pelanggaran.getLokasi().getId());	
			}
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
	
	public void addLokasis(List<Lokasi> lokasis){
		if(lokasis!=null){
			for(Lokasi lokasi:lokasis){
				addLokasi(lokasi);
			}
		}
	}
	
	public void addLokasi(Lokasi lokasi){
		if(lokasi!=null){
			ContentValues cv = new ContentValues();
			if(lokasi.getId()==null||lokasi.getId().equals("")){
				IDGenerator idGenerator = new IDGenerator(context, this);
				lokasi.setId(idGenerator.getIdLocation());
			}
			cv.put(COLUMN_ID_LOCATION, lokasi.getId());
			Anak anak = lokasi.getAnak();
			if(anak!=null){
				cv.put(COLUMN_ANAK_LOCATION, anak.getIdAnak());	
			}
			cv.put(COLUMN_LATITUDE, lokasi.getlatitude());
			cv.put(COLUMN_LONGITUDE, lokasi.getLongitude());
			cv.put(COLUMN_TIME, lokasi.getTime());
			
			if(lokasi.isLog()){
				cv.put(COLUMN_LOG, "1");	
			}else{
				cv.put(COLUMN_LOG, "0");
			}
			
			long result = getDb().insert(LOCATION_TABLE_NAME, null, cv);
			if(result>0){
				LocationFileManager.addLocation(lokasi);
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
			cv.put(COLUMN_MONITORING_DATE_MONITORING, tanggalMonitoring.getDataMonitoring().getIdMonitoring());
			
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
			cv.put(COLUMN_MONITORING_DAY_MONITORING, dayMonitoring.getDataMonitoring().getIdMonitoring());
			
			getDb().insert(DAY_MONITORING_TABLE_NAME, null, cv);	
		}
	}
	
	
	
	//............................................................................
	
	//get from Cursor.............................................................
	
	private List<String> getIdsFromCursor(Cursor cursor){
		List<String> ids = new ArrayList<String>();
		if(cursor.moveToFirst()){
			do{
				ids.add(cursor.getString(cursor.getColumnIndex(COLUMN_ID)));
			}while(cursor.moveToNext());
			cursor.close();
		}
		return ids;
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
	
	
	
	private Anak getAnakFromCursor(Cursor cursor, boolean withPelanggaran, boolean withMonitoring){
		if(cursor!=null && cursor.getCount()>0){
			Anak anak = new Anak();
			int indexIdAnak 	= cursor.getColumnIndex(COLUMN_ID_ANAK);
			int indexOrtuAnak	= cursor.getColumnIndex(COLUMN_ORTU_ANAK);
			int indexNamaAnak	= cursor.getColumnIndex(COLUMN_NAMA_ANAK);
			int indexPhoneAnak	= cursor.getColumnIndex(COLUMN_NO_HP_ANAK);
			int indexLocationAnak 	= cursor.getColumnIndex(COLUMN_LAST_LOCATION_ANAK);
			int indexTrackAnak	= cursor.getColumnIndex(COLUMN_TRACK_ANAK);
			
			anak.setIdAnak(cursor.getString(indexIdAnak));
			
			if(cursor.getString(indexTrackAnak).equals("1")){
				anak.setTrack(true);
			}else{
				anak.setTrack(false);
			}
			
			
			anak.setIdOrtu(cursor.getString(indexOrtuAnak));
			anak.setNamaAnak(cursor.getString(indexNamaAnak));
			anak.setNoHpAnak(cursor.getString(indexPhoneAnak));
			Log.d("database manager", "get lokasi dr anak dgn id lokasi : "+cursor.getString(indexLocationAnak));
			anak.setLastLokasi(getLokasiByIdLokasi(cursor.getString(indexLocationAnak)));
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
			int indexAktifMonitoring		= cursor.getColumnIndex(COLUMN_AKTIF_MONITORING);
			
			dataMonitoring.setIdMonitoring(cursor.getString(indexIdMonitoring));
			dataMonitoring.setLokasi(getLokasiByIdLokasi(cursor.getString(indexLocationMonitoring)));
			dataMonitoring.setKeterangan(cursor.getString(indexKetMonitoring));
			dataMonitoring.setWaktuMulai(cursor.getLong(indexDateMulaiMonitoring));
			dataMonitoring.setWaktuSelesai(cursor.getLong(indexDateSelesaiMonitoring));
			dataMonitoring.setStatus(cursor.getInt(indexStatusMonitoring));
			dataMonitoring.setTolerancy(cursor.getInt(indexTolerancy));
			if(cursor.getString(indexAktifMonitoring).equals("1")){
				dataMonitoring.setAktif(true);
			}else{
				dataMonitoring.setAktif(false);
			}
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
			int indexLog		= cursor.getColumnIndex(COLUMN_LOG);
			int indexTime		= cursor.getColumnIndex(COLUMN_TIME);
			
			lokasi.setId(cursor.getString(indexIdLokasi));
			if(cursor.getString(indexLog).equals("1")){
				lokasi.setLog(true);
			}else{
				lokasi.setLog(false);
			}
			lokasi.setLatitude(cursor.getDouble(indexLatitude));
			lokasi.setLongitude(cursor.getDouble(indexLongitude));
			lokasi.setTime(cursor.getLong(indexTime));
			
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
	  
	private static SQLiteDatabase getDb(){
		if(mDatabaseHelper.getWritableDatabase().isOpen()){
			mDatabaseHelper.getWritableDatabase().close();
		}
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
			db.execSQL(CREATE_DATE_MONITORING);
			db.execSQL(CREATE_DAY_MONITORING);
	    }
	    
	    private void dropTables(SQLiteDatabase db){
	    	db.execSQL("DROP TABLE IF EXISTS " + ANAK_TABLE_NAME);
	    	db.execSQL("DROP TABLE IF EXISTS " + LOCATION_TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + PELANGGARAN_TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + MONITORING_TABLE_NAME);
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
	    private static final int DATABASE_VERSION = 11;
		
	    private static final String CREATE_ANAK = 
	    		"CREATE TABLE IF NOT EXISTS "+
	    		ANAK_TABLE_NAME+" ("+
	    		COLUMN_ID_ANAK+" VARCHAR(10) PRIMARY KEY,"+
	    		COLUMN_LAST_LOCATION_ANAK+" VARCHAR(10),"+
	    		COLUMN_ORTU_ANAK+" VARCHAR(40),"+
	    		COLUMN_NAMA_ANAK+" VARCHAR(100),"+
	    		COLUMN_NO_HP_ANAK+" VARCHAR(50),"+
	    		COLUMN_TRACK_ANAK+" VARCHAR(1))";
	    
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
	    		COLUMN_AKTIF_MONITORING+" VARCHAR(1),"+	    		
	    		COLUMN_TOLERANCY_MONITORING+" INTEGER)";
	    
	    private static final String CREATE_LOCATION = 
	    		"CREATE TABLE IF NOT EXISTS "+
	    		LOCATION_TABLE_NAME+" ("+
	    		COLUMN_ID_LOCATION+" VARCHAR(10) PRIMARY KEY,"+	   
	    		COLUMN_ANAK_LOCATION+" VARCHAR(10),"+
	    		COLUMN_LONGITUDE+" REAL,"+
	    		COLUMN_LATITUDE+" REAL,"+
	    		COLUMN_LOG+" VARCHAR(1),"+
	    		COLUMN_TIME+" INTEGER)";
	    
	    
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
	
	private static DatabaseHelper mDatabaseHelper = null;
	
	    
	
	private static final String COLUMN_ID 		= "id";
	
	private static final String ANAK_TABLE_NAME 			= 
    		"anak";
    private static final String COLUMN_ID_ANAK 				= "id";
    private static final String COLUMN_ORTU_ANAK			= "ortu";
    private static final String COLUMN_NAMA_ANAK			= "nama";
    private static final String COLUMN_NO_HP_ANAK			= "no_hp";
    private static final String COLUMN_LAST_LOCATION_ANAK 	= "lokasi";
    private static final String COLUMN_TRACK_ANAK			= "track";
    
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
    private static final String COLUMN_AKTIF_MONITORING			= "aktif";
    private static final String DAY_MONITORING_TABLE_NAME		=
    		"day";
    private static final String COLUMN_MONITORING_DAY_MONITORING	= "monitoring";
    private static final String COLUMN_DAY_DAY_MONITORING			= "day";
    
    private static final String LOCATION_TABLE_NAME				=
    		"lokasi";
    private static final String COLUMN_ID_LOCATION	= "id";
    private static final String COLUMN_ANAK_LOCATION= "anak";
    private static final String COLUMN_LONGITUDE 	= "longitude";
    private static final String COLUMN_LATITUDE		= "latitude";
    private static final String COLUMN_TIME			= "time";
    private static final String COLUMN_LOG			= "log";
    
    private static final String DATE_MONITORING_TABLE_NAME			=
    		"date";
    private static final String COLUMN_MONITORING_DATE_MONITORING	= "monitoring";
    private static final String COLUMN_DATE_DATE_MONITORING			= "date";
      
}
