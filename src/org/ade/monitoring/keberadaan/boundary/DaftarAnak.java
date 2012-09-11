package org.ade.monitoring.keberadaan.boundary;

import java.util.List;

import org.ade.monitoring.keberadaan.R;
import org.ade.monitoring.keberadaan.Variable.Operation;
import org.ade.monitoring.keberadaan.Variable.Status;
import org.ade.monitoring.keberadaan.entity.Anak;
import org.ade.monitoring.keberadaan.storage.DatabaseManager;
import org.ade.monitoring.keberadaan.util.IDGenerator;
import org.ade.monitoring.keberadaan.util.IFormOperation;

import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DaftarAnak extends ListActivity implements IFormOperation{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_monak);
		TextView tvTitle = (TextView) findViewById(R.id.listMonakTitle);
		tvTitle.setText("Daftar Anak");
		databaseManager = new DatabaseManager(this);
		idGenerator 	= new IDGenerator(this, databaseManager);
		getListView().setAdapter
			(new AdapterDaftarAnak
					(this, R.layout.daftar_anak_item, databaseManager.getAllAnak(true,false)));
		ImageView iv = (ImageView) findViewById(R.id.listMonakIvAdd);
		iv.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				showDialog(Operation.ADD);
			}
		});
		setPendaftaranAnak();
	}
	
	private void setPendaftaranAnak(){
		pendaftaranAnak = new PendaftaranAnak(this, null);
	}

	@Override
	protected Dialog onCreateDialog(int id, Bundle bundle) {
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
				return null;
			}case Operation.MULTIPLE_CHOICE:{
				return null;
			}
		}
		return super.onCreateDialog(id);
	}
	
	public void add() {
		Anak anak = new Anak();
		anak.setIdAnak	(pendaftaranAnak.getId());
		anak.setIdOrtu	(idGenerator.getIdOrangTua());
		anak.setNamaAnak(pendaftaranAnak.getName());
		anak.setNoHpAnak(pendaftaranAnak.getPhone());
		databaseManager.addAnak(anak);
	}

	public void edit() {
		Anak anak = new Anak();
		anak.setIdAnak	(pendaftaranAnak.getId());
		anak.setIdOrtu	(idGenerator.getIdOrangTua());
		anak.setNamaAnak(pendaftaranAnak.getName());
		anak.setNoHpAnak(pendaftaranAnak.getPhone());
	}

	public void delete() {
		
	}

	private IDGenerator		idGenerator;
	private DatabaseManager databaseManager;
	private PendaftaranAnak pendaftaranAnak;
	
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
				pelanggaran.setText(anak.getPelanggarans().size()+"");
				
				rowView.setOnLongClickListener(new DaftarAnakLongClick(daftarAnak, anak));
				
				return rowView;
			}else{
				return null;
			}
		}
		
		private final DaftarAnak daftarAnak;
		private final int resource;
		private List<Anak> anaks;
		
	}
	
	private final static class HandlerAdd extends Handler{
		public HandlerAdd(IFormOperation daftarAnak){
			mDaftarAnak = daftarAnak;
		}
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
				case Status.SUCCESS:{
					mDaftarAnak.add();
				}
			}
		}
		private IFormOperation mDaftarAnak;
	}
	
	private final static class HandlerEdit extends Handler{
		public HandlerEdit(IFormOperation daftarAnak){
			mDaftarAnak = daftarAnak;
		}
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
				case Status.SUCCESS:{
					mDaftarAnak.edit();
				}
			}
		}
		private IFormOperation mDaftarAnak;
	}
	
	private final static class DaftarAnakLongClick implements View.OnLongClickListener{

		public DaftarAnakLongClick(DaftarAnak daftarAnak, Anak anak){
			mDaftarAnak = daftarAnak;
			mAnak 		= anak;
		}
		public boolean onLongClick(View arg0) {
			Bundle bundle = new Bundle();
			bundle.putString("id", mAnak.getIdAnak());
			bundle.putString("nama", mAnak.getNamaAnak());
			bundle.putString("orangTua", mAnak.getIdOrtu());
			bundle.putString("noHp", mAnak.getNoHpAnak());
			mDaftarAnak.showDialog(Operation.MULTIPLE_CHOICE);
			return false;
		}
		private final Anak mAnak;
		private final DaftarAnak mDaftarAnak;
	}
	
}
