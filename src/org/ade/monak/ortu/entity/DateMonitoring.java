package org.ade.monak.ortu.entity;

public class DateMonitoring {

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
	public long getDate() {
		return date;
	}
	public void setDate(long date) {
		this.date = date;
	}
	private String id = "";
	private DataMonitoring dataMonitoring;
	private long date;
}
