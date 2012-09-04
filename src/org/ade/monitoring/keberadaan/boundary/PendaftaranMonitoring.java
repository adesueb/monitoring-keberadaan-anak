package org.ade.monitoring.keberadaan.boundary;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.ade.monitoring.keberadaan.R;
import org.ade.monitoring.keberadaan.boundary.submenu.PilihAnak;
import org.ade.monitoring.keberadaan.boundary.submenu.PilihMingguan;
import org.ade.monitoring.keberadaan.boundary.submenu.PilihToleransi;
import org.ade.monitoring.keberadaan.boundary.submenu.PilihWaktu;
import org.ade.monitoring.keberadaan.entity.Anak;
import org.ade.monitoring.keberadaan.entity.DataMonitoring;
import org.ade.monitoring.keberadaan.entity.Lokasi;
import org.ade.monitoring.keberadaan.map.Peta;
import org.ade.monitoring.keberadaan.storage.DatabaseManager;
import org.ade.monitoring.keberadaan.tanda.ITandaLokasi;
import org.ade.monitoring.keberadaan.tanda.TandaLokasiSendiri;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
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
		pilihToleransi 	= new PilihToleransi(this, mHandler);
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
		showDialog(STATUS_LOKASI);
	}
	
	private void actionPilihToleransi(){
		showDialog(TOLERANSI);
	}
	
	private void actionOk(){
		if(dataMonitoring!=null){
			databaseManager.addDataMonitoring(dataMonitoring);
		}
	}
	
	private void actionCancel(){
		finish();
	}
	
	private void actionTandaSeharusnya(){
		dataMonitoring.setStatus(DataMonitoring.SEHARUSNYA);
	}
	
	private void actionTandaTerlarang(){
		dataMonitoring.setStatus(DataMonitoring.TERLARANG);
	}
	
	private void actionTandaiDariMap(){
		Intent intent = new Intent(this, Peta.class);
		startActivityForResult(intent, LOKASI);
	}
	
	private void actionTandaiSendiri(){
		tandaLokasi = new TandaLokasiSendiri(this, mHandler);
		tandaLokasi.actionTandaiLokasi();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode){
			case LOKASI:{
				if(resultCode==RESULT_OK && data!=null){
					Lokasi lokasi = new Lokasi();
					lokasi.setLatitude(data.getDoubleExtra("latitude", 0));
					lokasi.setLongitude(data.getDoubleExtra("longitude", 0));
					dataMonitoring.setLokasi(lokasi);
				}
				break;
			}
		}
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
			}case STATUS_LOKASI:{
				final CharSequence[] items = {"Seharusnya", "Terlarang"};

				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("Pilih Status Tanda");
				builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
				    public void onClick(DialogInterface dialog, int item) {
				    	switch(item){
				    		case 0:{
				    			actionTandaSeharusnya();
				    			break;
				    		}case 1:{
				    			actionTandaTerlarang();
				    			break;
				    		}
				    	}
				    	showDialog(LOKASI);
				    }
				});
				AlertDialog alert = builder.create();
				return alert;
			}case TOLERANSI:{
				return pilihToleransi;
			}case LOKASI:{
				final CharSequence[] items = {"Sendiri","Dari Map"};

				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("Pilih Tandai");
				builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
				    public void onClick(DialogInterface dialog, int item) {
				    	switch(item){
				    		case 0:{
				    			actionTandaiSendiri();
				    			break;
				    		}case 1:{
				    			actionTandaiDariMap();
				    			break;
				    		}
				    	}
				    }
				});
				AlertDialog alert = builder.create();
				return alert;
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
					dataMonitoring.setLokasi(tandaLokasi.getLokasi());
					break;
				}case TOLERANSI:{
					dataMonitoring.setTolerancy(pilihToleransi.getToleransi());
					break;
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
	private PilihToleransi 	pilihToleransi;
	private ITandaLokasi	tandaLokasi;
	
	private DataMonitoring dataMonitoring;
	
	public final static int ANAK			= 0;
	public final static int WAKTU			= 1;
	public final static int TANGGAL			= 2;
	public final static int MINGGUAN		= 3;
	public final static int STATUS_LOKASI	= 4;
	public final static int TOLERANSI		= 5;
	public final static int LOKASI			= 6;
	
}