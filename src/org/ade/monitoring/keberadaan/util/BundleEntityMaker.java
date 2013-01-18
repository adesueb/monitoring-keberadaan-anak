package org.ade.monitoring.keberadaan.util;

import org.ade.monitoring.keberadaan.entity.Anak;
import org.ade.monitoring.keberadaan.entity.DataMonitoring;
import org.ade.monitoring.keberadaan.entity.Lokasi;

import android.location.Location;
import android.os.Bundle;

public class BundleEntityMaker {

	public static Bundle makeBundleFromAnak(Anak anak){
		Bundle bundle = new Bundle();
		bundle.putString("id", anak.getIdAnak());
		bundle.putString("nama", anak.getNamaAnak());
		bundle.putString("orangTua", anak.getIdOrtu());
		bundle.putString("noHp", anak.getNoHpAnak());
		if(anak.getLastLokasi()!=null){
			bundle.putString("lastLokasi", anak.getLastLokasi().getId());
		}
		return bundle;
	}
	
	public static Bundle makeBundleFromDataMonitoring(DataMonitoring dataMonitoring){
		Bundle bundle = new Bundle();
		bundle.putString("id", dataMonitoring.getIdMonitoring());
		
		return bundle;
	}
	
	
	
	public static Bundle makeBundleFromLokasi(Lokasi lokasi){
		Bundle bundle = new Bundle();
		bundle.putDouble("longitude", lokasi.getLongitude());
		bundle.putDouble("latitude", lokasi.getlatitude());
		bundle.putLong("time", lokasi.getTime());
		return bundle;
	}
	
	public static Bundle makeBundleFromLocation(Location lokasi){
		Bundle bundle = new Bundle();
		bundle.putDouble("longitude", lokasi.getLongitude());
		bundle.putDouble("latitude", lokasi.getLatitude());
		bundle.putLong("time", lokasi.getTime());
		return bundle;
	}
}
