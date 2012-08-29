package org.ade.monitoring.keberadaan.boundary;

import org.ade.monitoring.keberadaan.R;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class PendaftaranAnak extends Dialog{

	
	public PendaftaranAnak(Context context, int theme) {
		super(context, theme);
		createDialog();
	}

	private void createDialog() {
		setContentView(R.layout.pendaftaran_anak);
//		ImageView ivBack = (ImageView) findViewById(R.id.pendaftaranBack);
//		
		final EditText txtName 	= (EditText) findViewById(R.id.pendaftaranTxtName);
		final EditText txtPhone 	= (EditText) findViewById(R.id.pendaftaranTxtPhone);
//		
//		
//		ivBack.setOnClickListener(new OnClickListener() {
//			
//			public void onClick(View v) {
//				finish();
//			}
//		});
		
		Button btnOk 		= (Button) findViewById(R.id.pendaftaranBtnOk);
		btnOk.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				String name 	= txtName.getText().toString();
				String phone 	= txtPhone.getText().toString();
				//TODO : action save to anak and generate id anak
			}
		});
		
		Button btnCancel 	= (Button) findViewById(R.id.pendaftaranBtnCancel);		
		btnCancel.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				txtName.setText("");
				txtPhone.setText("");
			}
		});
	}
	
	
}
