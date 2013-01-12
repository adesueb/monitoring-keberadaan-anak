package org.ade.monitoring.keberadaan.map.view;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.android.maps.OverlayItem;

public class PetaOverlay extends MonitoringOverlay{

	public PetaOverlay(Drawable defaultMarker, Context context) {
		super(boundCenterBottom(defaultMarker));
		mContext 	= context;
		
	}

	@Override
	protected OverlayItem createItem(int i) {
		return mOverlays.get(i);
	}
	
	@Override
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
