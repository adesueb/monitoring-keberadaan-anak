package org.ade.monitoring.keberadaan.service;


import org.ade.monitoring.keberadaan.R;
import org.ade.monitoring.keberadaan.entity.Peringatan;
import org.ade.monitoring.keberadaan.map.Peta;

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
		CharSequence tickerText = "Hello";
		long when = System.currentTimeMillis();

		Notification notification 	= new Notification(icon, tickerText, when);
		CharSequence contentTitle 	= "My notification";
		CharSequence contentText 	= "Hello World!";
		Intent notificationIntent 	= new Intent(context, Peta.class);
		
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

		notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
		
		int HELLO_ID = 1;

		mNotificationManager.notify(HELLO_ID, notification);
	}
  
	private Context context;
	
	private NotificationManager mNotificationManager;

}
