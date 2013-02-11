package org.ade.monak.ortu.entity;


import java.util.*;

import org.ade.monak.ortu.util.MonakJsonConverter;


public class DataMonitoring implements IPesanData, IEntity{

  
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
	
	public String getKeterangan() {
		return keterangan;
	}

	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}
	
	public List<DayMonitoring> getHaris() {
		if(haris!=null){
			for(DayMonitoring hari:haris){
				hari.setDataMonitoring(this);
			}
		}
		return haris;
	}

	public void setHaris(List<DayMonitoring> haris) {
		this.haris = haris;
	}

	public List<DateMonitoring> getTanggals() {
		if(tanggals!=null){
			for(DateMonitoring date:tanggals){
				date.setDataMonitoring(this);
			}
		}
		return tanggals;
	}

	public void setTanggals(List<DateMonitoring> tanggals) {
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
	
	public boolean isAktif() {
		return isAktif;
	}

	public void setAktif(boolean isAktif) {
		this.isAktif = isAktif;
	}

	public void setTipe(int tipe) {
		this.tipe = tipe;
	}

	public int getTipe() {
		return tipe;
	}

	public String getJsonPesanData() {
		return MonakJsonConverter.convertPesanDataToJson(this);
	}
	
	public String getIdEntity() {
		return idMonitoring;
	}

	private String	keterangan;
	private String 	idMonitoring;
	private int		status;
	private Lokasi 	lokasi;
    private Anak	anak;
    private int		tolerancy;
    private long 	waktuMulai;
    private long 	waktuSelesai;
    private boolean isAktif;
    private int 	tipe;
    private List<DayMonitoring> haris;
    private List<DateMonitoring>tanggals;
    
    public final static int SEHARUSNYA = 0;
    public final static int TERLARANG	= 1;



}
