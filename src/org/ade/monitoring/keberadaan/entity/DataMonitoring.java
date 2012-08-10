package org.ade.monitoring.keberadaan.entity;


import java.util.*;


public class DataMonitoring {

  
	public DataMonitoring () { };
  
    public void setTanda( Lokasi lokasi, Date waktuMulai, Date waktuSelesai ){
    	setLokasi(lokasi);
    	setWaktuMulai(waktuMulai);
    	setWaktuSelesai(waktuSelesai);
    }
    
    public Lokasi getLokasi() {
		return lokasi;
	}

	public void setLokasi(Lokasi lokasi) {
		this.lokasi = lokasi;
	}

	public Date getWaktuMulai() {
		return waktuMulai;
	}

	public void setWaktuMulai(Date waktuMulai) {
		this.waktuMulai = waktuMulai;
	}

	public Date getWaktuSelesai() {
		return waktuSelesai;
	}

	public void setWaktuSelesai(Date waktuSelesai) {
		this.waktuSelesai = waktuSelesai;
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
    private Date 	waktuMulai;
    private Date 	waktuSelesai;
    
    private final static int SEHARUSNYA = 0;
    private final static int TERLARANG	= 1;

}
