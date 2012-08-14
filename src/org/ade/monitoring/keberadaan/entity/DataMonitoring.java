package org.ade.monitoring.keberadaan.entity;


import java.util.*;


public class DataMonitoring {

  
	public DataMonitoring () { };
  
    public void setTanda( Lokasi lokasi, long waktuMulai, long waktuSelesai ){
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

	

	public void setWaktuMulai(long waktuMulai) {
		this.waktuMulai = waktuMulai;
	}
	
	public void setWaktuSelesai(long waktuSelesai) {
		this.waktuSelesai = waktuSelesai;
	}
	
	public long getWaktuSelesaiLong(){
		
		return waktuSelesai;
	}
	
	public Date getWaktuMulaiDate() {
		Calendar calMulai = Calendar.getInstance();
		calMulai.setTimeInMillis(waktuMulai);
		return calMulai.getTime();
	}
	
	public long getWaktuMulaiLong(){
		return waktuMulai;
	}

	public Date getWaktuSelesaiDate() {
		Calendar calSelesai = Calendar.getInstance();
		calSelesai.setTimeInMillis(waktuSelesai);
		return calSelesai.getTime();		
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
    private long 	waktuMulai;
    private long 	waktuSelesai;
    
    private final static int SEHARUSNYA = 0;
    private final static int TERLARANG	= 1;

}
