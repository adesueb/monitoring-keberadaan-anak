package org.ade.monitoring.keberadaan.lokasi;

public class LocationDistanceUtil {

	public static double distanceInMiles(double lat1, double lng1, double lat2, double lng2) {
		double earthRadius = 3958.75;
		double dLat = Math.toRadians(lat2-lat1);
		double dLng = Math.toRadians(lng2-lng1);
		double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
		           Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
		           Math.sin(dLng/2) * Math.sin(dLng/2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		
		return earthRadius * c;
	}
	
	public static float distanceInMeters(double lat1, double lng1, double lat2, double lng2){
		double dist = distanceInMiles(lat1, lng1, lat2, lng2);
		double meterConversion = 1609.344;
		
		return new Float(dist * meterConversion).floatValue();
	}
}
