package org.ade.monak.ortu.util;

import android.os.Bundle;

public interface IFormOperation{
	public void onAdd(Bundle bundle);
	public void onEdit(Bundle bundle);
	public void onDelete(Bundle bundle);
	public void onSave(Bundle bundle, int status);
}
