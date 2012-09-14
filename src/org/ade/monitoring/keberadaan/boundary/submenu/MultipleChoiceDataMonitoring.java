package org.ade.monitoring.keberadaan.boundary.submenu;

import org.ade.monitoring.keberadaan.boundary.DaftarMonitoring;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;

public class MultipleChoiceDataMonitoring extends MultipleChoice{

	public MultipleChoiceDataMonitoring(DaftarMonitoring activity, Bundle bundle,
			boolean withDetail) {
		super(activity, bundle, withDetail);
		daftarMonitoring = activity;
	}

	@Override
	protected void onDelete(Bundle bundle) {
		daftarMonitoring.delete(bundle);
	}

	@Override
	protected void onEdit(Bundle bundle) {
		daftarMonitoring.edit(bundle);
	}

	@Override
	protected void onDetail(Bundle bundle) {
		
	}
	
	private DaftarMonitoring daftarMonitoring;

}
