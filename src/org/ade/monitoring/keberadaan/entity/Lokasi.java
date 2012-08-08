package org.ade.monitoring.keberadaan.entity;


public class Lokasi {

 
	public Lokasi () { }
	  
	
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
	  
	private double mLatitude;
	private double mLongitude;
}
