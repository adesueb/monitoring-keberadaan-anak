package org.ade.monitoring.keberadaan.entity;

import java.util.ArrayList;
import java.util.List;

public class OrangTua implements IEntity{
		
	public String getIdOrangtua() {
		return idOrangtua;
	}
	public void setIdOrangtua(String idOrangtua) {
		this.idOrangtua = idOrangtua;
	}
	public String getNamaOrangtua() {
		return namaOrangtua;
	}
	public void setNamaOrangtua(String namaOrangtua) {
		this.namaOrangtua = namaOrangtua;
	}
	public List<Anak> getAnaks() {
		return anaks;
	}
	public void setAnaks(List<Anak> anaks) {
		this.anaks = anaks;
	}
	public String getIdEntity() {
		return idOrangtua;
	}
	private String idOrangtua 	= "";
	private String namaOrangtua	= "";
	private List<Anak> anaks	= new ArrayList<Anak>();
	
}
