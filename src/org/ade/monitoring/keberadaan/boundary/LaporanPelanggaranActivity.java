package org.ade.monitoring.keberadaan.boundary;

import org.ade.monitoring.keberadaan.boundary.submenu.LaporanPelanggaranView;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class LaporanPelanggaranActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LaporanPelanggaranView mView = new LaporanPelanggaranView(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(mView);
	}

}
