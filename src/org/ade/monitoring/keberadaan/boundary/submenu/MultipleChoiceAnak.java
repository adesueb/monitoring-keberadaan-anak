package org.ade.monitoring.keberadaan.boundary.submenu;

import org.ade.monitoring.keberadaan.MonitoringKeberadaan;
import org.ade.monitoring.keberadaan.boundary.DaftarAnak;
import org.ade.monitoring.keberadaan.boundary.DaftarMonitoring;

import android.content.Intent;
import android.os.Bundle;

public class MultipleChoiceAnak extends MultipleChoice{

	public MultipleChoiceAnak(DaftarAnak daftarAnak, Bundle bundle,
			boolean withDetail) {
		super(daftarAnak, bundle, withDetail);
		this.daftarAnak = daftarAnak;
		setTitle("Pilih Sub Menu : ");
	}

	@Override
	protected void onDelete(Bundle bundle) {
		daftarAnak.onDelete(bundle);
		this.dismiss();
	}

	@Override
	protected void onEdit(Bundle bundle) {
		daftarAnak.onEdit(bundle);
		this.dismiss();
	}

	@Override
	protected void onDetail(Bundle bundle) {
		//TODO : go to dataMonitoring....
		Intent intent = new Intent(getActivity(), DaftarMonitoring.class);
		intent.putExtras(getBundle());
		getActivity().startActivity(intent);
		this.dismiss();
		getActivity().finish();
	}

	private final DaftarAnak daftarAnak;

}
