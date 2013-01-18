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

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}
	
	public Anak getAnak() {
		return anak;
	}

	public void setAnak(Anak anak) {
		this.anak = anak;
	}

	public String toString(){
		return time+","+mLatitude+","+mLongitude;
	}

	private String id;
	private double mLatitude;
	private double mLongitude;
	private long   time;
	
	private Anak anak;
}
