package org.ade.monak.ortu.boundary;

import java.util.ArrayList;
import java.util.List;

import org.ade.monak.ortu.R;
import org.ade.monak.ortu.entity.Lokasi;
import org.ade.monak.ortu.service.storage.DatabaseManagerOrtu;
import org.ade.monak.ortu.service.storage.PreferenceMonitoringManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class Setting extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_view);
		LinearLayout buttonViewMonitoring =  (LinearLayout) findViewById(R.id.adminButtonViewMonitoring);
		buttonViewMonitoring.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				Intent intent = new Intent(Setting.this, DaftarMonitoring.class);
				startActivity(intent);
			}
		});
		
		LinearLayout buttonViewLokasi = (LinearLayout) findViewById(R.id.adminButtonViewLokasi);
		buttonViewLokasi.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				showDialog(DIALOG_LOKASI);
			}
		});
		
		LinearLayout buttonSetTipeKoneksi = (LinearLayout) findViewById(R.id.adminButtonSetTipeKoneksi);
		buttonSetTipeKoneksi.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				showDialog(DIALOG_SET_TIPE_KONEKSI);
			}
		});
		
		
		LinearLayout buttonViewClearLokasi = (LinearLayout) findViewById(R.id.adminButtonClearLokasi);
		buttonViewClearLokasi.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				showDialog(DIALOG_CLEAR_LOKASI);
			}
		});
		
		LinearLayout buttonViewClearMonitoring = (LinearLayout) findViewById(R.id.adminButtonClearMonitoring);
		buttonViewClearMonitoring.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				showDialog(DIALOG_CLEAR_MONITORING);
			}
		});
		
		LinearLayout buttonViewClearPelanggaran = (LinearLayout) findViewById(R.id.adminButtonClearPelanggaran);
		buttonViewClearPelanggaran.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				showDialog(DIALOG_CLEAR_PELANGGARAN);
			}
		});
		LinearLayout buttonViewClearAnak = (LinearLayout) findViewById(R.id.adminButtonClearAnak);
		buttonViewClearAnak.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				showDialog(DIALOG_CLEAR_ANAK);
			}
		});
	}
	
	
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch(id){
			case DIALOG_LOKASI:{
				return makeDialogLokasi();		
			}case DIALOG_CLEAR_LOKASI:{
				AlertDialog.Builder alert = new AlertDialog.Builder(this);                 
				alert.setTitle("Perhatian !!!");  
				alert.setMessage("Anda mau menghapus Semua Data Monitoring?");                

				alert.setPositiveButton("ya", new DialogInterface.OnClickListener() {  
			      
					public void onClick(DialogInterface dialog, int whichButton) {
						DatabaseManagerOrtu databaseManager = new DatabaseManagerOrtu(Setting.this);
				        databaseManager.deleteAllLokasi();
				        return;                  
			         }  
			     });  

				alert.setNegativeButton("tidak", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {

						dialog.dismiss();
						return;   
					}
				});
				AlertDialog alertDialog = alert.create();
				return alertDialog;
			}case DIALOG_CLEAR_PELANGGARAN:{
				AlertDialog.Builder alert = new AlertDialog.Builder(this);                 
				alert.setTitle("Perhatian !!!");  
				alert.setMessage("Anda mau menghapus Semua Pelanggaran?");                

				alert.setPositiveButton("ya", new DialogInterface.OnClickListener() {  
			      
					public void onClick(DialogInterface dialog, int whichButton) {
						DatabaseManagerOrtu databaseManager = new DatabaseManagerOrtu(Setting.this);
				        databaseManager.deleteAllPelanggarans();				       
						return;                  
			         }  
			     });  

				alert.setNegativeButton("tidak", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {

						dialog.dismiss();
						return;   
					}
				});
				AlertDialog alertDialog = alert.create();
				return alertDialog;
			}case DIALOG_CLEAR_MONITORING:{
				AlertDialog.Builder alert = new AlertDialog.Builder(this);                 
				alert.setTitle("Perhatian !!!");  
				alert.setMessage("Anda mau menghapus Semua Monitoring?");                

				alert.setPositiveButton("ya", new DialogInterface.OnClickListener() {  
			      
					public void onClick(DialogInterface dialog, int whichButton) {
						DatabaseManagerOrtu databaseManager = new DatabaseManagerOrtu(Setting.this);
						databaseManager.deleteAllDataMonitoring();
						return;                  
			         }  
			     });  

				alert.setNegativeButton("tidak", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {

						dialog.dismiss();
						return;   
					}
				});
				AlertDialog alertDialog = alert.create();
				return alertDialog;
			}case DIALOG_CLEAR_ANAK:{
				AlertDialog.Builder alert = new AlertDialog.Builder(this);                 
				alert.setTitle("Perhatian !!!");  
				alert.setMessage("Anda mau menghapus Semua Anak?");                

				alert.setPositiveButton("ya", new DialogInterface.OnClickListener() {  
			      
					public void onClick(DialogInterface dialog, int whichButton) {
						DatabaseManagerOrtu databaseManager = new DatabaseManagerOrtu(Setting.this);
						databaseManager.deleteAllAnak();
						return;                  
			         }  
			     });  

				alert.setNegativeButton("tidak", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {

						dialog.dismiss();
						return;   
					}
				});
				AlertDialog alertDialog = alert.create();
				return alertDialog;
			}case DIALOG_SET_TIPE_KONEKSI:{
				return makeDialogSetTipeKoneksi();
			}
		}
		return null;
	}


	private Dialog makeDialogSetTipeKoneksi(){
		final PreferenceMonitoringManager pref = new PreferenceMonitoringManager(Setting.this);
		
		final Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.list_general);
		
		final ListView listView = (ListView) dialog.findViewById(R.id.listGeneral);
				
		String [] tipeKoneksis = new String[2];
		tipeKoneksis[0] = "internet";
		tipeKoneksis[1] = "sms";
		
		ArrayAdapter<String>listAdapter = 
				new ArrayAdapter<String>
					(this, android.R.layout.simple_list_item_single_choice, tipeKoneksis);
		listView.setAdapter(listAdapter);
		listView.setSelection(pref.getTipeKoneksi());
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		
		Button buttonOk = (Button) dialog.findViewById(R.id.listGeneralButtonOk);
		buttonOk.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				
				int len = listView.getCount();
				List<Integer> pilihanOverlay = new ArrayList<Integer>();
				SparseBooleanArray checked = listView.getCheckedItemPositions();
				for (int i = 0; i < len; i++){
					 if (checked.get(i)) {
						 pilihanOverlay.add(i);
					 }
				}
				if(pilihanOverlay.size()>0){
					pref.setTipeKoneksi(pilihanOverlay.get(0));	
				}
				dialog.dismiss();
			}
		});
		
		return dialog;
	}

	private Dialog makeDialogLokasi(){
		final Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.list_general);
		
		final ListView listView = (ListView) dialog.findViewById(R.id.listGeneral);
		
		DatabaseManagerOrtu databaseManager = new DatabaseManagerOrtu(this);
		final List<Lokasi> lokasis = databaseManager.getAllLokasi();
		if(lokasis==null){
			Toast.makeText(this, "tidak ada lokasi", Toast.LENGTH_SHORT).show();
			return null;
		}
		String [] idLokasis = new String[lokasis.size()];
		for(int i=0;i<lokasis.size();i++){
			idLokasis[i] = lokasis.get(i).getId();
		}
		ArrayAdapter<String>listAdapter = 
				new ArrayAdapter<String>
					(this, android.R.layout.simple_list_item_multiple_choice, idLokasis);
		listView.setAdapter(listAdapter);
		listView.setChoiceMode(ListView.CHOICE_MODE_NONE);
		
		Button buttonOk = (Button) dialog.findViewById(R.id.listGeneralButtonOk);
		buttonOk.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		
		return dialog;
		
	}
	
	private static final int DIALOG_LOKASI				= 0;
	private static final int DIALOG_CLEAR_LOKASI		= 1;
	private static final int DIALOG_CLEAR_PELANGGARAN	= 2;
	private static final int DIALOG_CLEAR_MONITORING	= 3;
	private static final int DIALOG_CLEAR_ANAK			= 4;
	private static final int DIALOG_SET_TIPE_KONEKSI	= 5;	
}
