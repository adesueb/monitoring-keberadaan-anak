package org.ade.monitoring.keberadaan.boundary;

import java.util.List;

import org.ade.monitoring.keberadaan.R;
import org.ade.monitoring.keberadaan.Variable.Operation;
import org.ade.monitoring.keberadaan.boundary.submenu.MultipleChoiceDataMonitoring;
import org.ade.monitoring.keberadaan.entity.Anak;
import org.ade.monitoring.keberadaan.entity.DataMonitoring;
import org.ade.monitoring.keberadaan.map.view.Peta;
import org.ade.monitoring.keberadaan.service.storage.DatabaseManager;
import org.ade.monitoring.keberadaan.util.BundleEntityMaker;
import org.ade.monitoring.keberadaan.util.EntityBundleMaker;
import org.ade.monitoring.keberadaan.util.IFormOperation;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class DaftarMonitoring extends ListActivity implements IFormOperation{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_monak);
		Intent intent = getIntent();
		final Bundle bundle = intent.getExtras();
		Anak anak = EntityBundleMaker.getAnakFromBundle(bundle);
		databaseManager = new DatabaseManager(this);
		dataMonitorings	= databaseManager.getDataMonitoringsByAnak(anak.getIdAnak());
		daftarMonitoringAdapter	= 
				new AdapterDaftarMonitoring(this, R.layout.daftar_monitoring_item, dataMonitorings);
		
		ImageView ivAdd = (ImageView) findViewById(R.id.listMonakIvAdd);
		ivAdd.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				onAdd(bundle);
			}
		});
		
		ImageView ivMap = (ImageView) findViewById(R.id.listMonakMap);
		ivMap.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(DaftarMonitoring.this, Peta.class);
				startActivity(intent);
				finish();
			}
		});
		
		getListView().setAdapter(daftarMonitoringAdapter);
		
	}

	public void onAdd(Bundle bundle) {
		Intent intent = new Intent(this, PendaftaranMonitoring.class);
		intent.putExtras(bundle);
		startActivityForResult(intent, Operation.ADD);
	}

	public void onEdit(Bundle bundle) {
		Intent intent = new Intent(this, PendaftaranMonitoring.class);
		bundle.putBoolean(PendaftaranMonitoring.EXTRA_EDIT, true);
		intent.putExtras(bundle);
		startActivityForResult(intent, Operation.EDIT);
	}

	public void onDelete(Bundle bundle) {
		showDialog(Operation.DELETE, bundle);
	}
	
	
	
	@Override
	protected Dialog onCreateDialog(int id, final Bundle bundle) {
		switch(id){
			case Operation.MULTIPLE_CHOICE:{
				return new MultipleChoiceDataMonitoring(this, bundle);
			}case Operation.DELETE:{
				AlertDialog.Builder alert = new AlertDialog.Builder(this);                 
				alert.setTitle("Perhatian !!!");  
				alert.setMessage("Anda mau menghapus Data Monitoring\ndengan id : "+bundle.getString("id")+"?");                

				final EditText input = new EditText(this); 
				input.setSingleLine(false);
			    input.setLines(3);
				alert.setView(input);

				alert.setPositiveButton("ya", new DialogInterface.OnClickListener() {  
			      
					public void onClick(DialogInterface dialog, int whichButton) {
						DataMonitoring dataMonitoring = EntityBundleMaker.getDataMonitoringFromBundle(bundle);
						dataMonitoring = databaseManager.getDataMonitoringByIdMonitoring(dataMonitoring.getIdMonitoring(), true, true);
						databaseManager.deleteDataMonitoring(dataMonitoring);
						for(DataMonitoring dataMonitoringFor:dataMonitorings){
							if(dataMonitoringFor.getIdMonitoring().equals(dataMonitoring.getIdMonitoring())){
								dataMonitorings.remove(dataMonitoringFor);
							}
						}
						daftarMonitoringAdapter.notifyDataSetChanged();
						dialog.dismiss();
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
		return super.onCreateDialog(id, bundle);
	}
	
	private DatabaseManager 		databaseManager;
	private AdapterDaftarMonitoring daftarMonitoringAdapter;
	private List<DataMonitoring>	dataMonitorings;
	
	private final static class AdapterDaftarMonitoring extends ArrayAdapter<DataMonitoring>{

		public AdapterDaftarMonitoring(DaftarMonitoring daftarMonitoring,
				int textViewResourceId, List<DataMonitoring> objects) {
			super(daftarMonitoring, textViewResourceId, objects);
			
			this.daftarMonitoring	= daftarMonitoring;
			this.resource 			= textViewResourceId;
			this.dataMonitorings	= objects;
		}
		

		@Override
		public int getCount() {
			return dataMonitorings!=null?dataMonitorings.size():0;
		}


		@Override
		public DataMonitoring getItem(int position) {
			return super.getItem(position);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			
			if(dataMonitorings!=null&&dataMonitorings.size()>0){
				
			
				DataMonitoring dataMonitoring = dataMonitorings.get(position);
				
				if(dataMonitoring == null) return null;
				
				LayoutInflater inflater = (LayoutInflater) daftarMonitoring
						
				        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				
				View rowView = inflater.inflate(resource, parent, false);
				
				TextView keterangan	= (TextView) rowView.findViewById(R.id.daftarMonitoringText);
				keterangan.setText(dataMonitoring.getKeterangan());
						
				rowView.setOnLongClickListener(new DaftarMonitoringLongClick(daftarMonitoring, dataMonitoring));
				
				return rowView;
			}else{
				return null;
			}
		}
		
		private final DaftarMonitoring daftarMonitoring;
		private final int resource;
		private List<DataMonitoring> dataMonitorings;
		
	}
	
	private final static class DaftarMonitoringLongClick implements View.OnLongClickListener{

		public DaftarMonitoringLongClick(DaftarMonitoring daftarMonitoring, DataMonitoring dataMonitoring){
			mDaftarMonitoring 	= daftarMonitoring;
			mDataMonitoring 	= dataMonitoring;
		}
		public boolean onLongClick(View arg0) {
			
			DataMonitoring dataMonitoring = mDataMonitoring;
			Anak anak = mDataMonitoring.getAnak();
			
			if(anak==null){
				dataMonitoring = mDaftarMonitoring.databaseManager.getDataMonitoringByIdMonitoring(mDataMonitoring.getIdMonitoring(), true, true);
			}
			
			mDaftarMonitoring.showDialog
				(Operation.MULTIPLE_CHOICE, BundleEntityMaker.makeBundleFromDataMonitoring(dataMonitoring));
			return false;
		}
		private final DataMonitoring mDataMonitoring;
		private final DaftarMonitoring mDaftarMonitoring;
	}

	public void onSave(Bundle bundle, int status) {
		
	}
	
	

}
