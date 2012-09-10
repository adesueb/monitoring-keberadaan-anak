package org.ade.monitoring.keberadaan.tanda;

import org.ade.monitoring.keberadaan.boundary.PendaftaranMonitoring;
import org.ade.monitoring.keberadaan.entity.Lokasi;
import org.ade.monitoring.keberadaan.lokasi.GpsManager;
import org.ade.monitoring.keberadaan.util.Status;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

public class TandaLokasiSendiri implements ITandaLokasi{

	public TandaLokasiSendiri(Context context, Handler handler){
		mHandler = handler;
		
		mGps = new GpsManager(context, handlerSendiri);
	}
	public void actionTandaiLokasi() {

		mGps.searchLokasi();
	}

	public Lokasi getLokasi() {
		
		return mLokasi;
	}
	
	Handler handlerSendiri = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch(msg.what){
				case Status.SUCCESS :{
					if(mGps!=null){
						mLokasi = new Lokasi();
						mLokasi.setLatitude(mGps.getLokasi().getlatitude());
						mLokasi.setLongitude(mGps.getLokasi().getLongitude());
						mHandler.sendEmptyMessage(PendaftaranMonitoring.LOKASI);
					}
					
				}
			}
		}
		
	};

	private GpsManager mGps;
	
	private 		Lokasi 	mLokasi;
	private 		Handler mHandler;

}
