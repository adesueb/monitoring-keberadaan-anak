package org.ade.monitoring.keberadaan.entity;

public class Pelanggaran {
	
	public String getIdPelanggaran() {
		return idPelanggaran;
	}
	public void setIdPelanggaran(String idPelanggaran) {
		this.idPelanggaran = idPelanggaran;
	}
	public String getWaktuPelanggaran() {
		return waktuPelanggaran;
	}
	public void setWaktuPelanggaran(String waktuPelanggaran) {
		this.waktuPelanggaran = waktuPelanggaran;
	}
	public Lokasi getLokasi() {
		return lokasi;
	}
	public void setLokasi(Lokasi lokasi) {
		this.lokasi = lokasi;
	}
	
	private String idPelanggaran;
	private String waktuPelanggaran;
	private Lokasi lokasi;}
