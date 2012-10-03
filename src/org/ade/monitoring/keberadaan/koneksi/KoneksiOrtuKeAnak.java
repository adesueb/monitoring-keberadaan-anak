package org.ade.monitoring.keberadaan.koneksi;

import org.ade.monitoring.keberadaan.entity.DataMonitoring;
import org.ade.monitoring.keberadaan.entity.PesanData;

import android.content.Context;

public class KoneksiOrtuKeAnak {
	
	public KoneksiOrtuKeAnak(Context context){
		koneksiSms = new KoneksiSMSMonitoring(context);
		koneksiInternet = new KoneksiInternetMonitoring();
	}
	
	public boolean sendDataMonitoring(DataMonitoring dataMonitoring){
		//TODO : send data monitoring sebelum disimpen di database ortu....
		return false;
	}
	
	
	
	private final KoneksiInternetMonitoring koneksiInternet;
	private final KoneksiSMSMonitoring koneksiSms;
	
	// kelas tambahan....................................................
	
	private final static class KoneksiSMSMonitoring extends KoneksiSMS{

		public KoneksiSMSMonitoring(Context context) {
			super(context);
		}

		@Override
		public void menerimaPesanData(PesanData pesanData) {
			
		}
		
	}
	
	private final static class KoneksiInternetMonitoring extends KoneksiInternet{

		@Override
		public void menerimaPesanData(PesanData pesanData) {
			
		}
		
	}	
	//.............................................................
}
