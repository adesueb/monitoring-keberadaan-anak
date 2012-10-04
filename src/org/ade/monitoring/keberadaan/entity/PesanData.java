package org.ade.monitoring.keberadaan.entity;

import java.util.List;

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
		if(dataMonitoring!=null){
			Lokasi lokasi 		= dataMonitoring.getLokasi();
			double latitude		= lokasi.getlatitude();
			double longitude 	= lokasi.getLongitude();
			long 	mulai 		= dataMonitoring.getWaktuMulai();
			long 	selesai 	= dataMonitoring.getWaktuSelesai();
			int 	status 		= dataMonitoring.getStatus();
			List<DayMonitoring> 	haris 		= dataMonitoring.getHaris();
			List<DateMonitoring> 	tanggals 	= dataMonitoring.getTanggals();
			int toleransi = dataMonitoring.getTolerancy();
			String idOrtu = dataMonitoring.getAnak().getIdOrtu();
			String phoneNumber = dataMonitoring.getAnak().getNoHpAnak();
			
			JSONObject object = new JSONObject();
			try {
				object.put("latitude", latitude);
				object.put("longitude", longitude);
				object.put("mulai", mulai);
				object.put("selesai", selesai);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return object.toString();
		}else{
			return "";
		}
		
	}
  
	private DataMonitoring 	dataMonitoring;
	private int tipe;
	

}
