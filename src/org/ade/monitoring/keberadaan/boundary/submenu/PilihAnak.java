package org.ade.monitoring.keberadaan.boundary.submenu;

import java.util.List;

import org.ade.monitoring.keberadaan.boundary.PendaftaranMonitoring;
import org.ade.monitoring.keberadaan.entity.Anak;
import org.ade.monitoring.keberadaan.storage.DatabaseManager;

import android.R;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class PilihAnak extends Dialog{

	public PilihAnak(Context context, DatabaseManager databaseManager, Handler handler) {
		super(context);
		setContentView(org.ade.monitoring.keberadaan.R.layout.list_general);
		this.databaseManager = databaseManager;
		mHandler = handler;
		
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initListAnak();
		initButtonAndTitle();
	}


	private void initListAnak(){
		anaks = databaseManager.getAllAnak(false, false);
		String[] arrAnak = new String[anaks.size()];
		for(int i=0;i<anaks.size();i++){
			arrAnak[i] = anaks.get(i).getNamaAnak();
		}
		ArrayAdapter<String>listAdapter = 
				new ArrayAdapter<String>
					(this.getContext(), R.layout.simple_list_item_single_choice, arrAnak);  
		
		listView = (ListView) findViewById(org.ade.monitoring.keberadaan.R.id.listGeneral);
		listView.setAdapter(listAdapter);
	}
	
	private void initButtonAndTitle(){
		Button buttonOk = (Button) findViewById(org.ade.monitoring.keberadaan.R.id.listGeneralButtonOk);
		buttonOk.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				anak = anaks.get(listView.getSelectedItemPosition());
				mHandler.sendEmptyMessage(PendaftaranMonitoring.ANAK);
				PilihAnak.this.dismiss();
			}
			
		});
		TextView title = (TextView) findViewById(org.ade.monitoring.keberadaan.R.id.listGeneralTitle);
		title.setText("Pilih Anak");
	}
	
	public Anak getAnak(){
		return anak;
	}
	
	private final Handler mHandler;
	private final DatabaseManager databaseManager;
	private ListView listView;
	private List<Anak> anaks;
	private Anak anak;

}
