package org.ade.monitoring.keberadaan.entity;

public class DayMonitoring {

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public DataMonitoring getDataMonitoring() {
		return dataMonitoring;
	}
	public void setDataMonitoring(DataMonitoring dataMonitoring) {
		this.dataMonitoring = dataMonitoring;
	}
	public int getHari() {
		return hari;
	}
	public void setHari(int hari) {
		this.hari = hari;
	}
	private String id;
	private DataMonitoring dataMonitoring;
	private int hari;
}
