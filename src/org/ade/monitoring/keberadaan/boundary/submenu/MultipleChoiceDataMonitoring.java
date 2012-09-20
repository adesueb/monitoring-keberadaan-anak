package org.ade.monitoring.keberadaan.boundary.submenu;

import org.ade.monitoring.keberadaan.boundary.DaftarMonitoring;

import android.os.Bundle;

public class MultipleChoiceDataMonitoring extends MultipleChoice{

	public MultipleChoiceDataMonitoring(DaftarMonitoring activity, Bundle bundle,
			boolean withDetail) {
		super(activity, bundle, withDetail);
		daftarMonitoring = activity;

		setTitle("Pilih Sub Menu : ");
	}

	@Override
	protected void onDelete(Bundle bundle) {
		daftarMonitoring.onDelete(bundle);
	}

	@Override
	protected void onEdit(Bundle bundle) {
		daftarMonitoring.onEdit(bundle);
	}

	@Override
	protected void onDetail(Bundle bundle) {
		
	}
	
	private DaftarMonitoring daftarMonitoring;

}
