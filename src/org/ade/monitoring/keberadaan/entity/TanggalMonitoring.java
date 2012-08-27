package org.ade.monitoring.keberadaan.entity;

import java.util.Calendar;
import java.util.Date;

public class TanggalMonitoring {
	
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

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}
	public int getDate() {
		return date;
	}

	public void setDate(int date) {
		this.date = date;
	}




	private String 			idWaktuMonitoring;
	private DataMonitoring 	dataMonitoring;

	private int date;
    private int day;
}
