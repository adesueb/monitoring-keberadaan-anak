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
	
	public void setWaktuMulai(long waktuMulai) {
		this.waktuMulai = waktuMulai;
	}
	
	public void setWaktuSelesai(long waktuSelesai) {
		this.waktuSelesai = waktuSelesai;
	}
		
	public Date getWaktuMulaiInDate() {
		Calendar calMulai = Calendar.getInstance();
		calMulai.setTimeInMillis(waktuMulai);
		return calMulai.getTime();
	}
	
	public long getWaktuSelesai(){
		
		return waktuSelesai;
	}
	
	public long getWaktuMulai(){
		return waktuMulai;
	}

	public Date getWaktuSelesaiInDate() {
		Calendar calSelesai = Calendar.getInstance();
		calSelesai.setTimeInMillis(waktuSelesai);
		return calSelesai.getTime();		
	}
	

	public List<Integer> getHaris() {
		return haris;
	}

	public void setHaris(List<Integer> haris) {
		this.haris = haris;
	}

	public List<Long> getTanggals() {
		return tanggals;
	}

	public void setTanggals(List<Long> tanggals) {
		this.tanggals = tanggals;
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
    private long 	waktuMulai;
    private long 	waktuSelesai;
    private List<Integer> 	haris;
    private List<Long>		tanggals;
    
    private final static int SEHARUSNYA = 0;
    private final static int TERLARANG	= 1;

}
