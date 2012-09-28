package org.ade.monitoring.keberadaan.map;

import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public abstract class MonitoringOverlay extends ItemizedOverlay<OverlayItem>{

	public MonitoringOverlay(Drawable defaultMarker) {
		super(defaultMarker);
	}
	
	public abstract void addOverLay(OverlayItem overLay);

}
