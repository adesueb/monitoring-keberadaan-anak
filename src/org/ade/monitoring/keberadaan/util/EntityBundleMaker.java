package org.ade.monitoring.keberadaan.util;

import org.ade.monitoring.keberadaan.entity.Anak;
import org.ade.monitoring.keberadaan.entity.DataMonitoring;
import org.ade.monitoring.keberadaan.entity.Lokasi;

import android.os.Bundle;

public class EntityBundleMaker {
	public static Anak getAnakFromBundle(Bundle bundle){
		if(bundle.getString("id")==null || bundle.getString("id").equals("")){
			return null;
		}
		Anak anak = new Anak();
		anak.setIdAnak(bundle.getString("id"));
		anak.setNamaAnak(bundle.getString("nama"));
		anak.setNoHpAnak(bundle.getString("noHp"));
		anak.setIdOrtu(bundle.getString("orangTua"));
		Lokasi lokasi = new Lokasi();
		lokasi.setId(bundle.getString("lastLokasi"));
		anak.setLastLokasi(lokasi);  
		return anak;
	}
	
	public static DataMonitoring getDataMonitoringFromBundle(Bundle bundle){
		
		
		if(bundle==null||bundle.getString("id")==null||bundle.getString("id").equals("")){
			return null;
		}
		
		DataMonitoring dataMonitoring = new DataMonitoring();
		dataMonitoring.setIdMonitoring(bundle.getString("id"));
		return dataMonitoring;
	}
	
	public static Lokasi getLokasiFromBundle(Bundle bundle){
		if(bundle.getDouble("latitude")==0){
			return null;
		}
		Lokasi lokasi = new Lokasi();
		lokasi.setLatitude(bundle.getDouble("latitude"));
		lokasi.setLongitude(bundle.getDouble("longitude"));
		lokasi.setTime(bundle.getLong("time"));
		return lokasi;
	}
}
