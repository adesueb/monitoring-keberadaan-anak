package org.ade.monitoring.keberadaan.boundary;

import org.ade.monitoring.keberadaan.R;
import org.ade.monitoring.keberadaan.map.Peta;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class DaftarLaporan extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.daftar_laporan);
		
		setToolbarMenu();
	}
	
	private void setToolbarMenu(){
  		ImageView ivSearch 	= (ImageView) findViewById(R.id.laporanSearch);		
		ivSearch.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				showDialog(DIALOG_SEARCH);
			}
		});
		
		ImageView ivMap 	= (ImageView) findViewById(R.id.laporanMap);		
		ivMap.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				Intent intent = new Intent(DaftarLaporan.this, Peta.class); 
				startActivity(intent);
				finish();
			}
		});
		
		ImageView ivAdvance 	= (ImageView) findViewById(R.id.laporanAdvance);		
		ivAdvance.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				
			}
		});
		
		
		
  	}


	private final static int DIALOG_SEARCH		= 0;
}
