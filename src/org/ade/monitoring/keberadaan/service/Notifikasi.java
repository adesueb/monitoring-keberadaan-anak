package org.ade.monitoring.keberadaan.service;


import org.ade.monitoring.keberadaan.R;
import org.ade.monitoring.keberadaan.Variable.TipePesanData;
import org.ade.monitoring.keberadaan.entity.Anak;
import org.ade.monitoring.keberadaan.entity.DataMonitoring;
import org.ade.monitoring.keberadaan.entity.Peringatan;
import org.ade.monitoring.keberadaan.map.Peta;
import org.ade.monitoring.keberadaan.service.storage.DatabaseManager;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;


/*
 * FIXME : fix this class for notification........
 */

public class Notifikasi {

  
	public Notifikasi (Context context) {
		String ns = Context.NOTIFICATION_SERVICE;
		mNotificationManager = (NotificationManager) context.getSystemService(ns);
		this.context = context;
	}
	
	public void tampilkanNotifikasiPeringatan(Peringatan peringatan){
		
		int icon = R.drawable.pelanggaran;
		CharSequence tickerText 	= "";
		CharSequence contentTitle 	= "";
		long when = System.currentTimeMillis();
		
		DatabaseManager databaseManager = new DatabaseManager(context);
		DataMonitoring 	dataMonitoring 	= 
				databaseManager.getDataMonitoringByIdMonitoring(peringatan.getIdMonitoring(), true, true);

		Anak anak = dataMonitoring.getAnak();
		
		if(peringatan.getTipe()==TipePesanData.PERINGATAN_TERLARANG){
			tickerText 		= anak.getNamaAnak()+" melakukan pelanggaran..."+"!!!PERINGATAN TERLARANG"+"!!!";
			contentTitle 	= "PERINGATAN TERLARANG";
		}else{
			tickerText = anak.getNamaAnak()+" melakukan pelanggaran..."+"!!!PERINGATAN SEHARUSNYA"+"!!!";
			contentTitle 	= "PERINGATAN SEHARUSNYA";
		}
		
		Notification notification 	= new Notification(icon, tickerText, when);
		CharSequence contentText 	= "anak : "+anak.getNamaAnak()+"\n"+"click disini untuk lihat lokasi";
		Intent notificationIntent 	= new Intent(context, Peta.class);
		notificationIntent.putExtra(Peta.EXTRA_PELANGGARAN, true);
		
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

		notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
		
		int HELLO_ID = 1;

		mNotificationManager.notify(HELLO_ID, notification);
	}
  
	private Context context;
	
	private NotificationManager mNotificationManager;

}
