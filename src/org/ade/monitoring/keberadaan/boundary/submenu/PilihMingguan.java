package org.ade.monitoring.keberadaan.boundary.submenu;

import java.util.ArrayList;
import java.util.List;

import org.ade.monitoring.keberadaan.R;
import org.ade.monitoring.keberadaan.boundary.PendaftaranMonitoring;

import android.app.Dialog;
import android.content.Context;
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
		setContentView(R.layout.list_general);
		final ListView listView = (ListView) findViewById(R.id.listGeneral);
		String[] arrHari = new String[]{"Minggu","Senin","Selasa","Rabu","Kamis","Jum'at","Sabtu"};
		ArrayAdapter<String>listAdapter = 
				new ArrayAdapter<String>
					(this.getContext(), android.R.layout.simple_list_item_multiple_choice, arrHari);
		listView.setAdapter(listAdapter);
		Button buttonOk = (Button) findViewById(R.id.listGeneralButtonOk);
		buttonOk.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				int len = listView.getCount();
				SparseBooleanArray checked = listView.getCheckedItemPositions();
				for (int i = 0; i < len; i++){
					 if (checked.get(i)) {
						 haris.add(i+1);
					 }
				}
				mHandler.sendEmptyMessage(PendaftaranMonitoring.MINGGUAN);
			}
		});
	}
	public List<Integer> getHaris(){
		return haris;
	}
	private List<Integer> haris = new ArrayList<Integer>();
	private Handler mHandler;

}
