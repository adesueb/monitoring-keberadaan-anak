package org.ade.monitoring.keberadaan.tanda;

import org.ade.monitoring.keberadaan.lokasi.GpsManager;
import org.ade.monitoring.keberadaan.service.Status;

import android.content.Context;
import android.os.Handler;
import android.os.Message;



/**
 * Class TandaSeharusnya
 */
public class TandaSeharusnya implements TandaLokasi {

  

	public TandaSeharusnya () { }

	public void pilihTandaiPeta() {
		
	}

	public void pilihTandaiSendiri() {
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
	
	private GpsManager mGps;
	private Context mContext;
  


}
