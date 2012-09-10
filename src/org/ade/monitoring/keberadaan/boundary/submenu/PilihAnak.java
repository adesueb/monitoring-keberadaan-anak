package org.ade.monitoring.keberadaan.boundary.submenu;

import java.util.List;

import org.ade.monitoring.keberadaan.boundary.PendaftaranMonitoring;
import org.ade.monitoring.keberadaan.entity.Anak;
import org.ade.monitoring.keberadaan.storage.DatabaseManager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class PilihAnak{

	public PilihAnak(Context context, DatabaseManager databaseManager, Handler handler) {
		mContext = context;
		mDatabaseManager = databaseManager;
		mHandler = handler;
	}
	
	public Anak getAnak(){
		return anak;
	}
	
	public void setAnak(Anak anak) {
		this.anak = anak;
	}
	

	public Handler getHandler() {
		return mHandler;
	}
	
	public Dialog getDialog(){
		List<Anak> anaks = mDatabaseManager.getAllAnak(false, false);
		if(anaks==null){
			return getDirectionAnakDialog();
		}else{
			return new PilihAnakDialog(this, anaks);
		}
	}
	
	private Dialog getDirectionAnakDialog(){
		AlertDialog.Builder alert = new AlertDialog.Builder(this.getContext());                 
		alert.setTitle("Perhatian !!!");  
		alert.setMessage("Belum ada anak yang terdaftar, daftar beberapa anak?");        
		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {  
			public void onClick(DialogInterface dialog, int whichButton) {  
				mHandler.sendEmptyMessage(PendaftaranMonitoring.KE_ANAK);
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
		return alert.create();
	}
	
	public Context getContext() {
		return mContext;
	}

	public void setContext(Context context) {
		this.mContext = context;
	}

	


	private final Handler mHandler;
	private final DatabaseManager mDatabaseManager;
	
	private Context mContext;
	private Anak anak;
	
	private final static class PilihAnakDialog extends Dialog{

		public PilihAnakDialog(PilihAnak pilihAnak, List<Anak> anaks) {
			super(pilihAnak.getContext());
			mPilihAnak = pilihAnak;
			mAnaks = anaks;
		}
		
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(org.ade.monitoring.keberadaan.R.layout.list_general);
			initListAnak();
			initButtonAndTitle();
		}
		
		
		
		private void initButtonAndTitle(){
			Button buttonOk = (Button) findViewById(org.ade.monitoring.keberadaan.R.id.listGeneralButtonOk);
			buttonOk.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
					mPilihAnak.setAnak( mAnaks.get(listView.getSelectedItemPosition()));
					mPilihAnak.getHandler().sendEmptyMessage(PendaftaranMonitoring.ANAK);
					PilihAnakDialog.this.dismiss();
				}
				
			});
			TextView title = (TextView) findViewById(org.ade.monitoring.keberadaan.R.id.listGeneralTitle);
			title.setText("Pilih Anak");
		}
		
		private void initListAnak(){
			
			if(mAnaks!=null){
				String[] arrAnak = new String[mAnaks.size()];
				for(int i=0;i<mAnaks.size();i++){
					arrAnak[i] = mAnaks.get(i).getNamaAnak();
				}
				ArrayAdapter<String>listAdapter = 
						new ArrayAdapter<String>
							(this.getContext(), android.R.layout.simple_list_item_single_choice, arrAnak);  
				
				listView = (ListView) findViewById(org.ade.monitoring.keberadaan.R.id.listGeneral);
				listView.setAdapter(listAdapter);
			}
		}

		private final List<Anak> mAnaks;
		
		private final PilihAnak mPilihAnak;
		private ListView listView;
		
		
	}
	
}
