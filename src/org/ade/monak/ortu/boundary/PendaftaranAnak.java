package org.ade.monak.ortu.boundary;

import org.ade.monak.ortu.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class PendaftaranAnak extends Dialog{

	
	public PendaftaranAnak(Context context, Handler handler) {
		super(context);
		createDialog();
		mHandler = handler;
	}

	private void createDialog() {
		setContentView(R.layout.pendaftaran_anak);
		txtName 	= (EditText) findViewById(R.id.pendaftaranTxtName);
		txtPhone 	= (EditText) findViewById(R.id.pendaftaranTxtPhone);

		txtId 		= (EditText) findViewById(R.id.pendaftaranTxtId);
		

		Button btnOk 		= (Button) findViewById(R.id.pendaftaranBtnOk);
		btnOk.setOnClickListener(new PendaftaranAnakClickListener(this));
		
		Button btnCancel 	= (Button) findViewById(R.id.pendaftaranBtnCancel);		
		btnCancel.setOnClickListener(new PendaftaranAnakClickListener(this));
	}
	
	
	
	public String getId() {
		return txtId.getText().toString();
	}

	public void setId(String txtId) {
		this.txtId.setText( txtId);
	}

	public void setName(String name){
		txtName.setText(name);
	}
	
	public void setPhone(String phone){
		txtPhone.setText(phone);
	}
	
	public String getName(){
		return txtName.getText().toString();
	}
	
	public String getPhone(){
		return txtPhone.getText().toString();
	}
	
	public Handler getHandler() {
		return mHandler;
	}

	public void setHandler(Handler mHandler) {
		this.mHandler = mHandler;
	}
	
	public void refresh(){
		setId("");
		setName("");
		setPhone("");
	}
	private EditText 	txtName;
	private EditText 	txtPhone;
	private EditText	txtId;
	private Handler		mHandler;
	
	private final static class PendaftaranAnakClickListener implements View.OnClickListener{

		public PendaftaranAnakClickListener(PendaftaranAnak pendaftaranAnak){
			mPendaftaranAnak = pendaftaranAnak;
		}
		public void onClick(View v) {
			if(v.getId()==R.id.pendaftaranBtnOk){
				Handler handler = mPendaftaranAnak.getHandler();
				if(handler!=null){
					Bundle bundle = new Bundle();
					bundle.putString("idAnak", 		mPendaftaranAnak.getId());
					bundle.putString("nama", 	mPendaftaranAnak.getName());
					bundle.putString("noHp", 	mPendaftaranAnak.getPhone());
					mPendaftaranAnak.refresh();
					Message message = new Message();
					message.setData(bundle);
					message.setTarget(handler);
					handler.sendMessage(message);
				}
				
			}else{
				mPendaftaranAnak.refresh();
			}
			mPendaftaranAnak.dismiss();
		}
		
		private PendaftaranAnak mPendaftaranAnak;
		
	}
}
