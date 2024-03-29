package org.ade.monak.ortu.boundary.submenu;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.ade.monak.ortu.R;
import org.ade.monak.ortu.boundary.PendaftaranMonitoring;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

public class PilihWaktu extends Dialog{

	public PilihWaktu(Context context, Handler handler) {
		super(context);
		mHandler = handler;
		setTitle("Pilih Waktu : ");

		setContentView(R.layout.monitoring_waktu);
		
		TimePicker time1 = (TimePicker) findViewById(R.id.monitoringTime1);
		time1.setIs24HourView(true);

		TimePicker time2 = (TimePicker) findViewById(R.id.monitoringTime2);
		time2.setIs24HourView(true);
	}
	
	public void setWaktus(List<Long> waktus){
		this.waktus = waktus;
		if(waktus!=null && waktus.size()>0){
			
			Calendar cal = Calendar.getInstance();

			TimePicker time1 = (TimePicker) findViewById(R.id.monitoringTime1);
			long waktu1 = waktus.get(0);
			cal.setTimeInMillis(waktu1);
			time1.setCurrentHour(cal.get(Calendar.HOUR_OF_DAY));
			time1.setCurrentMinute(cal.get(Calendar.MINUTE));
			
			TimePicker time2 = (TimePicker) findViewById(R.id.monitoringTime2);
			long waktu2 = waktus.get(1);
			cal.setTimeInMillis(waktu2);
			time2.setCurrentHour(cal.get(Calendar.HOUR_OF_DAY));
			time2.setCurrentMinute(cal.get(Calendar.MINUTE));
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Button buttonOk = (Button) findViewById(R.id.monitoringWaktuBtnOk);
		
		buttonOk.setOnClickListener( new View.OnClickListener() {
			
			public void onClick(View v) {
				waktus = new ArrayList<Long>();
				TimePicker time1 = (TimePicker) findViewById(R.id.monitoringTime1);
				
				Calendar cal = Calendar.getInstance();
				cal.set(Calendar.HOUR_OF_DAY, time1.getCurrentHour());
				cal.set(Calendar.MINUTE, time1.getCurrentMinute());
				waktus.add(cal.getTimeInMillis());
				
				TimePicker time2 = (TimePicker) findViewById(R.id.monitoringTime2);
				
				Calendar cal2 = Calendar.getInstance();
				cal2.set(Calendar.HOUR_OF_DAY, time2.getCurrentHour());
				cal2.set(Calendar.MINUTE, time2.getCurrentMinute());
				waktus.add(cal2.getTimeInMillis());
				mHandler.sendEmptyMessage(PendaftaranMonitoring.WAKTU);
				dismiss();
			}
		});
	}



	public List<Long> getWaktus(){
		
		return waktus;
	}
	
	private List<Long> waktus = new ArrayList<Long>();
	
	private Handler mHandler;

}
