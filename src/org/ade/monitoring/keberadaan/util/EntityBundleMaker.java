package org.ade.monitoring.keberadaan.util;

import org.ade.monitoring.keberadaan.entity.Anak;
import org.ade.monitoring.keberadaan.entity.DataMonitoring;

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
}
