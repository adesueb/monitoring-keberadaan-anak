package org.ade.monak.ortu.boundary.submenu;

import java.util.List;

import org.ade.monak.ortu.R;
import org.ade.monak.ortu.boundary.PendaftaranMonitoring;
import org.ade.monak.ortu.entity.Anak;
import org.ade.monak.ortu.service.storage.DatabaseManagerOrtu;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class PilihAnak{

	public PilihAnak(Context context, DatabaseManagerOrtu databaseManager, Handler handler) {
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
			return new PilihAnakDialog(this, anaks, anak);
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
	private final DatabaseManagerOrtu mDatabaseManager;
	
	private Context mContext;
	private Anak anak;
	
	private final static class PilihAnakDialog extends Dialog{

		public PilihAnakDialog(PilihAnak pilihAnak, List<Anak> anaks, Anak anak) {
			super(pilihAnak.getContext());
			mPilihAnak = pilihAnak;
			mAnaks = anaks;
			this.anak = anak;
		}
		
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.list_general);
			initListAnak();
			initButtonAndTitle();
		}
		
		
		
		private void initButtonAndTitle(){
			Button buttonOk = (Button) findViewById(R.id.listGeneralButtonOk);
			buttonOk.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {

					int len = listView.getCount();
					SparseBooleanArray checked = listView.getCheckedItemPositions();
					for (int i = 0; i < len; i++){
						 if (checked.get(i)) {
							mPilihAnak.setAnak( mAnaks.get(i));
							break;
						 }
					}
					mPilihAnak.getHandler().sendEmptyMessage(PendaftaranMonitoring.ANAK);
					PilihAnakDialog.this.dismiss();
				}
				
			});
			setTitle("Pilih Anak : ");
		}
		
		private void initListAnak(){
			
			if(mAnaks!=null){
				String[] arrAnak = new String[mAnaks.size()];
				int positionAnak = 0;
				for(int i=0;i<mAnaks.size();i++){
					arrAnak[i] = mAnaks.get(i).getNamaAnak();
					if(anak!=null){
						if(mAnaks.get(i).getIdAnak().equals(anak.getIdAnak())){
							positionAnak = i+1;
						}
					}
				}
				ArrayAdapter<String>listAdapter = 
						new ArrayAdapter<String>
							(this.getContext(), android.R.layout.simple_list_item_single_choice, arrAnak);  
				
				listView = (ListView) findViewById(R.id.listGeneral);
				listView.setAdapter(listAdapter);
				if(positionAnak!=0){
					listView.setSelection(positionAnak-1);	
				}
				listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
			}
		}

		private final List<Anak> mAnaks;
		
		private final PilihAnak mPilihAnak;
		private ListView listView;
		
		private final Anak anak;
		
		
	}
	
}
