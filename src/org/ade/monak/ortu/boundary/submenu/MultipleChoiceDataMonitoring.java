package org.ade.monak.ortu.boundary.submenu;

import org.ade.monak.ortu.boundary.DaftarMonitoring;

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
		this.dismiss();
	}

	@Override
	protected void onEdit(Bundle bundle) {
		daftarMonitoring.onEdit(bundle);
		this.dismiss();
	}
	
	@Override
	protected void onLog(Bundle bundle) {		
	}
	
	private DaftarMonitoring daftarMonitoring;

}
