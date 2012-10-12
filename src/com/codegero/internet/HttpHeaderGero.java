package com.codegero.internet;

public class HttpHeaderGero {

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String toString(){
		return id+": "+value+"\r\n";
	}
	private String id;
	private String value;
}
