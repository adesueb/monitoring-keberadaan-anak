package org.ade.monitoring.keberadaan.entity;

import java.util.Calendar;
import java.util.Date;

public class WaktuMonitoring {
	
	public String getIdWaktuMonitoring() {
		return idWaktuMonitoring;
	}

	public void setIdWaktuMonitoring(String idWaktuMonitoring) {
		this.idWaktuMonitoring = idWaktuMonitoring;
	}

	public DataMonitoring getDataMonitoring() {
		return dataMonitoring;
	}

	public void setDataMonitoring(DataMonitoring dataMonitoring) {
		this.dataMonitoring = dataMonitoring;
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
	
	private String 			idWaktuMonitoring;
	private DataMonitoring 	dataMonitoring;

    private long 	waktuMulai;
    private long 	waktuSelesai;
}
