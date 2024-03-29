package org.ade.monak.ortu.entity;

import java.util.List;

public class Anak implements IEntity{

    public Anak () { }
	
	public String getIdOrtu() {
		return idOrtu;
	}
	public void setIdOrtu(String idOrtu) {
		this.idOrtu = idOrtu;
	}
	
	public String getIdAnak() {
		return idAnak;
	}
	public void setIdAnak(String idAnak) {
		this.idAnak = idAnak;
	}
	
	public String getNamaAnak() {
		return namaAnak;
	}
	public void setNamaAnak(String namaAnak) {
		this.namaAnak = namaAnak;
	}
	
	public String getNoHpAnak() {
		return noHpAnak;
	}
	public void setNoHpAnak(String noHpAnak) {
		this.noHpAnak = noHpAnak;
	}
	
	public List<Pelanggaran> getPelanggarans() {
		return pelanggarans;
	}
	public void setPelanggarans(List<Pelanggaran> pelanggarans) {
		this.pelanggarans = pelanggarans;
	}
	
	public List<DataMonitoring> getDataMonitorings() {
		return dataMonitorings;
	}
	public void setDataMonitorings(List<DataMonitoring> dataMonitorings) {
		this.dataMonitorings = dataMonitorings;
	}
	
	public List<Lokasi> getLokasis() {
		return lokasis;
	}
	public void setLokasis(List<Lokasi> lokasis) {
		this.lokasis = lokasis;
	}
	
	public Lokasi getLastLokasi() {
		if(lastLokasi!=null){
			lastLokasi.setAnak(this);	
		}
		return lastLokasi;
	}
	public void setLastLokasi(Lokasi lastLokasi) {
		this.lastLokasi = lastLokasi;

	}
	public boolean isTrack() {
		return isTrack;
	}

	public void setTrack(boolean isTrack) {
		this.isTrack = isTrack;
	}
	
	public String getNoImeiAnak() {
		return noImeiAnak;
	}

	public void setNoImeiAnak(String noImeiAnak) {
		this.noImeiAnak = noImeiAnak;
	}

	public String getIdEntity() {
		return idAnak;
	}
	
	private String idAnak;
	// id ortu is no hp ortu....
	private String idOrtu;
	//..........................
    private String namaAnak;
    private String noImeiAnak;
    private String noHpAnak;
    private Lokasi lastLokasi;
    private boolean isTrack;
    
    private List<Lokasi>			lokasis;
    private List<Pelanggaran> 		pelanggarans;
    private List<DataMonitoring> 	dataMonitorings;

}
