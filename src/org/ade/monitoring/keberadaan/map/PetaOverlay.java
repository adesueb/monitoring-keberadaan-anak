package org.ade.monitoring.keberadaan.map;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class PetaOverlay extends ItemizedOverlay<OverlayItem>{

	public PetaOverlay(Drawable defaultMarker, Context context) {
		super(defaultMarker);
		mContext = context;
	}

	@Override
	protected OverlayItem createItem(int i) {
		return mOverlays.get(i);
	}


	@Override
	public boolean onTap(GeoPoint p, MapView mapView) {
		return super.onTap(p, mapView);
	}

	
	public void addOverLay(OverlayItem overLay){
		mOverlays.add(overLay);
		populate();
	}
	
	@Override
	protected boolean onTap(int index) {
		OverlayItem 		item 	= mOverlays.get(index);
		AlertDialog.Builder dialog 	= new AlertDialog.Builder(mContext);
		dialog.setTitle		(item.getTitle());
		dialog.setMessage	(item.getSnippet());
		dialog.show();
	  return true;
	}

	@Override
	public int size() {
		return mOverlays.size();
	}

	
	private Context mContext;
	private List<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
}
