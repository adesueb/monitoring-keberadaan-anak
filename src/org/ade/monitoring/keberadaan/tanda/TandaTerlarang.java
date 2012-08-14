package org.ade.monitoring.keberadaan.tanda;

import org.ade.monitoring.keberadaan.R;
import org.ade.monitoring.keberadaan.lokasi.GpsManager;
import org.ade.monitoring.keberadaan.service.Status;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;



/**
 * Class TandaTerlarang
 */
public class TandaTerlarang implements TandaLokasi {


  
	public TandaTerlarang (Context context) { 
	  
		mContext = context;
	}
  
	public void pilihTandaiSendiri(){
		Handler handler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch(msg.what){
					case Status.SUCCESS :{
						mGps.getLokasi().getlatitude();
						mGps.getLokasi().getLongitude();
					}
				}
			}
		};
		mGps = new GpsManager(mContext, handler);
		mGps.searchLokasi();
	}

	public void pilihTandaiPeta(){
	}
	
	
	

	private GpsManager mGps;
	private Context mContext;

}
