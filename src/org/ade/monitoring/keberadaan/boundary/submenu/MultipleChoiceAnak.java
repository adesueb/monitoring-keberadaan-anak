package org.ade.monitoring.keberadaan.boundary.submenu;

import org.ade.monitoring.keberadaan.Variable.Operation;
import org.ade.monitoring.keberadaan.boundary.DaftarAnak;

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
		getActivity().showDialog(Operation.DETAIL, bundle);
		this.dismiss();
	}

	private final DaftarAnak daftarAnak;

}
