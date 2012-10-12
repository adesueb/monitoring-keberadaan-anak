package org.ade.monitoring.keberadaan.entity;

import org.ade.monitoring.keberadaan.util.MonakJsonConverter;

public class Peringatan implements IPesanData{
	
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
	public void setTipe(int tipe) {
		this.tipe = tipe;
	}
	public int getTipe() {
		return tipe;
	}
	public String getJsonPesanData() {
		return MonakJsonConverter.convertPesanDataToJson(this);
	}
	public String getIdOrtu() {
		return idOrtu;
	}
	public void setIdOrtu(String idOrtu) {
		this.idOrtu = idOrtu;
	}


	private String idMonitoring;
	private Lokasi lokasiAnak;
	private String idOrtu;
	private int tipe;
}
