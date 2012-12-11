package org.ade.monitoring.keberadaan.boundary;

import java.util.ArrayList;
import java.util.List;

import org.ade.monitoring.keberadaan.R;
import org.ade.monitoring.keberadaan.Variable.VariableEntity;
import org.ade.monitoring.keberadaan.map.Peta;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

public class DaftarLaporan extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.daftar_laporan);
		
		setToolbarMenu();
	}
	
	protected Dialog onCreateDialog(int id) {
  		super.onCreateDialog(id);
  		final Dialog dialog = new Dialog(this);
  		dialog.setContentView(R.layout.list_general);
		
		final ListView listView = (ListView) dialog.findViewById(R.id.listGeneral);
		
		
		ArrayAdapter<String>listAdapter = 
				new ArrayAdapter<String>
					(this, android.R.layout.simple_list_item_single_choice, ARR_REPORT);
		
		listView.setAdapter(listAdapter);
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		Button buttonOk = (Button) dialog.findViewById(R.id.listGeneralButtonOk);
		buttonOk.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				int len = listView.getCount();
				SparseBooleanArray checked = listView.getCheckedItemPositions();
				for (int i = 0; i < len; i++){
					 if (checked.get(i)) {
						 refreshLaporan(i);
					 }
				}
					
				dialog.dismiss();
			}
		});
		return dialog;
	}
  	
	private void refreshLaporan(int pilihanLaporan){
		switch(pilihanLaporan){
			case 0:{
				
				break;
			}case 1:{
				break;
			}case 2:{
				break;
			}case 3:{
				break;
			}case 4:{
				break;
			}
		}
	}
	
	private void setToolbarMenu(){
  		ImageView ivSearch 	= (ImageView) findViewById(R.id.laporanSearch);		
		ivSearch.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				showDialog(DIALOG_SEARCH);
			}
		});
		
		ImageView ivMap 	= (ImageView) findViewById(R.id.laporanMap);		
		ivMap.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				Intent intent = new Intent(DaftarLaporan.this, Peta.class); 
				startActivity(intent);
				finish();
			}
		});
		
		ImageView ivAdvance 	= (ImageView) findViewById(R.id.laporanAdvance);		
		ivAdvance.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				
			}
		});
  	}


	public static final String [] ARR_REPORT = {
		"Pelanggaran", 
		"Anak", 
		"Monitoring", 
		"Terlarang",
		"Seharusnya"};
	
	private final static int DIALOG_SEARCH		= 0;
}
