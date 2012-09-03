package org.ade.monitoring.keberadaan.boundary.submenu;

import org.ade.monitoring.keberadaan.R;
import org.ade.monitoring.keberadaan.boundary.PendaftaranMonitoring;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class PilihToleransi extends Dialog{

	public PilihToleransi(Context context, final Handler handler) {
		super(context);
		setContentView(R.layout.monitoring_toleransi);
		Button buttonOk = (Button) findViewById(R.id.monitoringToleransiOk);
		buttonOk.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				EditText textNumber = (EditText) findViewById(R.id.monitoringToleransiTextNumber);
				toleransi = Integer.parseInt(textNumber.getText().toString());
				handler.sendEmptyMessage(PendaftaranMonitoring.TOLERANSI);
				dismiss();
			}
		});
	}
	
	public int getToleransi(){
		return toleransi;
	}
	private int toleransi = 0;

}
