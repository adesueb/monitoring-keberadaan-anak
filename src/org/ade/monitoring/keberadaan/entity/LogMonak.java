package org.ade.monitoring.keberadaan.entity;

public class LogMonak {
	
	public String getIdLog() {
		return idLog;
	}
	public void setIdLog(String idLog) {
		this.idLog = idLog;
	}
	public Lokasi getLokasi() {
		return lokasi;
	}
	public void setLokasi(Lokasi lokasi) {
		this.lokasi = lokasi;
	}
	public Anak getAnak() {
		return anak;
	}
	public void setAnak(Anak anak) {
		this.anak = anak;
	}
	
	private String 	idLog;
	private Lokasi 	lokasi;
	private Anak 	anak;
}
