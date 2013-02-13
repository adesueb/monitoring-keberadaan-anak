package org.ade.monak.ortu.boundary;

import java.util.ArrayList;
import java.util.List;

import org.ade.monak.ortu.R;
import org.ade.monak.ortu.Variable.Operation;
import org.ade.monak.ortu.Variable.Status;
import org.ade.monak.ortu.boundary.submenu.MultipleChoiceAnak;
import org.ade.monak.ortu.entity.Anak;
import org.ade.monak.ortu.entity.Lokasi;
import org.ade.monak.ortu.entity.Pelanggaran;
import org.ade.monak.ortu.map.view.Peta;
import org.ade.monak.ortu.service.BinderHandlerMonak;
import org.ade.monak.ortu.service.MonakService;
import org.ade.monak.ortu.service.gate.monak.SenderPendaftaranAnak;
import org.ade.monak.ortu.service.gate.monak.SenderTrackingMode;
import org.ade.monak.ortu.service.storage.DatabaseManagerOrtu;
import org.ade.monak.ortu.service.storage.LogMonakFileManager;
import org.ade.monak.ortu.service.util.IBindMonakServiceConnection;
import org.ade.monak.ortu.service.util.ServiceMonakConnection;
import org.ade.monak.ortu.util.BundleEntityMaker;
import org.ade.monak.ortu.util.HandlerAdd;
import org.ade.monak.ortu.util.HandlerEdit;
import org.ade.monak.ortu.util.IDGenerator;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class DaftarAnak extends ListActivity implements IFormOperation, IBindMonakServiceConnection{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_monak);
		setTitle("Daftar Anak");
		
		databaseManager 	= new DatabaseManagerOrtu(this);
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
				anaks.add(anak);
				daftarAnakAdapter.notifyDataSetChanged();
				
				databaseManager.addAnak(anak);
				sendRequestLocationAnak(anak,ADD);
				break;
			}case Operation.EDIT:{

				Anak anak = new Anak();
				anak.setIdAnak	(bundle.getString("idAnak"));
				anak.setIdOrtu	(idGenerator.getIdOrangTua());
				anak.setNamaAnak(bundle.getString("nama"));
				anak.setNoHpAnak(bundle.getString("noHp"));
		
				for(Anak anakFor:anaks){
					if(anak.getIdAnak().equals(anakFor.getIdAnak())){
						anakFor.setNamaAnak(anak.getNamaAnak());
						anakFor.setNoHpAnak(anak.getNoHpAnak());
						anakFor.setLastLokasi(null);
					}
				}
				daftarAnakAdapter.notifyDataSetChanged();
				databaseManager.deleteLokasiAnak(anak);
				databaseManager.updateAnak(anak);
				
				sendRequestLocationAnak(anak, UPDATE);
				
				break;
			}case Operation.DELETE:{
				Anak anak = new Anak();
				anak.setIdAnak	(bundle.getString("idAnak"));
				anak.setIdOrtu	(idGenerator.getIdOrangTua());
				anak.setNamaAnak(bundle.getString("nama"));
				anak.setNoHpAnak(bundle.getString("noHp"));
				anak = databaseManager.getAnakById(anak.getIdAnak(), false, false);
				if(anak.getLastLokasi()==null){
					for(Anak anakFor:anaks){
						if(anak.getIdAnak().equals(anakFor.getIdAnak())){
							anaks.remove(anakFor);
							break;
						}
					}
					daftarAnakAdapter.notifyDataSetChanged();
					databaseManager.deleteAnak(anak);
				}else{
					databaseManager.deleteLokasiAnak(anak);
					
				}
				sendRequestLocationAnak(anak, DELETE);	
				
				break;
			}
			
		}
		
	}
	
	private void startTrack(Anak anak){
		SenderTrackingMode senderTrackingMode = new SenderTrackingMode(this);
		senderTrackingMode.sendStartTracking(anak);
		Toast.makeText(this, "anak :"+anak.getNamaAnak()+" akan ditracking", Toast.LENGTH_SHORT).show();
	}
	
	private void stopTrack(Anak anak){
		SenderTrackingMode senderTrackingMode = new SenderTrackingMode(this);
		senderTrackingMode.sendStopTracking(anak);
		Toast.makeText(this, "anak :"+anak.getNamaAnak()+" akan stop tracking", Toast.LENGTH_SHORT).show();
	}
	

	private void sendRequestLocationAnak(Anak anak, int operation){	
		SenderPendaftaranAnak senderAnak = new SenderPendaftaranAnak(this, new SendingLocationHandler(this, anak, operation));
		switch(operation){
			case ADD :{
				senderAnak.sendPendaftarAnak(anak);
				break;
			}case UPDATE:{
				senderAnak.sendUpdateAnak(anak);
				break;
			}case DELETE:{
				senderAnak.sendHapusAnak(anak);
				break;
			}
		}
		
	}
	
	public void dialogToPetaTrack(final Anak anak) {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);                 
		alert.setTitle("Perhatian !!!");  
		alert.setMessage("Anda mau menampilkan track dalam bentuk tampilan peta?");                

		alert.setPositiveButton("ya", new DialogInterface.OnClickListener() {  
	      
			public void onClick(DialogInterface dialog, int whichButton) {  
				Intent intent = new Intent(DaftarAnak.this, Peta.class);
				Bundle bundle = BundleEntityMaker.makeBundleFromAnak(anak);
				bundle.putInt(Peta.EXTRA_ACTION, Peta.EXTRA_TRACK);
				intent.putExtras(bundle);
				DaftarAnak.this.startActivity(intent);		
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
	
	@Override
	protected void onStop() {
		super.onStop();
		if(bound){
			handlerBinder.unBindUIHandlerWaitingLocation();
			handlerBinder.unBindUIHandlerWaitingKonfirmasiHapusAnak();
			handlerBinder.unBindUIHandlerWaitingKonfirmasiTrack();
			unbindService(serviceConnection);
		}
		bound = false;
	}

	public void setBinderHandlerMonak(BinderHandlerMonak binderHandlerMonak) {
		handlerBinder = binderHandlerMonak;
		handlerBinder.bindUIHandlerWaitingLocation(new WaitingLocationHandler(this));	
		handlerBinder.bindUIHandlerWaitingKonfirmasiTrack(new WaitingKonfirmasiTrackHandler(this));
		handlerBinder.bindUIHandlerWaitingKonfirmasiHapusAnak(new WaitingKonfirmasiHapusHandler(this));
	}

	public void setBound(boolean bound) {
		this.bound = bound;
	}

	private ServiceMonakConnection serviceConnection;
	private IDGenerator			idGenerator;
	private DatabaseManagerOrtu 	databaseManager;
	private PendaftaranAnak 	pendaftaranAnak;
	private ArrayAdapter<Anak>	daftarAnakAdapter;
	private List<Anak> 			anaks;
	private List<Anak> 			anaksFull;
	private BinderHandlerMonak	handlerBinder;
	
	private boolean				bound;
	
	public final static String WAITING_LOCATION_STORAGE_HANDLER_ID = "waiting_location_storage_handler";
	
	private final static int ADD 	= 0;
	private final static int UPDATE = 1;
	private final static int DELETE = 2;
	
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
					
				LinearLayout llBackground = (LinearLayout) rowView.findViewById(R.id.background);
								
				Button btnStartTrack = (Button) rowView.findViewById(R.id.daftarAnakButtonStartTrack);
				btnStartTrack.setOnClickListener(new BtnStartTrackOnClick(daftarAnak, anak));
			
				
				Button btnStopTrack = (Button) rowView.findViewById(R.id.daftarAnakButtonStopTrack);				
				btnStopTrack.setOnClickListener(new BtnStopTrackOnClick(daftarAnak, anak));
				
				LinearLayout llStartTrack 	= (LinearLayout) rowView.findViewById(R.id.daftarAnakTrackStart);
				LinearLayout llStopTrack 	= (LinearLayout) rowView.findViewById(R.id.daftarAnakTrackStop);
				
				if(anak.isTrack()){
					llStartTrack.setVisibility(View.VISIBLE);					
					llStopTrack.setVisibility(View.GONE);
					
				}else{
					llStartTrack.setVisibility(View.GONE);
					llStopTrack.setVisibility(View.VISIBLE);
					
				}
				
				TextView txtAktif = (TextView) rowView.findViewById(R.id.daftarAnakTextTrackStart);
				TextView txtDeAktif = (TextView) rowView.findViewById(R.id.daftarAnakTextTrackStop);
				if(anak.getLastLokasi()==null){
					llBackground.setBackgroundResource(R.drawable.back_menu);
					btnStartTrack.setEnabled(false);
					btnStopTrack.setEnabled(false);
					txtAktif.setText(rowView.getResources().getString(R.string.registrasi_anak));
					txtDeAktif.setText(rowView.getResources().getString(R.string.registrasi_anak));
				}else{
					llBackground.setBackgroundResource(R.drawable.back_menu_green);			
					btnStartTrack.setEnabled(true);
					btnStopTrack.setEnabled(true);
					txtAktif.setText(rowView.getResources().getString(R.string.track_start));
					txtDeAktif.setText(rowView.getResources().getString(R.string.track_stop));
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
		public SendingLocationHandler(DaftarAnak daftarAnak, Anak anak, int operation){
			this.daftarAnak = daftarAnak;
			this.anak 		= anak;
			this.operation  = operation;
		}
		
		public void handleMessage(Message msg) {
			if(msg.what==Status.SUCCESS){

			}else if(msg.what==Status.FAILED){
				AlertDialog.Builder alert = new AlertDialog.Builder(daftarAnak);                 
				alert.setTitle("Perhatian !!!");  
				alert.setMessage("gagal mengirim verifikasi no HP anak... \ncoba lagi?");                

				alert.setPositiveButton("ya", new DialogInterface.OnClickListener() {  
			      
					public void onClick(DialogInterface dialog, int whichButton) {  
						daftarAnak.sendRequestLocationAnak(anak, operation);
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
		private final int operation;
	}
	
	private final static class WaitingLocationHandler extends Handler{

		public WaitingLocationHandler(DaftarAnak daftarAnak){
			this.daftarAnak = daftarAnak;
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

				for(Anak anakFor:daftarAnak.anaks){
					if(anakFor.getIdAnak().equals(idAnak)){
						anakFor.setLastLokasi(lokasi);
					}
				}		
				
				daftarAnak.daftarAnakAdapter.notifyDataSetChanged();
			}
		}
		
		private final DaftarAnak daftarAnak;
		
	}	
	
	private static final class BtnStartTrackOnClick implements View.OnClickListener{

		public BtnStartTrackOnClick(DaftarAnak daftarAnak, Anak anak){
			this.daftarAnak = daftarAnak;
			this.anak		= anak;
		}
		public void onClick(View v) {
			daftarAnak.startTrack(anak);
		}
		
		private final DaftarAnak 	daftarAnak;
		private final Anak 			anak;
		
	}
	
	private static final class BtnStopTrackOnClick implements View.OnClickListener{

		public BtnStopTrackOnClick(DaftarAnak daftarAnak, Anak anak){
			this.daftarAnak = daftarAnak;
			this.anak		= anak;
		}
		public void onClick(View v) {			
			daftarAnak.stopTrack(anak);
		}
		
		private final DaftarAnak 	daftarAnak;
		private final Anak 			anak;
		
	}
	
	
	
	private final static class WaitingKonfirmasiTrackHandler extends Handler{

		public WaitingKonfirmasiTrackHandler(DaftarAnak daftarAnak){
			this.daftarAnak = daftarAnak;
		}
		
		@Override
		public void handleMessage(Message msg) {
			if(msg.what==Status.SUCCESS){
				Bundle data = msg.getData();
				
				//cek apakah anak ini yang mau di update datanya...
				String idAnak = data.getString("idAnak");
				//.................................................
				
				for(Anak anakFor:daftarAnak.anaks){
					if(anakFor.getIdAnak().equals(idAnak)){
						anakFor.setTrack(data.getBoolean("track"));
						if(anakFor.isTrack()){
							daftarAnak.dialogToPetaTrack(anakFor);
						}
						break;
					}
				}						
				daftarAnak.daftarAnakAdapter.notifyDataSetChanged();
			}
		}
		
		private final DaftarAnak daftarAnak;
		
	}	
	
	private final static class WaitingKonfirmasiHapusHandler extends Handler{

		public WaitingKonfirmasiHapusHandler(DaftarAnak daftarAnak){
			this.daftarAnak = daftarAnak;
		}
		
		@Override
		public void handleMessage(Message msg) {
			if(msg.what==Status.SUCCESS){
				Bundle data = msg.getData();
				
				//cek apakah anak ini yang mau di update datanya...
				String idAnak = data.getString("idAnak");
				//.................................................
				for(Anak anakFor:daftarAnak.anaks){
					if(idAnak.equals(anakFor.getIdAnak())){
						daftarAnak.anaks.remove(anakFor);
						break;
					}
				}		
				
				daftarAnak.daftarAnakAdapter.notifyDataSetChanged();
			}
		}
		
		private final DaftarAnak daftarAnak;
		
	}	
	
}