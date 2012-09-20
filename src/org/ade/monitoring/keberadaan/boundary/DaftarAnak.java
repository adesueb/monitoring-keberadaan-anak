package org.ade.monitoring.keberadaan.boundary;

import java.util.ArrayList;
import java.util.List;

import org.ade.monitoring.keberadaan.R;
import org.ade.monitoring.keberadaan.Variable.Operation;
import org.ade.monitoring.keberadaan.boundary.submenu.MultipleChoiceAnak;
import org.ade.monitoring.keberadaan.entity.Anak;
import org.ade.monitoring.keberadaan.entity.Pelanggaran;
import org.ade.monitoring.keberadaan.storage.DatabaseManager;
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
import android.os.Bundle;
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
		TextView tvTitle = (TextView) findViewById(R.id.listMonakTitle);
		tvTitle.setText("Daftar Anak");
		databaseManager 	= new DatabaseManager(this);
		idGenerator 		= new IDGenerator(this, databaseManager);
		anaks				= databaseManager.getAllAnak(true,false);
		if(anaks==null){
			anaks = new ArrayList<Anak>();
		}
		daftarAnakAdapter 	= new AdapterDaftarAnak
				(this, R.layout.daftar_anak_item, anaks);
		getListView().setAdapter
			(daftarAnakAdapter);
		ImageView ivAdd = (ImageView) findViewById(R.id.listMonakIvAdd);
		ivAdd.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				showDialog(Operation.ADD);
			}
		});
		
		ImageView ivMap = (ImageView) findViewById(R.id.listMonakMap);
		ivMap.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				//TODO : open map... 
			}
		});
		
		setPendaftaranAnak();
	}
	
	private void setPendaftaranAnak(){
		pendaftaranAnak = new PendaftaranAnak(this, null);
	}

	@Override
	protected Dialog onCreateDialog(int id, final Bundle bundle) {
		switch (id){
			case Operation.ADD:{
				pendaftaranAnak.setId(idGenerator.getIdAnak());
				pendaftaranAnak.setHandler(new HandlerAdd(this));
				return pendaftaranAnak;
			}case Operation.EDIT:{
				pendaftaranAnak.setId(bundle.getString("id"));
				pendaftaranAnak.setName(bundle.getString("nama"));
				pendaftaranAnak.setPhone(bundle.getString("noHp"));
				pendaftaranAnak.setHandler(new HandlerEdit(this));				
				return pendaftaranAnak;
			}case Operation.DELETE:{
				AlertDialog.Builder alert = new AlertDialog.Builder(this);                 
				alert.setTitle("Perhatian !!!");  
				alert.setMessage("Anda mau menghapus anak : "+bundle.getString("nama")+"?");                

				final EditText input = new EditText(this); 
				input.setSingleLine(false);
			    input.setLines(3);
				alert.setView(input);

				alert.setPositiveButton("ya", new DialogInterface.OnClickListener() {  
			      
					public void onClick(DialogInterface dialog, int whichButton) {  
						delete(bundle);
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
			}case Operation.MULTIPLE_CHOICE:{
				return new MultipleChoiceAnak(this, bundle, true);
			}
		}
		return super.onCreateDialog(id);
	}
	
	public void add(Bundle bundle) {
		Anak anak = new Anak();
		anak.setIdAnak	(bundle.getString("id"));
		anak.setIdOrtu	(idGenerator.getIdOrangTua());
		anak.setNamaAnak(bundle.getString("nama"));
		anak.setNoHpAnak(bundle.getString("noHp"));
		databaseManager.addAnak(anak);
		anaks.add(anak);
		daftarAnakAdapter.notifyDataSetChanged();
		
	}

	public void edit(Bundle bundle) {
		Anak anak = new Anak();
		anak.setIdAnak	(bundle.getString("id"));
		anak.setIdOrtu	(idGenerator.getIdOrangTua());
		anak.setNamaAnak(bundle.getString("nama"));
		anak.setNoHpAnak(bundle.getString("noHp"));
		databaseManager.updateAnak(anak);
		for(Anak anakFor:anaks){
			if(anak.getIdAnak().equals(anakFor.getIdAnak())){
				anakFor.setNamaAnak(bundle.getString("nama"));
				anakFor.setNoHpAnak(bundle.getString("noHp"));
			}
		}
		daftarAnakAdapter.notifyDataSetChanged();
	}

	public void delete(Bundle bundle) {
		Anak anak = new Anak();
		anak.setIdAnak	(bundle.getString("id"));
		anak.setIdOrtu	(idGenerator.getIdOrangTua());
		anak.setNamaAnak(bundle.getString("nama"));
		anak.setNoHpAnak(bundle.getString("noHp"));
		databaseManager.deleteAnak(anak);
		for(Anak anakFor:anaks){
			if(anak.getIdAnak().equals(anakFor.getIdAnak())){
				anaks.remove(anakFor);
			}
		}
		daftarAnakAdapter.notifyDataSetChanged();
	}

	private IDGenerator			idGenerator;
	private DatabaseManager 	databaseManager;
	private PendaftaranAnak 	pendaftaranAnak;
	private ArrayAdapter<Anak>	daftarAnakAdapter;
	private List<Anak> 			anaks;
	
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
			
			mDaftarAnak.showDialog(Operation.MULTIPLE_CHOICE, BundleMaker.makeBundleFromAnak(mAnak));
			return false;
		}
		private final Anak mAnak;
		private final DaftarAnak mDaftarAnak;
	}
	
}
