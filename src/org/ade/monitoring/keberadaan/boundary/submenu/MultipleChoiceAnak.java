package org.ade.monitoring.keberadaan.boundary.submenu;

import org.ade.monitoring.keberadaan.boundary.DaftarAnak;
import org.ade.monitoring.keberadaan.boundary.DaftarMonitoring;
import org.ade.monitoring.keberadaan.map.view.Peta;

import android.content.Intent;
import android.os.Bundle;

public class MultipleChoiceAnak extends MultipleChoiceMonak{

	public MultipleChoiceAnak(DaftarAnak daftarAnak, Bundle bundle) {
		super(daftarAnak, bundle, true);
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
	protected void onLog(Bundle bundle) {
		Intent intent = new Intent(getActivity(), Peta.class);
		bundle.putInt(Peta.EXTRA_ACTION, Peta.EXTRA_LOG);
		intent.putExtras(bundle);
		getActivity().startActivity(intent);
	}

	@Override
	protected void onTrack(Bundle bundle) {
		Intent intent = new Intent(getActivity(), Peta.class);
		bundle.putInt(Peta.EXTRA_ACTION, Peta.EXTRA_TRACK);
		intent.putExtras(bundle);
		getActivity().startActivity(intent);		
	}
	
	private final DaftarAnak daftarAnak;

	

}
