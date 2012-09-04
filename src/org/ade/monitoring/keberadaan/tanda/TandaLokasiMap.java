package org.ade.monitoring.keberadaan.tanda;

import org.ade.monitoring.keberadaan.entity.Lokasi;

import android.content.Context;
import android.os.Handler;

public class TandaLokasiMap implements ITandaLokasi{

	public TandaLokasiMap(Context context, Handler handler){
		mContext 	= context;
		mHandler	= handler;
	}
	public void actionTandaiLokasi() {
		
	}

	public Lokasi getLokasi() {
		return mLokasi;
	}
	
	
	private 		Lokasi 	mLokasi;
	private final 	Context mContext;
	private final 	Handler mHandler;
}
