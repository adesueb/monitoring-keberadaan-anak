package org.ade.monitoring.keberadaan.boundary;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.ade.monitoring.keberadaan.R;
import org.ade.monitoring.keberadaan.boundary.submenu.PilihAnak;
import org.ade.monitoring.keberadaan.boundary.submenu.PilihLokasi;
import org.ade.monitoring.keberadaan.boundary.submenu.PilihMingguan;
import org.ade.monitoring.keberadaan.boundary.submenu.PilihToleransi;
import org.ade.monitoring.keberadaan.boundary.submenu.PilihWaktu;
import org.ade.monitoring.keberadaan.entity.Anak;
import org.ade.monitoring.keberadaan.entity.DataMonitoring;
import org.ade.monitoring.keberadaan.storage.DatabaseManager;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PendaftaranMonitoring extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pendaftaran_monitoring);
		databaseManager = new DatabaseManager(this);
		initAllButton();
		initSubMenu();
		dataMonitoring = new DataMonitoring();
	}
	
	private void initSubMenu(){
		pilihAnak 		= new PilihAnak(this, databaseManager, mHandler);
		pilihWaktu 		= new PilihWaktu(this, mHandler);
		pilihMingguan 	= new PilihMingguan(this, mHandler);
		pilihLokasi 	= new PilihLokasi(this);
		pilihToleransi 	= new PilihToleransi(this);
	}

	private void initAllButton(){
		LinearLayout buttonAnak 		= (LinearLayout) findViewById(R.id.monitoringButtonAnak);
		buttonAnak.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {actionPilihAnak();}
		});
		
		LinearLayout buttonWaktu 		= (LinearLayout) findViewById(R.id.monitoringButtonWaktu);
		buttonWaktu.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {actionPilihWaktu();}
		});
		
		LinearLayout buttonMingguan 	= (LinearLayout) findViewById(R.id.monitoringButtonMingguan);
		buttonMingguan.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {actionPilihMingguan();}
		});
		
		LinearLayout buttonTanggal 		= (LinearLayout) findViewById(R.id.MonitoringButtonTanggal);
		buttonTanggal.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {actionPilihTanggal();}
		});
		
		LinearLayout buttonLokasi 		= (LinearLayout) findViewById(R.id.monitoringButtonLokasi);
		buttonLokasi.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {actionPilihLokasi();}
		});
		
		LinearLayout buttonToleransi 	= (LinearLayout) findViewById(R.id.monitoringButtonToleransi);
		buttonToleransi.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {actionPilihToleransi();}
		});
		
		Button buttonOk 	= (Button) findViewById(R.id.monitoringButtonOk);
		buttonOk.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {				
				actionOk();
			}
		});
		
		Button buttonCancel = (Button) findViewById(R.id.monitoringButtonCancel);
		buttonCancel.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				actionCancel();
			}
		});
	}
	
	private void actionPilihAnak(){
		showDialog(ANAK);
	}
	
	private void actionPilihWaktu(){
		showDialog(WAKTU);
	}
	
	private void actionPilihMingguan(){
		showDialog(MINGGUAN);
	}
	
	private void actionPilihTanggal(){
		showDialog(TANGGAL);
	}
	
	private void actionPilihLokasi(){
		
	}
	
	private void actionPilihToleransi(){
		
	}
	
	private void actionOk(){
		
	}
	
	private void actionCancel(){
		finish();
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		
		switch(id){
			case ANAK:{
				return pilihAnak;
			}case WAKTU:{
				return pilihWaktu;
			}case TANGGAL:{
				Calendar cal = Calendar.getInstance();
				return new DatePickerDialog
						(this,this.datePickerListener,cal.get(Calendar.YEAR),
								cal.get(Calendar.MONTH),cal.get(Calendar.DATE));
			}case MINGGUAN:{
				return pilihMingguan;
			}case LOKASI:{
				return pilihLokasi;
			}case TOLERANSI:{
				return pilihToleransi;
			}
		}
		return null;
		
	}
	Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch(msg.what){
				case ANAK :{
					Anak anak = pilihAnak.getAnak(); 
					if(anak !=null){
						dataMonitoring.setAnak(anak);
						TextView textAnak = (TextView) findViewById(R.id.monitoringTextAnak);
						textAnak.setText(pilihAnak.getAnak().getNamaAnak());	
					}
					break;
				}case WAKTU:{
					List<Long> waktus = pilihWaktu.getWaktus();
					dataMonitoring.setWaktuMulai(waktus.get(0));
					dataMonitoring.setWaktuSelesai(waktus.get(1));
					break;
				}case MINGGUAN:{
					List<Integer> haris = pilihMingguan.getHaris();
					dataMonitoring.setHaris(haris);
					break;
				}case LOKASI:{
					
				}
			}
		}
		
	};
	
	private DatePickerDialog.OnDateSetListener datePickerListener = 
			new DatePickerDialog.OnDateSetListener() {
		
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			
			String dateSelected = dayOfMonth+"-"+monthOfYear+"-"+year;
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.YEAR, year);
			cal.set(Calendar.MONTH, monthOfYear);
			cal.set(Calendar.DATE, dayOfMonth);
			List<Long> tanggals = new ArrayList<Long>();
			tanggals.add(cal.getTimeInMillis());
			dataMonitoring.setTanggals(tanggals);
			Toast.makeText(PendaftaranMonitoring.this, "Selected Date is ="
			+dateSelected, Toast.LENGTH_SHORT).show();
		}
	};
	
	private DatabaseManager databaseManager;
	
	private PilihAnak 		pilihAnak;
	private PilihWaktu 		pilihWaktu;
	private PilihMingguan 	pilihMingguan;
	private PilihLokasi 	pilihLokasi;
	private PilihToleransi 	pilihToleransi;
	
	private DataMonitoring dataMonitoring;
	
	public final static int ANAK		= 0;
	public final static int WAKTU		= 1;
	public final static int TANGGAL		= 2;
	public final static int MINGGUAN	= 3;
	public final static int LOKASI		= 4;
	public final static int TOLERANSI	= 5;
	
}
