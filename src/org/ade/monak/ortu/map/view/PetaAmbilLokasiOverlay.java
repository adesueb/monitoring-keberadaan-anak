package org.ade.monak.ortu.map.view;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class PetaAmbilLokasiOverlay extends MonitoringOverlay{

	public PetaAmbilLokasiOverlay(Drawable defaultMarker, Context context, Handler handler) {
		super(boundCenterBottom(defaultMarker));
		mHandler	= handler;
		
	}

	@Override
	protected OverlayItem createItem(int i) {
		return mOverlays.get(i);
	}


	@Override
	public boolean onTap(GeoPoint p, MapView mapView) {
		
		if ( isPinch ){
	        return false;
	    }else{
	        if ( p!=null ){
	        	if(mHandler!=null){
	        		Message message = new Message();
		        	Bundle bundle = new Bundle();
		        	bundle.putDouble("latitude", p.getLatitudeE6()/1E6);
		        	bundle.putDouble("longitude", p.getLongitudeE6()/1E6);
		        	message.setData(bundle);
		            mHandler.sendMessage(message);	
	        	}
	        	return true;            // We handled the tap
	        }else{
	            return false;           // Null GeoPoint
	        }
	    }
	}

	
	public void addOverLay(OverlayItem overLay){
		mOverlays.add(overLay);
		populate();
	}


	@Override
	public int size() {
		return mOverlays.size();
	}

	@Override
	public boolean onTouchEvent(MotionEvent e, MapView mapView)
	{
	    int fingers = e.getPointerCount();
	    if( e.getAction()==MotionEvent.ACTION_DOWN ){
	        isPinch=false;  // Touch DOWN, don't know if it's a pinch yet
	    }
	    if( e.getAction()==MotionEvent.ACTION_MOVE && fingers==2 ){
	        isPinch=true;   // Two fingers, def a pinch
	    }
	    return super.onTouchEvent(e,mapView);
	}
	
	private boolean isPinch  =  false;
	private Handler mHandler;
	private List<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
}

