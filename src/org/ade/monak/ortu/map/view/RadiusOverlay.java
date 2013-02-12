package org.ade.monak.ortu.map.view;

import org.ade.monak.ortu.entity.Lokasi;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class RadiusOverlay extends Overlay {
	
	public RadiusOverlay(String id, Lokasi lokasi, int radius, int color) {
		
		GeoPoint point = new GeoPoint((int)(lokasi.getlatitude()*1E6),
				(int)(lokasi.getLongitude()*1E6));
		this.radius =radius;
		
		this.lokasi	= lokasi;
		
	    this.point 	= point;
	    
	    this.id		= id;
	
	    paint1 = new Paint();
	    paint1.setARGB(255, 0, 0, 0);  
	    paint1.setStrokeWidth(2);
	    paint1.setStrokeCap(Paint.Cap.ROUND);
	    paint1.setAntiAlias(true);
	    paint1.setDither(false);
	    paint1.setStyle(Paint.Style.STROKE);
	
	    paint2 = new Paint();
	    paint2.setColor(color);
	}
	
	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		float radius 	= mapView.getProjection().metersToEquatorPixels(this.radius);
	    Point pt 		= mapView.getProjection().toPixels(point, null);
//	    float radius = (float) Math.pow(2, mapView.getZoomLevel() - 10); 
	
	    if(radius < canvas.getHeight()/25){
	        radius = canvas.getHeight()/25;
	    }
	
	    canvas.drawCircle(pt.x, pt.y, radius, paint2);
	    canvas.drawCircle(pt.x, pt.y, radius, paint1);
	
	}
	
	public String getId(){
		return this.id;
	}
	
	public static int metersToRadius(float meters, MapView map, double latitude) {
	    return (int) (map.getProjection().metersToEquatorPixels(meters) * (1/ Math.cos(Math.toRadians(latitude))));         
	}
	

	private GeoPoint point;
	private Paint paint1, paint2;
	private Lokasi lokasi;
	private int radius;
	private String id;

}
