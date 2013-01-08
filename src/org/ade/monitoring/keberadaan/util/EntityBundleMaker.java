package org.ade.monitoring.keberadaan.util;

import org.ade.monitoring.keberadaan.entity.Anak;
import org.ade.monitoring.keberadaan.entity.DataMonitoring;
import org.ade.monitoring.keberadaan.entity.Lokasi;

import android.os.Bundle;

public class EntityBundleMaker {
	public static Anak getAnakFromBundle(Bundle bundle){
		Anak anak = new Anak();
		//TODO : first step is create anak from bundle, and then return it...   
		return anak;
	}
	
	public static DataMonitoring getDataMonitoringFromBundle(Bundle bundle){
		DataMonitoring dataMonitoring = new DataMonitoring();
		//TODO : first step is create datamonitoring, then return it....
		return dataMonitoring;
	}
	
	public static Lokasi getLokasiFromBundle(Bundle bundle){
		Lokasi lokasi = new Lokasi();
		lokasi.setLatitude(bundle.getDouble("latitude"));
		lokasi.setLongitude(bundle.getDouble("longitude"));
		lokasi.setTime(bundle.getLong("time"));
		return lokasi;
	}
}
