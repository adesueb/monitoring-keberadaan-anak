package org.ade.monitoring.keberadaan.entity;

import java.util.Calendar;
import java.util.Date;

public class Pelanggaran {
	
	public String getIdPelanggaran() {
		return idPelanggaran;
	}
	public void setIdPelanggaran(String idPelanggaran) {
		this.idPelanggaran = idPelanggaran;
	}
	public Anak getAnak() {
		return anak;
	}
	public void setAnak(Anak anak) {
		this.anak = anak;
	}
	public long getWaktuPelanggaran() {
		return waktuPelanggaran;
	}
	public Date getWaktuPelanggaranDate(){
		Calendar calMulai = Calendar.getInstance();
		calMulai.setTimeInMillis(waktuPelanggaran);
		return calMulai.getTime();
	}
	public void setWaktuPelanggaran(long waktuPelanggaran) {
		this.waktuPelanggaran = waktuPelanggaran;
	}
	public Lokasi getLokasi() {
		return lokasi;
	}
	public void setLokasi(Lokasi lokasi) {
		this.lokasi = lokasi;
	}
	
	private Anak	anak;
	private String 	idPelanggaran;
	private long 	waktuPelanggaran;
	private Lokasi 	lokasi;}
