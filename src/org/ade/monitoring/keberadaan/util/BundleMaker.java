package org.ade.monitoring.keberadaan.util;

import org.ade.monitoring.keberadaan.entity.Anak;
import org.ade.monitoring.keberadaan.entity.DataMonitoring;

import android.os.Bundle;

public class BundleMaker {

	public static Bundle makeBundleFromAnak(Anak anak){
		Bundle bundle = new Bundle();
		bundle.putString("id", anak.getIdAnak());
		bundle.putString("nama", anak.getNamaAnak());
		bundle.putString("orangTua", anak.getIdOrtu());
		bundle.putString("noHp", anak.getNoHpAnak());
		return bundle;
	}
	
	public static Bundle makeBundleFromDataMonitoring(DataMonitoring dataMonitoring){
		Bundle bundle = new Bundle();
		bundle.putString("id", dataMonitoring.getIdMonitoring());
		bundle.putString("anak", dataMonitoring.getAnak().getIdAnak());
		return bundle;
	}
}
