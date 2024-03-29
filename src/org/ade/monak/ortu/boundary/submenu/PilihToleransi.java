package org.ade.monak.ortu.boundary.submenu;

import org.ade.monak.ortu.R;
import org.ade.monak.ortu.boundary.PendaftaranMonitoring;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class PilihToleransi extends Dialog{

	public PilihToleransi(Context context, final Handler handler) {
		super(context);
		mHandler = handler;
		setContentView(R.layout.monitoring_toleransi);
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTitle("Toleransi : ");
		Button buttonOk = (Button) findViewById(R.id.monitoringToleransiOk);
		buttonOk.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				EditText textNumber = (EditText) findViewById(R.id.monitoringToleransiTextNumber);
				toleransi = Integer.parseInt(textNumber.getText().toString());
				mHandler.sendEmptyMessage(PendaftaranMonitoring.TOLERANSI);
				dismiss();
			}
		});
	}


	public void setToleransi(int toleransi){
		this.toleransi = toleransi;
		if(toleransi!=0){
			EditText textNumber = (EditText) findViewById(R.id.monitoringToleransiTextNumber);	
			textNumber.setText(toleransi+"");
		}
	}
	
	public int getToleransi(){
		return toleransi;
	}
	private int toleransi = 0;
	private Handler mHandler;

}
