package org.ade.monitoring.keberadaan.lokasi;

import org.ade.monitoring.keberadaan.Variable.Status;
import org.ade.monitoring.keberadaan.boundary.PendaftaranMonitoring;
import org.ade.monitoring.keberadaan.entity.Lokasi;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class TandaLokasiSendiri{

	public TandaLokasiSendiri(Context context, Handler handler){
		mHandler = handler;
		mContext = context;
		mGps = new GpsManager(context, handlerSendiri);
	}
	public void actionTandaiLokasi() {
		if(mGps.isGpsActif()){
			mGps.searchLokasi();
		}else{
			mHandler.post(new Runnable(){
				public void run() {
					Toast.makeText(mContext, "mengambil lokasi gagal,\naktifkan GPS!!", 
							Toast.LENGTH_SHORT).show();
				}	
			});
		}
		
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
					break;
				}
			}
		}
		
	};

	private GpsManager 	mGps;
	private Context 	mContext;
	
	private 		Lokasi 	mLokasi;
	private 		Handler mHandler;

}
