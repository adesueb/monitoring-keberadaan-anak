package org.ade.monitoring.keberadaan.boundary;

import org.ade.monitoring.keberadaan.boundary.submenu.LaporanBarView;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class LaporanPelanggaranActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LaporanBarView mView = new LaporanBarView(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(mView);
	}

}
