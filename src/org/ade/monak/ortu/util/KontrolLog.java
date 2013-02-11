package org.ade.monak.ortu.util;

import java.util.Calendar;
import java.util.List;

import org.ade.monak.ortu.entity.Lokasi;
import org.ade.monak.ortu.service.storage.DatabaseManagerOrtu;

public class KontrolLog {
	public static void kontrolDeleteLogLokasi(DatabaseManagerOrtu databaseManager){

		List<Lokasi> lokasis = databaseManager.getAllLogLokasi();
		Calendar cal = Calendar.getInstance();
		long maxDay = cal.getTimeInMillis()-MAX_DAY;
		for(Lokasi lokasiFor:lokasis){
			if(lokasiFor.getTime()<maxDay){
				databaseManager.deleteLokasi(lokasiFor);
			}
		}
	
	}

	private final static long MAX_DAY = (long) 86399999.99980646;
}
