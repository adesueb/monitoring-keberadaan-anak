package org.ade.monak.ortu.entity;


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
		if(anak!=null){
			anak.setLastLokasi(this);	
		}
		return anak;
	}

	public void setAnak(Anak anak) {
		this.anak = anak;	
		
	}
	public boolean isLog() {
		return log;
	}

	public void setLog(boolean log) {
		this.log = log;
	}

	public String toString(){
		return time+","+mLatitude+","+mLongitude;
	}

	private String 	id;
	private double 	mLatitude;
	private double 	mLongitude;
	private long   	time;
	private boolean log;
	
	private Anak anak;
}
