package org.ade.monitoring.keberadaan.service.gate.monak;

import org.ade.monitoring.keberadaan.Variable.Status;
import org.ade.monitoring.keberadaan.Variable.TipePesanMonak;
import org.ade.monitoring.keberadaan.entity.Anak;
import org.ade.monitoring.keberadaan.entity.Lokasi;
import org.ade.monitoring.keberadaan.map.service.GpsManager;
import org.ade.monitoring.keberadaan.service.gate.ASenderMonak;
import org.ade.monitoring.keberadaan.service.gate.SenderSMS;
import org.ade.monitoring.keberadaan.service.storage.PreferenceMonitoringManager;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class SenderLokasi extends ASenderMonak{
	
	public SenderLokasi(Context context) {
		super (context);
		this.pref 		= new PreferenceMonitoringManager(context);

	}
	
	public void sendLocationSingleRequest(String id){
		GpsManager gpsManager = new GpsManager(getContext(), new LocationReceiverHandler(this));
    	Lokasi lokasi = gpsManager.getLastLokasi();
    	if(lokasi!=null){
    		sendLocationSingleRequest(lokasi, id);	
    	}else{
    		gpsManager.searchLokasi();
    	}
	}

	public void sendLocationSingleRequest(Lokasi lokasi,  String idAnak){
		Anak anak = new Anak();
		anak.setIdAnak(idAnak);
		anak.setLokasi(lokasi);
		sendLocationSingleRequest(anak);
	}
	
	public void sendLocationModeTracking(Anak anak){
		send(anak, TipePesanMonak.RETRIEVE_TRACKING);
	}
	
	public void sendLocationSingleRequest(Anak anak){
		send(anak,TipePesanMonak.RETRIEVE_LOCATION_ANAK);
	}
	
	private void send(Anak anak, int tipe){
		Lokasi lokasi = anak.getLokasi();
		String cvs = tipe+","+lokasi.getlatitude()+","+lokasi.getLongitude()+","+anak.getIdAnak();
		SenderSMS senderSms = new SenderSMS(getContext(), null);
		senderSms.sendSMS(pref.getNoHpOrtu(), cvs);
	}
	
	@Override
	public void success(int tipeKoneksi) {

	}

	@Override
	public void failed(int tipeKoneksi) {		
		//TODO : send with internet...
	}
	
	private PreferenceMonitoringManager pref;
	
	
	private static class LocationReceiverHandler extends Handler{

		public LocationReceiverHandler(SenderLokasi senderLokasi){
			this.senderLokasi = senderLokasi;
		}
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch(msg.what){
				case Status.SUCCESS:{
					Bundle bundle = msg.getData();
					if(bundle!=null){
						Lokasi lokasi = new Lokasi();
						lokasi.setLatitude(bundle.getDouble("latitude"));
						lokasi.setLongitude(bundle.getDouble("longitude"));
						Anak anak = new Anak();
						anak.setIdAnak("");
						anak.setLokasi(lokasi);
						senderLokasi.sendLocationSingleRequest(anak);
					}
					break;
				}case Status.FAILED:{
					break;
				}
			}
			
		}
		private final SenderLokasi senderLokasi;
		
	}
	
	
}
