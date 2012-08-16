package org.ade.monitoring.keberadaan.boundary;

import org.ade.monitoring.keberadaan.R;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class PendaftaranAnak extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pendaftaran_anak);
		ImageView ivBack = (ImageView) findViewById(R.id.pendaftaranBack);
		
		final EditText txtName 	= (EditText) findViewById(R.id.pendaftaranTxtName);
		final EditText txtPhone 	= (EditText) findViewById(R.id.pendaftaranTxtPhone);
		
		
		ivBack.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				finish();
			}
		});
		
		Button btnOk 		= (Button) findViewById(R.id.pendaftaranBtnOk);
		btnOk.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				String name 	= txtName.getText().toString();
				String phone 	= txtPhone.getText().toString();
				//TODO : action save to anak and generate id anak
			}
		});
		
		Button btnCancel 	= (Button) findViewById(R.id.pendaftaranBtnCancel);		
		btnCancel.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				txtName.setText("");
				txtPhone.setText("");
			}
		});
	}
	
	
}
