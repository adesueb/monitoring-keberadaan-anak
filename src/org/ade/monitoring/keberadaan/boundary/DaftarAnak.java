package org.ade.monitoring.keberadaan.boundary;

import java.util.List;

import org.ade.monitoring.keberadaan.R;
import org.ade.monitoring.keberadaan.entity.Anak;
import org.ade.monitoring.keberadaan.storage.DatabaseManager;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class DaftarAnak extends ListActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		databaseManager = new DatabaseManager(this);
		getListView().setAdapter
			(new AdapterDaftarAnak
					(this, R.layout.daftar_anak_item, databaseManager.getAllAnak(true,false)));
	}
	
	private class AdapterDaftarAnak extends ArrayAdapter<Anak>{

		public AdapterDaftarAnak(Context context,
				int textViewResourceId, List<Anak> objects) {
			super(context, textViewResourceId, objects);
			this.context 	= context;
			this.resource 	= textViewResourceId;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			Anak anak = getItem(position);
			
			if(anak == null) return null;
			
			LayoutInflater inflater = (LayoutInflater) context
			        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			View rowView = inflater.inflate(resource, parent, false);
			
			TextView name 			= (TextView) rowView.findViewById(R.id.daftarAnakItemName);
			TextView phone 			= (TextView) rowView.findViewById(R.id.daftarAnakItemPhone);
			TextView pelanggaran 	= (TextView) rowView.findViewById(R.id.daftarAnakItemPelanggaran);
			
			name.setText(anak.getNamaAnak());
			phone.setText(anak.getNoHpAnak());
			pelanggaran.setText(anak.getPelanggarans().size()+"");
			
			return rowView;
		}
		
		private final Context context;
		private final int resource;
		
	}

	private DatabaseManager databaseManager;
}
