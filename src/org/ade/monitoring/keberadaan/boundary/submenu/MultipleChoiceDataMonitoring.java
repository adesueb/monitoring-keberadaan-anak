package org.ade.monitoring.keberadaan.boundary.submenu;

import org.ade.monitoring.keberadaan.boundary.DaftarMonitoring;

import android.os.Bundle;

public class MultipleChoiceDataMonitoring extends MultipleChoiceMonak{

	public MultipleChoiceDataMonitoring(DaftarMonitoring activity, Bundle bundle) {
		super(activity, bundle, false);
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
	
	@Override
	protected void onLog(Bundle bundle) {		
	}

	@Override
	protected void onTrack(Bundle bundle) {		
	}

	private DaftarMonitoring daftarMonitoring;

}
