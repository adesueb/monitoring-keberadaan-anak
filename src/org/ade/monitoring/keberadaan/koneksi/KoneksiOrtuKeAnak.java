package org.ade.monitoring.keberadaan.koneksi;

import org.ade.monitoring.keberadaan.entity.DataMonitoring;

public class KoneksiOrtuKeAnak {
	
	public KoneksiOrtuKeAnak(){
		
	}
	
	public boolean sendDataMonitoring(DataMonitoring dataMonitoring){
		//TODO : send data monitoring sebelum disimpen di database ortu....
		return false;
	}
	private KoneksiSMS koneksiSms;
}
