package org.ade.monitoring.keberadaan.boundary;

import java.util.ArrayList;
import java.util.List;

import org.ade.monitoring.keberadaan.R;
import org.ade.monitoring.keberadaan.Variable.Operation;
import org.ade.monitoring.keberadaan.boundary.submenu.MultipleChoiceAnak;
import org.ade.monitoring.keberadaan.entity.Anak;
import org.ade.monitoring.keberadaan.entity.Pelanggaran;
import org.ade.monitoring.keberadaan.map.Peta;
import org.ade.monitoring.keberadaan.service.storage.DatabaseManager;
import org.ade.monitoring.keberadaan.util.BundleMaker;
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

public class DaftarAnak extends ListActivity implements IFormOperation{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_monak);
		setTitle("Daftar Anak");
		
		databaseManager 	= new DatabaseManager(this);
		idGenerator 		= new IDGenerator(this, databaseManager);
		
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

	public void onEdit(Bundle bundle) {
		
		pendaftaranAnak.setId(bundle.getString("id"));
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
				anak.setIdAnak	(bundle.getString("id"));
				anak.setIdOrtu	(idGenerator.getIdOrangTua());
				anak.setNamaAnak(bundle.getString("nama"));
				anak.setNoHpAnak(bundle.getString("noHp"));
				// TODO : send location to Anak before save it...
				databaseManager.addAnak(anak);
				anaks.add(anak);
				daftarAnakAdapter.notifyDataSetChanged();
				
				break;
			}case Operation.EDIT:{
				Anak anak = new Anak();
				anak.setIdAnak	(bundle.getString("id"));
				anak.setIdOrtu	(idGenerator.getIdOrangTua());
				anak.setNamaAnak(bundle.getString("nama"));
				anak.setNoHpAnak(bundle.getString("noHp"));
				// TODO : send location to Anak before save it...
				databaseManager.updateAnak(anak);
				for(Anak anakFor:anaks){
					if(anak.getIdAnak().equals(anakFor.getIdAnak())){
						anakFor.setNamaAnak(bundle.getString("nama"));
						anakFor.setNoHpAnak(bundle.getString("noHp"));
					}
				}
				daftarAnakAdapter.notifyDataSetChanged();
				
				break;
			}case Operation.DELETE:{
				Anak anak = new Anak();
				anak.setIdAnak	(bundle.getString("id"));
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

	private IDGenerator			idGenerator;
	private DatabaseManager 	databaseManager;
	private PendaftaranAnak 	pendaftaranAnak;
	private ArrayAdapter<Anak>	daftarAnakAdapter;
	private List<Anak> 			anaks;
	private List<Anak> 			anaksFull;
	
	
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
				
				rowView.setOnLongClickListener(new DaftarAnakLongClick(daftarAnak, anak));
				
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
	
	private final static class DaftarAnakLongClick implements View.OnLongClickListener{

		public DaftarAnakLongClick(DaftarAnak daftarAnak, Anak anak){
			mDaftarAnak = daftarAnak;
			mAnak 		= anak;
		}
		public boolean onLongClick(View arg0) {
			(new MultipleChoiceAnak(mDaftarAnak, BundleMaker.makeBundleFromAnak(mAnak), true)).show();
			return false;
		}
		private final Anak mAnak;
		private final DaftarAnak mDaftarAnak;
	}
	
}