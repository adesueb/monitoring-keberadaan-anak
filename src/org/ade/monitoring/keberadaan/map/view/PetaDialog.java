package org.ade.monitoring.keberadaan.map.view;

import java.util.ArrayList;
import java.util.List;

import org.ade.monitoring.keberadaan.R;
import org.ade.monitoring.keberadaan.Variable.VariableEntity;
import org.ade.monitoring.keberadaan.entity.Anak;
import org.ade.monitoring.keberadaan.service.storage.DatabaseManager;

import android.app.Dialog;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class PetaDialog {

	public PetaDialog(Peta peta){
		this.peta = peta;
		this.databaseManager = new DatabaseManager(peta);
	}
	

	public Dialog getDialogSearch(){
		if(dialogSearch==null){
			makeDialogSearch();
		}
		return dialogSearch;
	}
	
	
	public Dialog getDialogLog() {
		if(dialogLog==null){
			dialogLog = new Dialog(peta);
			makeDialogLogAnak(dialogLog);
		}
		return dialogLog;
	}

	
	
	public Dialog getDialogTrack() {
		if(dialogTrack==null){
			dialogTrack = new Dialog(peta);	
			makeDialogTrackAnak(dialogTrack);
		}
		return dialogTrack;
	}
	
	private void makeDialogSearch(){
		dialogSearch = new Dialog(peta);
  		dialogSearch.setContentView(R.layout.list_general);
		
		final ListView listView = (ListView) dialogSearch.findViewById(R.id.listGeneral);
		
		
		ArrayAdapter<String>listAdapter = 
				new ArrayAdapter<String>
					(peta, android.R.layout.simple_list_item_multiple_choice, VariableEntity.ARR_ENTITY);
		listView.setAdapter(listAdapter);
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		Button buttonOk = (Button) dialogSearch.findViewById(R.id.listGeneralButtonOk);
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
				
				peta.actionOkSearchDialog(pilihanOverlay);
					
				dialogSearch.dismiss();
			}
		});
	}
	
	private void makeDialogTrackAnak(final Dialog dialog){
		
		dialog.setContentView(R.layout.list_general);
		
		final ListView listView = (ListView) dialog.findViewById(R.id.listGeneral);
		
		final List<Anak> anaks = databaseManager.getAllAnak(false, false);
		String [] namaAnaks = new String[anaks.size()];
		for(int i=0;i<anaks.size();i++){
			namaAnaks[i] = anaks.get(i).getNamaAnak();
		}
		ArrayAdapter<String>listAdapter = 
				new ArrayAdapter<String>
					(peta, android.R.layout.simple_list_item_multiple_choice, namaAnaks);
		listView.setAdapter(listAdapter);
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		
		Button buttonOk = (Button) dialog.findViewById(R.id.listGeneralButtonOk);
		buttonOk.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				List<Integer> pilihanOverlay = null;
				
				int len = listView.getCount();
				pilihanOverlay = new ArrayList<Integer>();
				SparseBooleanArray checked = listView.getCheckedItemPositions();
				for (int i = 0; i < len; i++){
					 if (checked.get(i)) {
						 pilihanOverlay.add(i);
					 }
				}	
				
				peta.actionOkTrackDialog(pilihanOverlay, anaks);	
				
					
				dialog.dismiss();
			}
		});
		
	}
	
	private void makeDialogLogAnak(final Dialog dialog){
		dialog.setContentView(R.layout.list_general);
		
		final ListView listView = (ListView) dialog.findViewById(R.id.listGeneral);
		
		final List<Anak> anaks = databaseManager.getAllAnak(false, false);
		String [] namaAnaks = new String[anaks.size()];
		for(int i=0;i<anaks.size();i++){
			namaAnaks[i] = anaks.get(i).getNamaAnak();
		}
		ArrayAdapter<String>listAdapter = 
				new ArrayAdapter<String>
					(peta, android.R.layout.simple_list_item_multiple_choice, namaAnaks);
		listView.setAdapter(listAdapter);
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		
		Button buttonOk = (Button) dialog.findViewById(R.id.listGeneralButtonOk);
		buttonOk.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				int selected = 0;
				selected = listView.getSelectedItemPosition()+1;
				
				if(selected>0){
					peta.actionOkLogDialog(anaks.get(selected-1));	
				}
					
				dialog.dismiss();
			}
		});
		
	}
	

	private Dialog dialogSearch;
	private Dialog dialogLog;
	private Dialog dialogTrack;
	
	private final Peta 				peta;
	private final DatabaseManager 	databaseManager;
}
