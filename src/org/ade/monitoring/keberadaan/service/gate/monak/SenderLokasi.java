package org.ade.monitoring.keberadaan.service.gate.monak;

import org.ade.monitoring.keberadaan.Variable.Status;
import org.ade.monitoring.keberadaan.Variable.TipePesanMonak;
import org.ade.monitoring.keberadaan.entity.Anak;
import org.ade.monitoring.keberadaan.entity.Lokasi;
import org.ade.monitoring.keberadaan.map.service.GpsManager;
import org.ade.monitoring.keberadaan.service.gate.SenderSMS;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class SenderLokasi {
	
	public SenderLokasi(Context context) {
		this.context = context;
	}

	public void sendLocation(String noHP, String id){
		LocationReceiverHandler locationHandler = new LocationReceiverHandler(this, noHP);
    	GpsManager gpsManager = new GpsManager(context, locationHandler);
    	Lokasi lokasi = gpsManager.getLastLokasi();
    	if(lokasi!=null){
    		
    	}
    	sendLocation(lokasi, noHP, id);
	}

	private void sendLocation(Lokasi lokasi, String noHp, String idAnak){
		Anak anak = new Anak();
		anak.setIdAnak(idAnak);
		anak.setNoHpAnak(noHp);
		anak.setLokasi(lokasi);
		kirimResponseLokasiAnak(anak);
	}
	
	private void kirimResponseLokasiAnak(Anak anak){
		Lokasi lokasi = anak.getLokasi();
		String cvs = TipePesanMonak.RETRIEVE_LOCATION_ANAK+","+lokasi.getlatitude()+","+lokasi.getLongitude()+","+anak.getIdAnak();
		SenderSMS senderSms = new SenderSMS(context, null);
		senderSms.sendSMS(anak.getNoHpAnak(), cvs);
	}
	
	
	private Context context;
	
	private static class LocationReceiverHandler extends Handler{

		public LocationReceiverHandler(SenderLokasi senderLokasi, String noHp){
			this.senderLokasi = senderLokasi;
			this.noHp		= noHp;
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
						anak.setNoHpAnak(noHp);
						anak.setLokasi(lokasi);
						senderLokasi.kirimResponseLokasiAnak(anak);
					}
					break;
				}case Status.FAILED:{
					break;
				}
			}
			
		}
		private final String	noHp;
		private final SenderLokasi senderLokasi;
		
	}
	
	
}
