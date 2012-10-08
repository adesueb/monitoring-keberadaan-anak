package org.ade.monitoring.keberadaan.util;

import java.util.List;

import org.ade.monitoring.keberadaan.entity.DataMonitoring;
import org.ade.monitoring.keberadaan.entity.DateMonitoring;
import org.ade.monitoring.keberadaan.entity.DayMonitoring;
import org.ade.monitoring.keberadaan.entity.Lokasi;
import org.ade.monitoring.keberadaan.entity.PesanData;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MonakJsonConverter {
	public static DataMonitoring convertJsonToDataMonitoring(String json){
		DataMonitoring dataMonitoring = new DataMonitoring();
		try {
			JSONObject object = new JSONObject(json);
			dataMonitoring.setKeterangan(object.getString(KETERANGAN));
			dataMonitoring.setStatus(object.getInt(STATUS));
			// set data monitoring.......................
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return dataMonitoring;
	}
	
	public static String convertPesanDataToJson(PesanData pesanData){
		String jsonTextDataMonitoring = MonakJsonConverter.convertDataMonitoringToJson(pesanData.getDataMonitoring());
		String jsonTextResult = "";
		try {
			JSONObject jsonPesanData = new JSONObject();
			jsonPesanData.put(DATAMONITORING, jsonTextDataMonitoring);
			jsonPesanData.put(TIPE, pesanData.getTipe());
			jsonTextResult = jsonPesanData.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonTextResult;
	}
	
	public static  String convertDataMonitoringToJson(DataMonitoring dataMonitoring){
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
			String keterangan = dataMonitoring.getKeterangan();
			String phoneNumber = dataMonitoring.getAnak().getNoHpAnak();
			
			JSONObject object = new JSONObject();
			try {
				object.put(LATITUDE, latitude);
				object.put(LONGITUDE, longitude);
				object.put(MULAI, mulai);
				object.put(SELESAI, selesai);
				object.put(STATUS, status);
				object.put(TOLERANSI, toleransi);
				object.put(ID_ORTU, idOrtu);
				object.put(NO_HP_ANAK, phoneNumber);
				object.put(KETERANGAN, keterangan);
				
				JSONArray arrayHaris = new JSONArray();
				for(DayMonitoring hari : haris){
					arrayHaris.put(hari.getHari());
				}
				object.put(HARIS, arrayHaris);
				
				JSONArray arrayTanggals = new JSONArray();
				for(DateMonitoring tanggal : tanggals){
					arrayTanggals.put(tanggal.getDate());
				}
				object.put(TANGGALS, arrayTanggals);
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return object.toString();
		}else{
			return "";
		}
	}
	
	private final static String TIPE			= "tipe";
	private final static String DATAMONITORING	= "dataMonitoring";
	private final static String KETERANGAN 		= "keterangan";
	private final static String LATITUDE		= "latitude";
	private final static String LONGITUDE		= "longitude";
	private final static String STATUS			= "status";
	private final static String MULAI 			= "mulai";
	private final static String SELESAI			= "selesai";
	private final static String TOLERANSI		= "toleransi";
	private final static String ID_ORTU			= "idOrtu";
	private final static String NO_HP_ANAK		= "noHpAnak";
	private final static String HARIS			= "haris";
	private final static String TANGGALS		= "tanggals";
	
}
