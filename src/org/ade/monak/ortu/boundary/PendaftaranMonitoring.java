package org.ade.monak.ortu.boundary;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.ade.monak.ortu.R;
import org.ade.monak.ortu.Variable.Status;
import org.ade.monak.ortu.boundary.submenu.PilihAnak;
import org.ade.monak.ortu.boundary.submenu.PilihMingguan;
import org.ade.monak.ortu.boundary.submenu.PilihToleransi;
import org.ade.monak.ortu.boundary.submenu.PilihWaktu;
import org.ade.monak.ortu.entity.Anak;
import org.ade.monak.ortu.entity.DataMonitoring;
import org.ade.monak.ortu.entity.DateMonitoring;
import org.ade.monak.ortu.entity.DayMonitoring;
import org.ade.monak.ortu.entity.Lokasi;
import org.ade.monak.ortu.map.service.TandaLokasiSendiri;
import org.ade.monak.ortu.map.view.Peta;
import org.ade.monak.ortu.service.gate.monak.SenderPesanData;
import org.ade.monak.ortu.service.storage.DatabaseManagerOrtu;
import org.ade.monak.ortu.service.storage.LogMonakFileManager;
import org.ade.monak.ortu.util.BundleEntityMaker;
import org.ade.monak.ortu.util.EntityBundleMaker;
import org.ade.monak.ortu.util.IDGenerator;

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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PendaftaranMonitoring extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.pendaftaran_monitoring);
		
		isEdit = getIntent().getBooleanExtra(EXTRA_EDIT, false);
		
		justView = getIntent().getBooleanExtra(EXTRA_JUST_VIEW, false);
	
		databaseManager = new DatabaseManagerOrtu(this);
		dataMonitoring 	= EntityBundleMaker.getDataMonitoringFromBundle(getIntent().getExtras());
		mIDGenerator	= new IDGenerator(this,databaseManager);
		
		String idMonitoring = "";
		if(dataMonitoring==null){
			dataMonitoring = new DataMonitoring();
			
			idMonitoring = mIDGenerator.getIdMonitoring();	
		}else{
			idMonitoring = dataMonitoring.getIdMonitoring();
		}

		TextView textId= (TextView) findViewById(R.id.monitoringTextId);
		textId.setText(idMonitoring);
		dataMonitoring.setIdMonitoring(idMonitoring);
		
		Bundle bundle = getIntent().getExtras();
		Anak anak = EntityBundleMaker.getAnakFromBundle(bundle);
		if(anak!=null){
			dataMonitoring.setAnak(anak);
			TextView txt = (TextView) findViewById(R.id.monitoringTextAnak);
			txt.setText(anak.getNamaAnak());
		}

		if(justView){
			Button buttonOk 	= (Button) findViewById(R.id.monitoringButtonOk);
			Button buttonClear = (Button) findViewById(R.id.monitoringButtonClear);
			buttonOk.setEnabled(false);
			buttonClear.setEnabled(false);
		}else{
			initAllButton();
		}
		
		initSubMenu();
		
		if(isEdit){
			dataMonitoring = databaseManager.getDataMonitoringByIdMonitoring(dataMonitoring.getIdMonitoring(), true, true);
			initEditMode();
		}
		

	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	
		switch(requestCode){
			case LOKASI:{
				if(resultCode==RESULT_OK && data!=null){
				
					Lokasi lokasi = new Lokasi();
					lokasi.setId(mIDGenerator.getIdLocation());
					lokasi.setLatitude(data.getDoubleExtra("latitude", 0));
					lokasi.setLongitude(data.getDoubleExtra("longitude", 0));
					dataMonitoring.setLokasi(lokasi);
					
					TextView textLokasi = (TextView) findViewById(R.id.monitoringTextLokasi);
					textLokasi.setText(lokasi.getlatitude()+", "+lokasi.getLongitude());
				}
				break;
			}
		}
	}
	
	private void initSubMenu(){
		// ade : not dialog... harus get Dialog dulu buat dapet dialog....
		pilihAnak 		= new PilihAnak(this, databaseManager, new HandlerMonitoring(this));
		//................................................................
		
		// ade : dialog...................................................
		pilihWaktu 		= new PilihWaktu(this, new HandlerMonitoring(this));
		pilihMingguan 	= new PilihMingguan(this, new HandlerMonitoring(this));
		pilihToleransi 	= new PilihToleransi(this, new HandlerMonitoring(this));
		//................................................................
		
	}

	private void initAllButton(){
//		LinearLayout buttonAnak 		= (LinearLayout) findViewById(R.id.monitoringButtonAnak);
//		buttonAnak.setOnClickListener(new OnClickListener() {
//			
//			public void onClick(View v) {actionPilihAnak();}
//		}); ditutup karena sudah ada anak otomatis...
		
		LinearLayout buttonStatus		= (LinearLayout) findViewById(R.id.monitoringButtonStatus);
		buttonStatus.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {actionPilihStatus();}
		});
		
		LinearLayout buttonKeterangan	= (LinearLayout) findViewById(R.id.monitoringButtonKeterangan);
		buttonKeterangan.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {actionPilihKeterangan();}
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
		
		Button buttonClear = (Button) findViewById(R.id.monitoringButtonClear);
		buttonClear.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				actionClear();
			}
		});
	}
	
	private void initEditMode(){
		if(dataMonitoring!=null){
			Anak anak = dataMonitoring.getAnak();
			if(anak!=null){
				pilihAnak.setAnak(anak);
				TextView txt = (TextView) findViewById(R.id.monitoringTextAnak);
				txt.setText(anak.getNamaAnak());
			}
			
			if(dataMonitoring.getWaktuMulai()!=0){
				List<Long> waktus = new ArrayList<Long>();
				long waktu1 = dataMonitoring.getWaktuMulai();
				long waktu2 = dataMonitoring.getWaktuSelesai();
				waktus.add(waktu1);
				waktus.add(waktu2);
				
				pilihWaktu.setWaktus(waktus);
				
				TextView textWaktu = (TextView) findViewById(R.id.monitoringTextWaktu);
				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(waktus.get(0));
				String stringWaktu = cal.get(Calendar.HOUR_OF_DAY)+":"+cal.get(Calendar.MINUTE)+" s/d ";
				cal.setTimeInMillis(waktus.get(1));
				stringWaktu += cal.get(Calendar.HOUR_OF_DAY)+":"+cal.get(Calendar.MINUTE);
				textWaktu.setText(stringWaktu);
				
			}
			
			List<DayMonitoring> haris = dataMonitoring.getHaris();
			if(haris!=null && haris.size()>0){
				List<Integer> angkas = new ArrayList<Integer>();
				for(DayMonitoring day:haris){
					angkas.add(day.getHari());
				}
				pilihMingguan.setHaris(angkas);
				TextView textHaris = (TextView) findViewById(R.id.monitoringTextMingguan);
				String stringHari = "";
				for(int hari:angkas){
					stringHari += hari+",";
				}
				if(!stringHari.equals("")){
					stringHari = stringHari.substring(0, stringHari.length()-1);	
				}
				textHaris.setText(stringHari);
			}
			
			int toleransi = dataMonitoring.getTolerancy();
			if(toleransi>0){
				pilihToleransi.setToleransi(toleransi);
				TextView textToleransi = (TextView) findViewById(R.id.monitoringTextToleransi);
				textToleransi.setText(pilihToleransi.getToleransi()+"m");
			}
			
			TextView textKeterangan = 
					(TextView) findViewById(R.id.monitoringTextKeterangan);
	    	textKeterangan.setText(dataMonitoring.getKeterangan());
	    	
	    	
	    	List<DateMonitoring> tanggals = dataMonitoring.getTanggals();
	    	if(tanggals!=null && tanggals.size()>0){
	    		
	    		Calendar cal = Calendar.getInstance();
	    		
	    		DateMonitoring tanggal = tanggals.get(tanggals.size()-1);
				long timeMillis = tanggal.getDate();
				cal.setTimeInMillis(timeMillis);
				
	    		TextView tv = (TextView) findViewById(R.id.monitoringTextTanggal);
	    		tv.setText(cal.get(Calendar.DATE)+"-"+
						(cal.get(Calendar.MONTH)+1)+"-"+
							cal.get(Calendar.YEAR));
	    		
	    	}
	    	
	    	int status = dataMonitoring.getStatus();
	    	
	    	TextView textStatus= (TextView) findViewById(R.id.monitoringTextStatus);
			if(status== DataMonitoring.SEHARUSNYA){
				textStatus.setText("Seharusnya");
			}else{
				textStatus.setText("Terlarang");
			}
			
			Lokasi lokasi = dataMonitoring.getLokasi();
			if(lokasi!=null){
				TextView textLokasi = (TextView) findViewById(R.id.monitoringTextLokasi);
				textLokasi.setText(lokasi.getlatitude()+", "+lokasi.getLongitude());
			}
	    	
		}
	}
	
//	private void actionPilihAnak(){
//		showDialog(ANAK);
//	}
	
	private void actionPilihStatus() {
		showDialog(STATUS_LOKASI);
	}
	
	private void actionPilihKeterangan() {
		showDialog(KETERANGAN);
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
		showDialog(LOKASI);
	}
	
	private void actionPilihToleransi(){
		showDialog(TOLERANSI);
	}
	
	private void actionOk(){
		if(dataMonitoring!=null){
			Anak anak = dataMonitoring.getAnak();
			Lokasi lokasi = dataMonitoring.getLokasi();
			
			int status = dataMonitoring.getStatus();
			int toleransi = dataMonitoring.getTolerancy();
			long waktuMulai = dataMonitoring.getWaktuMulai();
			if(toleransi == 0){
				dataMonitoring.setTolerancy(100);
			}
			
			if(status == 0){
				dataMonitoring.setStatus(DataMonitoring.SEHARUSNYA);
			}
			
			if(waktuMulai == 0){

				Toast.makeText(this, "pilih Waktu terlebih dahulu!!!", Toast.LENGTH_SHORT).show();
				return;
			}
			
			if(lokasi==null){

				Toast.makeText(this, "pilih Lokasi terlebih dahulu!!!", Toast.LENGTH_SHORT).show();
				
				return;
			}
			
			if(anak!=null){
				SenderPesanData sender = new SenderPesanData(this,new HandlerSendermonitoring(this));		
				if(isEdit){
					sender.sendDataMonitoringUpdate(dataMonitoring);
				}else{
					sender.sendDataMonitoringBaru(dataMonitoring);	
				}
				
			}else{
				Toast.makeText(this, "pilih anak terlebih dahulu!!!", Toast.LENGTH_SHORT).show();
			}
			
		}
	}
	
	private void save(){
		if(isEdit){
			dataMonitoring.setAktif(false);
			databaseManager.updateDataMonitoring(dataMonitoring);
		}else{
			databaseManager.addDataMonitoring(dataMonitoring);	
		}
		Intent intent = new Intent();
		intent.putExtras(BundleEntityMaker.makeBundleFromDataMonitoring(dataMonitoring));
		setResult(RESULT_OK, intent);
		finish();
	}
	
	private void actionClear(){
		dataMonitoring = new DataMonitoring();
		startActivity(getIntent()); 
		finish();
		
	}
	
	private void actionTandaSeharusnya(){
		dataMonitoring.setStatus(DataMonitoring.SEHARUSNYA);
		TextView textStatus= (TextView) findViewById(R.id.monitoringTextStatus);
		textStatus.setText("Seharusnya");
	}
	
	private void actionTandaTerlarang(){
		dataMonitoring.setStatus(DataMonitoring.TERLARANG);
		TextView textStatus= (TextView) findViewById(R.id.monitoringTextStatus);
		textStatus.setText("terlarang");
	}
	
	private void actionTandaiDariMap(){
		Intent intent = new Intent(this, Peta.class);
		intent.putExtra(Peta.EXTRA_ACTION, Peta.EXTRA_AMBIL_LOKASI);
		startActivityForResult(intent, LOKASI);
	}
	
	private void actionTandaiSendiri(){
		tandaLokasi = new TandaLokasiSendiri(this, new HandlerMonitoring(this));
		tandaLokasi.actionTandaiLokasi();
	}
	
	

	@Override
	protected Dialog onCreateDialog(int id) {
		
		switch(id){
			case ANAK:{
				return pilihAnak.getDialog();
			}case KETERANGAN : {
				AlertDialog.Builder alert = new AlertDialog.Builder(this);                 
				alert.setTitle("Add Keterangan");  
				alert.setMessage("keterangan");                

				final EditText input = new EditText(this); 
				input.setSingleLine(false);
			    input.setLines(3);
				alert.setView(input);
				
				if(dataMonitoring!=null){
					input.setText(dataMonitoring.getKeterangan());
				}

				alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {  
			      
					public void onClick(DialogInterface dialog, int whichButton) {  
			          
						dataMonitoring.setKeterangan(input.getText().toString());
						TextView textKeterangan = 
								(TextView) findViewById(R.id.monitoringTextKeterangan);
				    	textKeterangan.setText(dataMonitoring.getKeterangan());
						dialog.dismiss();
						return;                  
			         }  
			     });  

				alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {

						dialog.dismiss();
						return;   
					}
				});
				AlertDialog alertDialog = alert.create();
				return alertDialog;
			}case WAKTU:{
				return pilihWaktu;
			}case TANGGAL:{
				Calendar cal = Calendar.getInstance();
				if(dataMonitoring!=null){
					List<DateMonitoring> tanggals = dataMonitoring.getTanggals();
					if(tanggals!=null && tanggals.size()>0){
						DateMonitoring tanggal = tanggals.get(tanggals.size()-1);
						long timeMillis = tanggal.getDate();
						cal.setTimeInMillis(timeMillis);
					}
				}
				return new DatePickerDialog
						(this,new PendaftaranMonitoringDateListener(this),
								cal.get(Calendar.YEAR),
								cal.get(Calendar.MONTH),cal.get(Calendar.DATE));
			}case MINGGUAN:{
				return pilihMingguan;
			}case STATUS_LOKASI:{
				final CharSequence[] items = {"Seharusnya", "Terlarang"};

				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("Pilih Status Tanda");
				
				int pilihan = -1;
				
				if(dataMonitoring!=null){
					pilihan = dataMonitoring.getStatus();
				}
				
				builder.setSingleChoiceItems(items, pilihan, new DialogInterface.OnClickListener() {
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
				    	dialog.dismiss();
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

				    	dialog.dismiss();
				    }
				});
				AlertDialog alert = builder.create();
				return alert;
			}
		}
		return null;
		
	}
	
	private void setMonitoringFromHandlerCalling(int what){
		switch(what){
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
				TextView textWaktu = (TextView) findViewById(R.id.monitoringTextWaktu);
				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(waktus.get(0));
				String stringWaktu = cal.get(Calendar.HOUR_OF_DAY)+":"+cal.get(Calendar.MINUTE)+" s/d ";
				cal.setTimeInMillis(waktus.get(1));
				stringWaktu += cal.get(Calendar.HOUR_OF_DAY)+":"+cal.get(Calendar.MINUTE);
				textWaktu.setText(stringWaktu);
				
				dataMonitoring.setWaktuMulai(waktus.get(0));
				dataMonitoring.setWaktuSelesai(waktus.get(1));
				
				break;
			}case MINGGUAN:{
				List<Integer> haris = pilihMingguan.getHaris();
				List<DayMonitoring> days = new ArrayList<DayMonitoring>();
				TextView textHaris = (TextView) findViewById(R.id.monitoringTextMingguan);
				String stringHari = "";
				for(int hari:haris){
					stringHari += hari+",";
					DayMonitoring day = new DayMonitoring();
					day.setHari(hari);
					day.setDataMonitoring(dataMonitoring);
					days.add(day);
				}
				if(!stringHari.equals("")){
					stringHari = stringHari.substring(0, stringHari.length()-1);	
				}
				textHaris.setText(stringHari);
				
				dataMonitoring.setHaris(days);
				break;
			}case LOKASI:{
				Lokasi lokasi = tandaLokasi.getLokasi();
				lokasi.setId(mIDGenerator.getIdLocation());
				dataMonitoring.setLokasi(lokasi);

				TextView textLokasi = (TextView) findViewById(R.id.monitoringTextLokasi);
				textLokasi.setText(lokasi.getlatitude()+", "+lokasi.getLongitude());
				break;
			}case TOLERANSI:{
				dataMonitoring.setTolerancy(pilihToleransi.getToleransi());
				TextView textToleransi = (TextView) findViewById(R.id.monitoringTextToleransi);
				textToleransi.setText(pilihToleransi.getToleransi()+"m");
				break;
			}case KE_ANAK:{
				Intent intent = new Intent(this, DaftarAnak.class);
				startActivity(intent);
				break;
			}
		}
	}
	
	public DataMonitoring getDataMonitoring() {
		return dataMonitoring;
	}

	public void setDataMonitoring(DataMonitoring dataMonitoring) {
		this.dataMonitoring = dataMonitoring;
	}

	private DatabaseManagerOrtu databaseManager;
	
	private PilihAnak 		pilihAnak;
	private PilihWaktu 		pilihWaktu;
	private PilihMingguan 	pilihMingguan;
	private PilihToleransi 	pilihToleransi;
	private TandaLokasiSendiri	tandaLokasi;
	
	private IDGenerator		mIDGenerator;
	
	private DataMonitoring dataMonitoring;
	
	private boolean isEdit;
	private boolean justView;
	
	public final static String EXTRA_EDIT		= "isEdit";
	public final static String EXTRA_JUST_VIEW	= "justView";
	
	public final static int ANAK			= 0;
	public final static int KETERANGAN		= 1;
	public final static int WAKTU			= 2;
	public final static int TANGGAL			= 3;
	public final static int MINGGUAN		= 4;
	public final static int STATUS_LOKASI	= 5;
	public final static int TOLERANSI		= 6;
	public final static int LOKASI			= 7;
	public final static int KE_ANAK			= 8;
	
	private final static class HandlerSendermonitoring extends Handler{

		public HandlerSendermonitoring(PendaftaranMonitoring pendaftaranMonitoring){
			this.pendaftaranMonitoring = pendaftaranMonitoring;
		}
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
				case Status.SUCCESS:{
					pendaftaranMonitoring.save();
					break;
				}case Status.FAILED:{
					break;
				}
			}
		}
		private PendaftaranMonitoring pendaftaranMonitoring;
	}
	
	private final static class HandlerMonitoring extends Handler{

		public HandlerMonitoring(PendaftaranMonitoring pendaftaran){
			mPendaftaran = pendaftaran;
		}
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			mPendaftaran.setMonitoringFromHandlerCalling(msg.what);
		}
		
		private PendaftaranMonitoring mPendaftaran;
		
	}

	private final static class PendaftaranMonitoringDateListener 
								implements DatePickerDialog.OnDateSetListener{

		public PendaftaranMonitoringDateListener
				(PendaftaranMonitoring pendaftaranMonitoring){
			mPendaftaranMonitoring = pendaftaranMonitoring;
		}
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.YEAR, year);
			cal.set(Calendar.MONTH, monthOfYear);
			cal.set(Calendar.DATE, dayOfMonth);
			List<DateMonitoring> tanggals = new ArrayList<DateMonitoring>();
			DateMonitoring dateMonitoring =  new DateMonitoring();
			dateMonitoring.setDate(cal.getTimeInMillis());
			dateMonitoring.setDataMonitoring(mPendaftaranMonitoring.getDataMonitoring());
			tanggals.add(dateMonitoring);
			
			mPendaftaranMonitoring.getDataMonitoring().setTanggals(tanggals);
			TextView tv = (TextView) mPendaftaranMonitoring.findViewById(R.id.monitoringTextTanggal);
			
			tv.setText(cal.get(Calendar.DATE)+"-"+
					(cal.get(Calendar.MONTH)+1)+"-"+
						cal.get(Calendar.YEAR));
		}
		private PendaftaranMonitoring mPendaftaranMonitoring;
	}
	
}