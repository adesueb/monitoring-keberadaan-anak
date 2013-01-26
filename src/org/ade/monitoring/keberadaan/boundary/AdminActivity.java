package org.ade.monitoring.keberadaan.boundary;

import java.util.List;

import org.ade.monitoring.keberadaan.R;
import org.ade.monitoring.keberadaan.entity.Lokasi;
import org.ade.monitoring.keberadaan.service.storage.DatabaseManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class AdminActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.admin_view);
		LinearLayout buttonViewMonitoring =  (LinearLayout) findViewById(R.id.adminButtonViewMonitoring);
		buttonViewMonitoring.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				Intent intent = new Intent(AdminActivity.this, DaftarMonitoring.class);
				startActivity(intent);
			}
		});
		
		LinearLayout buttonViewLokasi = (LinearLayout) findViewById(R.id.adminButtonViewLokasi);
		buttonViewLokasi.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				showDialog(DIALOG_LOKASI);
			}
		});
		
		LinearLayout buttonViewClear = (LinearLayout) findViewById(R.id.adminButtonClear);
		buttonViewClear.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				showDialog(DIALOG_DELETE_ALL);
			}
		});
	}
	
	
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch(id){
			case DIALOG_LOKASI:{
				return makeDialogLokasi();		
			}case DIALOG_DELETE_ALL:{
				AlertDialog.Builder alert = new AlertDialog.Builder(this);                 
				alert.setTitle("Perhatian !!!");  
				alert.setMessage("Anda mau menghapus Semua Data Monitoring dan Lokasi?");                

				alert.setPositiveButton("ya", new DialogInterface.OnClickListener() {  
			      
					public void onClick(DialogInterface dialog, int whichButton) {
						DatabaseManager databaseManager = new DatabaseManager(AdminActivity.this);
				        databaseManager.deleteAllLokasi();
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
			}
		}
		return null;
	}



	private Dialog makeDialogLokasi(){
		final Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.list_general);
		
		final ListView listView = (ListView) dialog.findViewById(R.id.listGeneral);
		
		DatabaseManager databaseManager = new DatabaseManager(this);
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
	
	private static final int DIALOG_LOKASI	= 0;
	private static final int DIALOG_DELETE_ALL		= 1;
	

}
