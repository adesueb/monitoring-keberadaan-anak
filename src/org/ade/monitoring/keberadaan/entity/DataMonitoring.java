package org.ade.monitoring.keberadaan.entity;


import java.util.*;


public class DataMonitoring {

  
	public DataMonitoring () { };
      
    public Lokasi getLokasi() {
		return lokasi;
	}

	public void setLokasi(Lokasi lokasi) {
		this.lokasi = lokasi;
	}

	public String getIdMonitoring() {
		return idMonitoring;
	}

	public void setIdMonitoring(String idMonitoring) {
		this.idMonitoring = idMonitoring;
	}
	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	public Anak getAnak() {
		return anak;
	}
	public int getTolerancy() {
		return tolerancy;
	}

	public void setTolerancy(int tolerancy) {
		this.tolerancy = tolerancy;
	}

	public void setAnak(Anak anak) {
		this.anak = anak;
	}
	
	public List<WaktuMonitoring> getWaktuMonitorings() {
		return waktuMonitorings;
	}

	public void setWaktuMonitorings(List<WaktuMonitoring> waktuMonitorings) {
		this.waktuMonitorings = waktuMonitorings;
	}

	public boolean isTerlarang(){
		if(status == TERLARANG){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean isSeharusnya(){
		if(status == SEHARUSNYA){
			return true;
		}else{
			return false;
		}
	}


	private String 	idMonitoring;
	private int		status;
	private Lokasi 	lokasi;
    private Anak	anak;
    private int		tolerancy;
    
    private List<WaktuMonitoring> waktuMonitorings;
    
    private final static int SEHARUSNYA = 0;
    private final static int TERLARANG	= 1;

}
