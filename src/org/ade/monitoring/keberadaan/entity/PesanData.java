package org.ade.monitoring.keberadaan.entity;

import java.util.List;

import org.ade.monitoring.keberadaan.util.MonakJsonConverter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class PesanData {


	public PesanData () {
	}
  
	public void setDataMonitoring( DataMonitoring dataMonitoring ){
		this.dataMonitoring = dataMonitoring;
	}
	
	public void setTipe(int tipe){
		this.tipe = tipe;
	}
  
	public DataMonitoring getDataMonitoring(){
		return dataMonitoring;
	}
	
	public int getTipe(){
		return tipe;
	}
	
	public void setPesanDataFromJson(String json){
		
	}
		
	public String getJsonPesanData(){
		return MonakJsonConverter.convertPesanDataToJson(this);
	}
  
	private DataMonitoring 	dataMonitoring;
	private int tipe;
	

}
