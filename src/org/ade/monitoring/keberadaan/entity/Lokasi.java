package org.ade.monitoring.keberadaan.entity;


public class Lokasi {

 
	public Lokasi (double latitude, double longitude) {
		mLatitude 	= latitude;
		mLongitude 	= longitude;
	}
	
	public Lokasi(){}
	  
	
	public double getLongitude(  ){
		return mLongitude;
	}
	
	public double getlatitude(  ){
		return mLatitude;
	}
	
	
	public void setLongitude( double d ){
		mLongitude = d;
	}
	
	
	public void setLatitude( double d ){
		mLatitude = d;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}



	private String id;
	private double mLatitude;
	private double mLongitude;
}
