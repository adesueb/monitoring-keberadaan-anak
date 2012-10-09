package org.ade.monitoring.keberadaan.entity;

public class Peringatan {
	
	public String getIdMonitoring() {
		return idMonitoring;
	}
	public void setIdMonitoring(String idMonitoring) {
		this.idMonitoring = idMonitoring;
	}
	public Lokasi getLokasiAnak() {
		return lokasiAnak;
	}
	public void setLokasiAnak(Lokasi lokasiAnak) {
		this.lokasiAnak = lokasiAnak;
	}
	
	private String idMonitoring;
	private Lokasi lokasiAnak;
}
