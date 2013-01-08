package org.ade.monitoring.keberadaan.map.view;

import org.ade.monitoring.keberadaan.entity.DataMonitoring;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class MonitoringCircleOverlay extends Overlay {
	
	public MonitoringCircleOverlay(DataMonitoring dataMonitoring) {
		
		GeoPoint point = new GeoPoint((int)(dataMonitoring.getLokasi().getlatitude()*1E6),
				(int)(dataMonitoring.getLokasi().getLongitude()*1E6));
		
		this.dataMonitoring = dataMonitoring;
		
	    this.point = point;
	
	    paint1 = new Paint();
	    paint1.setARGB(128, 0, 0, 255);
	    paint1.setStrokeWidth(2);
	    paint1.setStrokeCap(Paint.Cap.ROUND);
	    paint1.setAntiAlias(true);
	    paint1.setDither(false);
	    paint1.setStyle(Paint.Style.STROKE);
	
	    paint2 = new Paint();
	    paint2.setARGB(64, 0, 0, 255);  
	
	}
	
	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		float radius 	= mapView.getProjection().metersToEquatorPixels(dataMonitoring.getTolerancy());
	    Point pt 		= mapView.getProjection().toPixels(point, null);
//	    float radius = (float) Math.pow(2, mapView.getZoomLevel() - 10); 
	
	    if(radius < canvas.getHeight()/25){
	        radius = canvas.getHeight()/25;
	    }
	
	    canvas.drawCircle(pt.x, pt.y, radius, paint2);
	    canvas.drawCircle(pt.x, pt.y, radius, paint1);
	
	}
	
	public static int metersToRadius(float meters, MapView map, double latitude) {
	    return (int) (map.getProjection().metersToEquatorPixels(meters) * (1/ Math.cos(Math.toRadians(latitude))));         
	}
	

	private GeoPoint point;
	private Paint paint1, paint2;
	private DataMonitoring dataMonitoring;

}
