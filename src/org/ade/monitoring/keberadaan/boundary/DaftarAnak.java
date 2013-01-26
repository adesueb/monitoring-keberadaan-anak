package org.ade.monitoring.keberadaan.boundary;

import java.util.ArrayList;
import java.util.List;

import org.ade.monitoring.keberadaan.R;
import org.ade.monitoring.keberadaan.Variable.Operation;
import org.ade.monitoring.keberadaan.Variable.Status;
import org.ade.monitoring.keberadaan.boundary.submenu.MultipleChoiceAnak;
import org.ade.monitoring.keberadaan.entity.Anak;
import org.ade.monitoring.keberadaan.entity.Lokasi;
import org.ade.monitoring.keberadaan.entity.Pelanggaran;
import org.ade.monitoring.keberadaan.map.view.Peta;
import org.ade.monitoring.keberadaan.service.MonakService;
import org.ade.monitoring.keberadaan.service.BinderHandlerMonak;
import org.ade.monitoring.keberadaan.service.gate.monak.SenderPendaftaranAnak;
import org.ade.monitoring.keberadaan.service.storage.DatabaseManager;
import org.ade.monitoring.keberadaan.service.util.IBindMonakServiceConnection;
import org.ade.monitoring.keberadaan.service.util.ServiceMonakConnection;
import org.ade.monitoring.keberadaan.util.BundleEntityMaker;
import org.ade.monitoring.keberadaan.util.HandlerAdd;
import org.ade.monitoring.keberadaan.util.HandlerEdit;
import org.ade.monitoring.keberadaan.util.IDGenerator;
import org.ade.monitoring.keberadaan.util.IFormOperation;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DaftarAnak extends ListActivity implements IFormOperation, IBindMonakServiceConnection{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_monak);
		setTitle("Daftar Anak");
		
		databaseManager 	= new DatabaseManager(this);
		idGenerator 		= new IDGenerator(this, databaseManager);
//		databaseManager.deleteAllLokasi();
		anaks				= databaseManager.getAllAnak(true,false);
		if(anaks==null){
			anaks = new ArrayList<Anak>();
		}
		
		anaksFull = new ArrayList<Anak>();
		anaksFull.addAll(anaks);

		daftarAnakAdapter 	= new AdapterDaftarAnak
				(this, R.layout.daftar_anak_item, anaks);
		getListView().setAdapter
			(daftarAnakAdapter);
		
		ImageView ivAdd = (ImageView) findViewById(R.id.listMonakIvAdd);
		ivAdd.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {

				onAdd(null);
			}
		});
		
		ImageView ivMap = (ImageView) findViewById(R.id.listMonakMap);
		ivMap.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(DaftarAnak.this, Peta.class);
				startActivity(intent);
				finish();
			}
		});
		
		final EditText txtSearch = (EditText) findViewById(R.id.listMonakSearch);
		txtSearch.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				anaks.clear();
				String namaAnak = txtSearch.getText().toString().toUpperCase();
				for(Anak anak:anaksFull){
					if(anak.getNamaAnak().toUpperCase().contains(namaAnak)){
						anaks.add(anak);
					}
				}
				Log.d("searching", "masuk searching");
				daftarAnakAdapter.notifyDataSetChanged();
			}
			
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {}
			
			public void afterTextChanged(Editable arg0) {}
		});
		
		setPendaftaranAnak();
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


	private void setPendaftaranAnak(){
		pendaftaranAnak = new PendaftaranAnak(this, null);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		return pendaftaranAnak;
	}
	
	public void onAdd(Bundle bundle) {
		
		pendaftaranAnak.setId(idGenerator.getIdAnak());
		pendaftaranAnak.setHandler(new HandlerAdd(DaftarAnak.this));

		showDialog(Operation.ADD);
	}
	
	public void onDetail(Bundle bundle){
		Intent intent = new Intent(this, DaftarMonitoring.class);
		intent.putExtras(bundle);
		startActivity(intent);
		
	}

	public void onEdit(Bundle bundle) {

		pendaftaranAnak.setId(bundle.getString("idAnak"));
		pendaftaranAnak.setName(bundle.getString("nama"));
		pendaftaranAnak.setPhone(bundle.getString("noHp"));
		pendaftaranAnak.setHandler(new HandlerEdit(this));			

		showDialog(Operation.EDIT);
	}

	public void onDelete(final Bundle bundle) {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);                 
		alert.setTitle("Perhatian !!!");  
		alert.setMessage("Anda mau menghapus anak : "+bundle.getString("nama")+"?");                

		alert.setPositiveButton("ya", new DialogInterface.OnClickListener() {  
	      
			public void onClick(DialogInterface dialog, int whichButton) {  
				onSave(bundle, Operation.DELETE);
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
		alertDialog.show();
	}
	
	public void onSave(Bundle bundle, int status){
		switch (status) {
			case Operation.ADD:{
				Anak anak = new Anak();
				anak.setIdAnak	(bundle.getString("idAnak"));
				anak.setIdOrtu	(idGenerator.getIdOrangTua());
				anak.setNamaAnak(bundle.getString("nama"));
				anak.setNoHpAnak(bundle.getString("noHp"));
				Log.d("DaftarAnak", "add anak");
				databaseManager.addAnak(anak);
				sendRequestLocationAnak(anak,false);
				break;
			}case Operation.EDIT:{

				Anak anak = new Anak();
				anak.setIdAnak	(bundle.getString("idAnak"));
				anak.setIdOrtu	(idGenerator.getIdOrangTua());
				anak.setNamaAnak(bundle.getString("nama"));
				anak.setNoHpAnak(bundle.getString("noHp"));
				databaseManager.deleteLokasiAnak(anak);
				databaseManager.updateAnak(anak);
				
				sendRequestLocationAnak(anak, true);
				
				break;
			}case Operation.DELETE:{
				Anak anak = new Anak();
				anak.setIdAnak	(bundle.getString("idAnak"));
				anak.setIdOrtu	(idGenerator.getIdOrangTua());
				anak.setNamaAnak(bundle.getString("nama"));
				anak.setNoHpAnak(bundle.getString("noHp"));
				databaseManager.deleteAnak(anak);
				for(Anak anakFor:anaks){
					if(anak.getIdAnak().equals(anakFor.getIdAnak())){
						anaks.remove(anakFor);
						break;
					}
				}
				daftarAnakAdapter.notifyDataSetChanged();
				break;
			}
			
		}
		
	}
	
	private void sendRequestLocationAnak(Anak anak, boolean isEdit){	
		SenderPendaftaranAnak senderAnak = new SenderPendaftaranAnak(this, new SendingLocationHandler(this, anak, isEdit));
		senderAnak.sendAnak(anak);				
		handlerBinder.bindUIHandlerWaitingLocation(new WaitingLocationHandler(this, anak, isEdit));	
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		Log.d("daftar anak", "status dari bound adalah : "+bound);
		
		if(bound){
			handlerBinder.unBindUIHandlerWaitingLocation();
			unbindService(serviceConnection);
		}
		bound = false;
	}

	public void setBinderHandlerMonak(BinderHandlerMonak binderHandlerMonak) {
		handlerBinder =binderHandlerMonak;
		for(Anak anak :anaks ){
			handlerBinder.bindUIHandlerWaitingLocation(new WaitingLocationHandler(this, anak, true));	
		}
	}

	public void setBound(boolean bound) {
		this.bound = bound;
	}

	private ServiceMonakConnection serviceConnection;
	private IDGenerator			idGenerator;
	private DatabaseManager 	databaseManager;
	private PendaftaranAnak 	pendaftaranAnak;
	private ArrayAdapter<Anak>	daftarAnakAdapter;
	private List<Anak> 			anaks;
	private List<Anak> 			anaksFull;
	private BinderHandlerMonak	handlerBinder;
	
	private boolean				bound;
	
	public final static String WAITING_LOCATION_STORAGE_HANDLER_ID = "waiting_location_storage_handler";
	
	private final static class AdapterDaftarAnak extends ArrayAdapter<Anak>{

		public AdapterDaftarAnak(DaftarAnak daftarAnak,
				int textViewResourceId, List<Anak> objects) {
			super(daftarAnak, textViewResourceId, objects);
			this.daftarAnak	= daftarAnak;
			this.resource 	= textViewResourceId;
			this.anaks		= objects;
		}
		

		@Override
		public int getCount() {
			return anaks!=null?anaks.size():0;
		}


		@Override
		public Anak getItem(int position) {
			return super.getItem(position);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			
			if(anaks!=null&&anaks.size()>0){
				
				Anak anak = anaks.get(position);
				
				if(anak == null) return null;
				
				LayoutInflater inflater = (LayoutInflater) daftarAnak
						
				        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				
				View rowView = inflater.inflate(resource, parent, false);
				
				TextView name 			= (TextView) rowView.findViewById(R.id.daftarAnakItemName);
				TextView phone 			= (TextView) rowView.findViewById(R.id.daftarAnakItemPhone);
				TextView pelanggaran 	= (TextView) rowView.findViewById(R.id.daftarAnakItemPelanggaran);
				
				name.setText(anak.getNamaAnak());
				phone.setText(anak.getNoHpAnak());
				
				List<Pelanggaran> pelanggarans = anak.getPelanggarans();
				if(pelanggarans!=null){
					pelanggaran.setText(pelanggarans.size()+"");				
				}else{
					pelanggaran.setText("0");
				}
				
				rowView.setOnClickListener(new DaftarAnakClick(daftarAnak, anak));
				
				rowView.setOnLongClickListener(new DaftarAnakLongClick(daftarAnak, anak));
				
				LinearLayout llResponse = (LinearLayout) rowView.findViewById(R.id.daftarAnakItemResponse);
				if(anak.getLastLokasi()==null){
					llResponse.setVisibility(View.VISIBLE);
				}else{
					llResponse.setVisibility(View.GONE);
				}
				
				LinearLayout llBackground = (LinearLayout) rowView.findViewById(R.id.background);
				if(position%2==0){

					llBackground.setBackgroundResource(R.drawable.back_menu_green);
				}else{

					llBackground.setBackgroundResource(R.drawable.back_menu);
				}
				
				return rowView;
			}else{
				return null;
			}
		}
		
		private final DaftarAnak daftarAnak;
		private final int resource;
		private List<Anak> anaks;
		
	}
	
	private final static class DaftarAnakClick implements View.OnClickListener{

		public DaftarAnakClick(DaftarAnak daftarAnak, Anak anak){
			mDaftarAnak = daftarAnak;
			mAnak 		= anak;
		}
		public void onClick(View v) {
			mDaftarAnak.onDetail(BundleEntityMaker.makeBundleFromAnak(mAnak));
		}
		

		private final Anak mAnak;
		private final DaftarAnak mDaftarAnak;
		
	}
	
	private final static class DaftarAnakLongClick implements View.OnLongClickListener{

		public DaftarAnakLongClick(DaftarAnak daftarAnak, Anak anak){
			mDaftarAnak = daftarAnak;
			mAnak 		= anak;
		}
		public boolean onLongClick(View arg0) {
			(new MultipleChoiceAnak(mDaftarAnak, BundleEntityMaker.makeBundleFromAnak(mAnak))).show();
			
			return false;
		}
		private final Anak mAnak;
		private final DaftarAnak mDaftarAnak;
	}
	
	private final static class SendingLocationHandler extends Handler{
		public SendingLocationHandler(DaftarAnak daftarAnak, Anak anak, boolean isEdit){
			this.daftarAnak = daftarAnak;
			this.anak 		= anak;
			this.isEdit		= isEdit;
		}
		
		public void handleMessage(Message msg) {
			if(msg.what==Status.SUCCESS){

				if(anak.getIdAnak()!=null && !anak.getIdAnak().equals("")){
					if(isEdit){
						for(Anak anakFor:daftarAnak.anaks){
							if(anak.getIdAnak().equals(anakFor.getIdAnak())){
								anakFor.setNamaAnak(anak.getNamaAnak());
								anakFor.setNoHpAnak(anak.getNoHpAnak());
								anakFor.setLastLokasi(null);
							}
						}						
					}else{
						daftarAnak.anaks.add(anak);
					}

				}
				
				daftarAnak.daftarAnakAdapter.notifyDataSetChanged();
				
			}else if(msg.what==Status.FAILED){
				AlertDialog.Builder alert = new AlertDialog.Builder(daftarAnak);                 
				alert.setTitle("Perhatian !!!");  
				alert.setMessage("gagal mengirim verifikasi no HP anak... \ncoba lagi?");                

				alert.setPositiveButton("ya", new DialogInterface.OnClickListener() {  
			      
					public void onClick(DialogInterface dialog, int whichButton) {  
						daftarAnak.sendRequestLocationAnak(anak, isEdit);
						dialog.dismiss();
						return;                  
			        }  
			     });  

				alert.setNegativeButton("tidak", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {

						daftarAnak.handlerBinder.unBindUIHandlerWaitingLocation();
						dialog.dismiss();
						return;   
					}
				});
				AlertDialog alertDialog = alert.create();
				alertDialog.show();
			}
		}

		private final DaftarAnak daftarAnak;
		private final Anak anak;
		private final boolean isEdit;
	}
	
	private final static class WaitingLocationHandler extends Handler{

		public WaitingLocationHandler(DaftarAnak daftarAnak, Anak anak, boolean isEdit){
			this.daftarAnak = daftarAnak;
			this.anak		= anak;
			this.isEdit		= isEdit;
		}
		
		@Override
		public void handleMessage(Message msg) {
			if(msg.what==Status.SUCCESS){
				Bundle data = msg.getData();

				Lokasi lokasi = new Lokasi();
				lokasi.setLatitude(data.getDouble("latitude"));
				lokasi.setLongitude(data.getDouble("longitude"));
				//cek apakah anak ini yang mau di update datanya...
				String idAnak = data.getString("idAnak");
				//.................................................
				if(anak.getIdAnak()!=null && anak.getIdAnak().equals(idAnak)){
					anak.setLastLokasi(lokasi);
					if(isEdit){
						for(Anak anakFor:daftarAnak.anaks){
							if(anak.getIdAnak().equals(anakFor.getIdAnak())){
								anakFor.setNamaAnak(anak.getNamaAnak());
								anakFor.setNoHpAnak(anak.getNoHpAnak());
								anakFor.setLastLokasi(lokasi);
							}
						}						
					}else{
						daftarAnak.anaks.add(anak);
					}

				}
				
				
				daftarAnak.daftarAnakAdapter.notifyDataSetChanged();
			}
		}
		
		private final DaftarAnak daftarAnak;
		private final Anak anak;
		private final boolean isEdit;
		
	}	
	
}