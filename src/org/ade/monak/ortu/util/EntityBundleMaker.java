package org.ade.monak.ortu.util;

import org.ade.monak.ortu.entity.Anak;
import org.ade.monak.ortu.entity.DataMonitoring;
import org.ade.monak.ortu.entity.Lokasi;

import android.os.Bundle;

public class EntityBundleMaker {
	public static Anak getAnakFromBundle(Bundle bundle){
		if(bundle==null || bundle.getString("idAnak")==null || bundle.getString("idAnak").equals("")){
			return null;
		}
		Anak anak = new Anak();
		anak.setIdAnak(bundle.getString("idAnak"));
		anak.setNamaAnak(bundle.getString("nama"));
		anak.setNoHpAnak(bundle.getString("noHp"));
		anak.setNoImeiAnak(bundle.getString("imei"));
		anak.setIdOrtu(bundle.getString("orangTua"));
		Lokasi lokasi = new Lokasi();
		lokasi.setId(bundle.getString("lastLokasi"));
		anak.setLastLokasi(lokasi);  
		return anak;
	}
	
	public static DataMonitoring getDataMonitoringFromBundle(Bundle bundle){
		
		
		if(bundle==null||bundle.getString("idMonitoring")==null||bundle.getString("idMonitoring").equals("")){
			return null;
		}
		
		DataMonitoring dataMonitoring = new DataMonitoring();
		dataMonitoring.setIdMonitoring(bundle.getString("idMonitoring"));
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
