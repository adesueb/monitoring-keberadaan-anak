package org.ade.monitoring.keberadaan.boundary.submenu;

import java.util.ArrayList;
import java.util.List;

import org.ade.monitoring.keberadaan.R;
import org.ade.monitoring.keberadaan.boundary.PendaftaranMonitoring;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class PilihMingguan extends Dialog{

	public PilihMingguan(Context context, Handler handler) {
		super(context);
		mHandler = handler;
		setTitle("Pilih Mingguan : ");

		setContentView(R.layout.list_general);

		listView = (ListView) findViewById(R.id.listGeneral);
		String[] arrHari = new String[]{"Minggu","Senin","Selasa","Rabu","Kamis","Jum'at","Sabtu"};
		
		ArrayAdapter<String>listAdapter = 
				new ArrayAdapter<String>
					(this.getContext(), android.R.layout.simple_list_item_multiple_choice, arrHari);
		listView.setAdapter(listAdapter);
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		Button buttonOk = (Button) findViewById(R.id.listGeneralButtonOk);
		buttonOk.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				int len = listView.getCount();
				SparseBooleanArray checked = listView.getCheckedItemPositions();
				haris.clear();
				for (int i = 0; i < len; i++){
					 if (checked.get(i)) {
						 haris.add(i+1);
					 }
				}
				mHandler.sendEmptyMessage(PendaftaranMonitoring.MINGGUAN);
				dismiss();
			}
		});
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		

		
		
	}

	public void setHaris(List<Integer> haris){
		this.haris = haris;
		if(haris!=null&&haris.size()>0){
			for(int hari:haris){
				listView.setItemChecked(hari-1, true);
			}
		}
	}
	
	public List<Integer> getHaris(){
		return haris;
	}
	
	private ListView listView;
	private List<Integer> haris = new ArrayList<Integer>();
	private Handler mHandler;

}
