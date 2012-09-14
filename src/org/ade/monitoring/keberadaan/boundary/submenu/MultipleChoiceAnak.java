package org.ade.monitoring.keberadaan.boundary.submenu;

import org.ade.monitoring.keberadaan.Variable.Operation;

import android.app.Activity;
import android.os.Bundle;

public class MultipleChoiceAnak extends MultipleChoice{

	public MultipleChoiceAnak(Activity activity, Bundle bundle,
			boolean withDetail) {
		super(activity, bundle, withDetail);
	}

	@Override
	protected void onDelete(Bundle bundle) {
		getActivity().showDialog(Operation.DELETE, bundle);
	}

	@Override
	protected void onEdit(Bundle bundle) {
		getActivity().showDialog(Operation.EDIT, bundle);
	}

	@Override
	protected void onDetail(Bundle bundle) {
		getActivity().showDialog(Operation.DETAIL, bundle);
	}


}
