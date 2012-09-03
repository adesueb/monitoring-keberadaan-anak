package org.ade.monitoring.keberadaan.boundary.submenu;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.ade.monitoring.keberadaan.R;
import org.ade.monitoring.keberadaan.boundary.PendaftaranMonitoring;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

public class PilihWaktu extends Dialog{

	public PilihWaktu(Context context, Handler handler) {
		super(context);
		mHandler = handler;
		setContentView(R.layout.monitoring_waktu);
		Button buttonOk = (Button) findViewById(R.id.monitoringWaktuBtnOk);
		buttonOk.setOnClickListener( new View.OnClickListener() {
			
			public void onClick(View v) {
				
				mHandler.sendEmptyMessage(PendaftaranMonitoring.WAKTU);
			}
		});
	}
	
	public List<Long> getWaktus(){
		List<Long> waktus = new ArrayList<Long>();
		TimePicker time1 = (TimePicker) findViewById(R.id.monitoringTime1);
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, time1.getCurrentHour()+1);
		cal.set(Calendar.MINUTE, time1.getCurrentMinute());
		waktus.add(cal.getTimeInMillis());
		TimePicker time2 = (TimePicker) findViewById(R.id.monitoringTime2);
		Calendar cal2 = Calendar.getInstance();
		cal2.set(Calendar.HOUR_OF_DAY, time2.getCurrentHour()+1);
		cal2.set(Calendar.MINUTE, time2.getCurrentMinute());
		waktus.add(cal2.getTimeInMillis());
		return waktus;
	}
	
	private Handler mHandler;

}
