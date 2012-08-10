package org.ade.monitoring.keberadaan.entity;

import java.util.List;



public class Anak {

    public Anak () { }
  
    
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

	private String idAnak;
    private String namaAnak;
    private String noHpAnak;
    
    private List<Pelanggaran> pelanggarans;
}
