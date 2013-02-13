package org.ade.monak.ortu.boundary;

import java.util.ArrayList;
import java.util.List;

import org.ade.monak.ortu.R;
import org.ade.monak.ortu.Variable.Operation;
import org.ade.monak.ortu.Variable.Status;
import org.ade.monak.ortu.boundary.submenu.MultipleChoiceDataMonitoring;
import org.ade.monak.ortu.entity.Anak;
import org.ade.monak.ortu.entity.DataMonitoring;
import org.ade.monak.ortu.map.view.Peta;
import org.ade.monak.ortu.service.BinderHandlerMonak;
import org.ade.monak.ortu.service.MonakService;
import org.ade.monak.ortu.service.gate.monak.SenderPesanData;
import org.ade.monak.ortu.service.gate.monak.SenderStartMonitoring;
import org.ade.monak.ortu.service.gate.monak.SenderStopMonitoring;
import org.ade.monak.ortu.service.storage.DatabaseManagerOrtu;
import org.ade.monak.ortu.service.storage.LogMonakFileManager;
import org.ade.monak.ortu.service.util.IBindMonakServiceConnection;
import org.ade.monak.ortu.service.util.ServiceMonakConnection;
import org.ade.monak.ortu.util.BundleEntityMaker;
import org.ade.monak.ortu.util.EntityBundleMaker;
import org.ade.monak.ortu.util.IFormOperation;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class DaftarMonitoring extends ListActivity implements IFormOperation,  IBindMonakServiceConnection{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_monak);
		Intent intent = getIntent();
		final Bundle bundle = intent.getExtras();
		anak = EntityBundleMaker.getAnakFromBundle(bundle);
		databaseManager = new DatabaseManagerOrtu(this);
		if(anak==null){
			dataMonitorings	= databaseManager.getAllDataMonitorings(true, true);
			if(dataMonitorings!=null && dataMonitorings.size()>0){
				anak = dataMonitorings.get(0).getAnak(); 	
			}
			justView = true;
		}else{
			dataMonitorings	= databaseManager.getDataMonitoringsByAnak(anak.getIdAnak());
		}
		if(dataMonitorings==null){
			dataMonitorings = new ArrayList<DataMonitoring>();
		}
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
		
		if(justView){
			ivAdd.setVisibility(View.GONE);
			ivMap.setVisibility(View.GONE);
		}
		
		getListView().setAdapter(daftarMonitoringAdapter);
		
	}
	
	

	@Override
	protected void onStart() {
		super.onStart();
		bound = false;
		serviceConnection = new ServiceMonakConnection(this);
		bindService(new Intent(MonakService.MONAK_SERVICE), 
													serviceConnection, 
													Context.BIND_AUTO_CREATE);
	}



	@Override
	protected void onStop() {
		super.onStop();
		if(bound){
			handlerBinder.unBindUIHandlerWaitingKonfirmasiAktif();
			unbindService(serviceConnection);
		}
		bound = false;
	}



	public void onAdd(Bundle bundle) {
		Intent intent = new Intent(this, PendaftaranMonitoring.class);
		intent.putExtras(bundle);
		startActivityForResult(intent, Operation.ADD);
	}

	public void onEdit(Bundle bundle) {
		Intent intent = new Intent(this, PendaftaranMonitoring.class);
		bundle.putBoolean(PendaftaranMonitoring.EXTRA_EDIT, true);
		if(justView){
			bundle.putBoolean(PendaftaranMonitoring.EXTRA_JUST_VIEW, true);
		}
		intent.putExtras(bundle);
		startActivityForResult(intent, Operation.EDIT);
	}

	public void onDelete(Bundle bundle) {
		showDialog(Operation.DELETE, bundle);
	}
	
	public void deleteSave(Bundle bundle){
		DataMonitoring dataMonitoring = EntityBundleMaker.getDataMonitoringFromBundle(bundle);
		
		dataMonitoring = databaseManager.getDataMonitoringByIdMonitoring(dataMonitoring.getIdMonitoring(), true, true);
		if(dataMonitoring==null){
			return;
		}
		databaseManager.deleteDataMonitoring(dataMonitoring);
		for(DataMonitoring dataMonitoringFor:dataMonitorings){
			if(dataMonitoringFor.getIdMonitoring().equals(dataMonitoring.getIdMonitoring())){
				dataMonitorings.remove(dataMonitoringFor);
				break;
			}
		}
		daftarMonitoringAdapter.notifyDataSetChanged();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		DataMonitoring dataMonitoring = null;
		if(resultCode==RESULT_OK){
			Bundle bundle = data.getExtras();
			dataMonitoring = EntityBundleMaker.getDataMonitoringFromBundle(bundle);
			dataMonitoring = databaseManager.getDataMonitoringByIdMonitoring(dataMonitoring.getIdMonitoring(), false, false);
			switch(requestCode){
				case Operation.ADD:{
					
					break;
				}case Operation.EDIT:{
					for(DataMonitoring forDataMonitoring:dataMonitorings){
						if(forDataMonitoring.getIdMonitoring().equals(dataMonitoring.getIdMonitoring())){
							dataMonitorings.remove(forDataMonitoring);
							break;
						}
					}
					break;
				}
			}	

			dataMonitorings.add(dataMonitoring);
			daftarMonitoringAdapter.notifyDataSetChanged();
		}
		
	}

	@Override
	protected Dialog onCreateDialog(int id, final Bundle bundle) {
		switch(id){
			case Operation.MULTIPLE_CHOICE:{
				return new MultipleChoiceDataMonitoring(this, bundle);
			}case Operation.DELETE:{
				AlertDialog.Builder alert = new AlertDialog.Builder(this);                 
				alert.setTitle("Perhatian !!!");  
				alert.setMessage("Anda mau menghapus Data Monitoring\ndengan id : "+bundle.getString("idMonitoring")+"?");                

				alert.setPositiveButton("ya", new DialogInterface.OnClickListener() {  
			      
					public void onClick(DialogInterface dialog, int whichButton) {
						DataMonitoring dataMonitoring = EntityBundleMaker.getDataMonitoringFromBundle(bundle);
						dataMonitoring = databaseManager.getDataMonitoringByIdMonitoring(dataMonitoring.getIdMonitoring(), true, true);	
						if(dataMonitoring.isAktif()){
							SenderPesanData sender = new SenderPesanData(DaftarMonitoring.this, new HandlerDeleteSendermonitoring(DaftarMonitoring.this, dataMonitoring));		
							sender.sendDataMonitoringDelete(dataMonitoring);	
						}else{
							databaseManager.deleteDataMonitoring(dataMonitoring);
							for(DataMonitoring dataMonitoringFor:dataMonitorings){
								if(dataMonitoringFor.getIdMonitoring().equals(dataMonitoring.getIdMonitoring())){
									dataMonitorings.remove(dataMonitoringFor);
									break;
								}
							}
							daftarMonitoringAdapter.notifyDataSetChanged();
							SenderPesanData sender = new SenderPesanData(DaftarMonitoring.this, null);		
							sender.sendDataMonitoringDelete(dataMonitoring);			
						}

						dialog.dismiss();
						dismissDialog(Operation.DELETE);
						return;                  
			         }  
			     });  

				alert.setNegativeButton("tidak", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {

						dialog.dismiss();
						dismissDialog(Operation.DELETE);
						return;   
					}
				});
				AlertDialog alertDialog = alert.create();
				return alertDialog;
			}
		}
		return super.onCreateDialog(id, bundle);
	}


	public void setBinderHandlerMonak(BinderHandlerMonak binderHandlerMonak) {
		handlerBinder = binderHandlerMonak;
		handlerBinder.bindUIHandlerWaitingKonfirmasiAktif(new WaitingKonfirmasiAktifHandler(this));
		
	}

	public void setBound(boolean bound) {
		this.bound = bound;
	}	
	
	private void startMonitoring(DataMonitoring dataMonitoring){
		SenderStartMonitoring sender = new SenderStartMonitoring(this);
		dataMonitoring = databaseManager.getDataMonitoringByIdMonitoring(dataMonitoring.getIdMonitoring(), true, true);
		sender.sendStartMonitoring(dataMonitoring);
		Toast.makeText(this, "monitoring:"+dataMonitoring.getKeterangan()+" akan diaktifkan", Toast.LENGTH_SHORT).show();
	}
	
	private void stopMonitoring(DataMonitoring dataMonitoring){
		SenderStopMonitoring sender = new SenderStopMonitoring(this);
		dataMonitoring = databaseManager.getDataMonitoringByIdMonitoring(dataMonitoring.getIdMonitoring(), true, true);
		sender.sendStopMonitoring(dataMonitoring);
		Toast.makeText(this, "monitoring:"+dataMonitoring.getKeterangan()+" akan dinonaktifkan", Toast.LENGTH_SHORT).show();
	}
	

	private ServiceMonakConnection serviceConnection;
	private DatabaseManagerOrtu 		databaseManager;
	private AdapterDaftarMonitoring daftarMonitoringAdapter;
	private List<DataMonitoring>	dataMonitorings;
	private Anak anak;
	private boolean bound	= false;
	private BinderHandlerMonak handlerBinder;
	
	private boolean justView = false;
	
	private static final class BtnStartOnClick implements View.OnClickListener{


		public BtnStartOnClick(DaftarMonitoring daftarMonitoring, DataMonitoring dataMonitoring){
			this.daftarMonitoring 	= daftarMonitoring;
			this.dataMonitoring		= dataMonitoring;
		}
		public void onClick(View v) {
			daftarMonitoring.startMonitoring(dataMonitoring);
		}
		
		private final DaftarMonitoring 	daftarMonitoring;
		private final DataMonitoring	dataMonitoring;
		
		
	}
	
	private static final class BtnStopOnClick implements View.OnClickListener{

		public BtnStopOnClick(DaftarMonitoring daftarMonitoring, DataMonitoring dataMonitoring){
			this.daftarMonitoring 	= daftarMonitoring;
			this.dataMonitoring		= dataMonitoring;
		}
		public void onClick(View v) {
			daftarMonitoring.stopMonitoring(dataMonitoring);
		}
		
		private final DaftarMonitoring 	daftarMonitoring;
		private final DataMonitoring	dataMonitoring;
		
	}
	
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
				
				rowView.setOnClickListener(new DaftarMonitoringClick(daftarMonitoring, dataMonitoring));
				if(!daftarMonitoring.justView){
					rowView.setOnLongClickListener(new DaftarMonitoringLongClick(daftarMonitoring, dataMonitoring));	
				}
				
				LinearLayout llBackground = (LinearLayout) rowView.findViewById(R.id.background);
				
				Button btnAktifMonitoring = (Button) rowView.findViewById(R.id.daftarMonitoringButtonStart);
				btnAktifMonitoring.setOnClickListener(new BtnStartOnClick(daftarMonitoring, dataMonitoring));
			
				
				Button btnDeAktifMonitoring = (Button) rowView.findViewById(R.id.daftarMonitoringButtonStop);				
				btnDeAktifMonitoring.setOnClickListener(new BtnStopOnClick(daftarMonitoring, dataMonitoring));
				
				LinearLayout llAktifMonitoring 	= (LinearLayout) rowView.findViewById(R.id.daftarMonitoringStatusAktif);
				
				LinearLayout llDeAktifMonitoring= (LinearLayout) rowView.findViewById(R.id.daftarMonitoringStatusDeaktif);

				
				if(dataMonitoring.isAktif()){
					llBackground.setBackgroundResource(R.drawable.back_menu_green);
					llDeAktifMonitoring.setVisibility(View.GONE);

					llAktifMonitoring.setVisibility(View.VISIBLE);
				}else{
					llBackground.setBackgroundResource(R.drawable.back_menu);
					llAktifMonitoring.setVisibility(View.GONE);
					llDeAktifMonitoring.setVisibility(View.VISIBLE);
					
				}
				
				
				return rowView;
			}else{
				return null;
			}
		}
		
		private final DaftarMonitoring daftarMonitoring;
		private final int resource;
		private List<DataMonitoring> dataMonitorings;
		
	}
	
	private final static class DaftarMonitoringClick implements View.OnClickListener{

		public DaftarMonitoringClick(DaftarMonitoring daftarMonitoring, DataMonitoring dataMonitoring){
			mDaftarMonitoring 	= daftarMonitoring;
			mDataMonitoring 	= dataMonitoring;
		}
		public void onClick(View v) {
			mDaftarMonitoring.onEdit(BundleEntityMaker.makeBundleFromDataMonitoring(mDataMonitoring));
		}
		

		private final DataMonitoring mDataMonitoring;
		private final DaftarMonitoring mDaftarMonitoring;
		
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
	
	private final static class HandlerDeleteSendermonitoring extends Handler{

		public HandlerDeleteSendermonitoring(DaftarMonitoring daftarMonitoring, DataMonitoring dataMonitoring){
			this.daftarMonitoring 	= daftarMonitoring;
			this.dataMonitoring		= dataMonitoring;
		}
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
				case Status.SUCCESS:{
					daftarMonitoring.deleteSave(BundleEntityMaker.makeBundleFromDataMonitoring(dataMonitoring));
					break;
				}case Status.FAILED:{
					break;
				}
			}
		}
		private final DaftarMonitoring 	daftarMonitoring;
		private final DataMonitoring 	dataMonitoring;
	}
	

	private final static class WaitingKonfirmasiAktifHandler extends Handler{

		public WaitingKonfirmasiAktifHandler(DaftarMonitoring daftarMonitoring){
			this.daftarMonitoring = daftarMonitoring;
		}
		
		@Override
		public void handleMessage(Message msg) {
			if(msg.what==Status.SUCCESS){
				Bundle data = msg.getData();
				
				//cek apakah anak ini yang mau di update datanya...
				String idMonitoring = data.getString("idMonitoring");
				//.................................................
				LogMonakFileManager.debug("handler di invoke");					
				
				if(daftarMonitoring.dataMonitorings!=null && daftarMonitoring.dataMonitorings.size()>0){
					boolean all = false;
					if(idMonitoring.equals(daftarMonitoring.anak.getIdAnak())){
						all = true;
					}
					LogMonakFileManager.debug("dapat dataMonitoring dengan idMonitoring : "+idMonitoring);					
					for(DataMonitoring dataMonitoring:daftarMonitoring.dataMonitorings){
						LogMonakFileManager.debug("dapat for dengan id datamonitoring for : "+dataMonitoring.getIdMonitoring());

						if(all){
							dataMonitoring.setAktif(data.getBoolean("aktif"));
							continue;
						}
						if(dataMonitoring.getIdMonitoring().equals(idMonitoring)){
							dataMonitoring.setAktif(data.getBoolean("aktif"));
						}
					}						
					daftarMonitoring.daftarMonitoringAdapter.notifyDataSetChanged();
				}
			}
		}
		
		private final DaftarMonitoring daftarMonitoring;
		
	}

	

	

}
