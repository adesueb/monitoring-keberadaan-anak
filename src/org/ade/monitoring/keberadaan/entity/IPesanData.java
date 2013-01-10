package org.ade.monitoring.keberadaan.entity;

import java.util.List;

import org.ade.monitoring.keberadaan.Variable.TipePesanMonitoring;
import org.ade.monitoring.keberadaan.util.MonakJsonConverter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public interface IPesanData {
	
	public void setTipe(int tipe);
	
	public int getTipe();
		
	public String getJsonPesanData();
	
}
