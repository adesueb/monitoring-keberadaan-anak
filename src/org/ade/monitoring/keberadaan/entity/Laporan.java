package org.ade.monitoring.keberadaan.entity;

import java.util.List;

public class Laporan {
	
	public List<String> getSeries() {
		return series;
	}
	
	public void setSeries(List<String> series) {
		this.series = series;
	}
	
	public List<String> getCategories() {
		return categories;
	}
	
	public void setCategories(List<String> categories) {
		this.categories = categories;
	}
	
	public List<Integer> getValues() {
		return values;
	}
	
	public void setValues(List<Integer> values) {
		this.values = values;
	}
	
	private List<String> series;
	private List<String> categories;
	private List<Integer> values;
}
