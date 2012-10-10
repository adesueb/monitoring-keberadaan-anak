package org.ade.monitoring.keberadaan.entity;

import java.util.List;



public class Anak {

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
	public Lokasi getLokasi() {
		return lokasi;
	}
	public void setLokasi(Lokasi lokasi) {
		this.lokasi = lokasi;
	}
	
	private String idAnak;
	private String idOrtu;
    private String namaAnak;
    private String noHpAnak;
    
    private Lokasi lokasi;
    
    private List<Pelanggaran> 		pelanggarans;
    private List<DataMonitoring> 	dataMonitorings;
}
