package org.ade.monitoring.keberadaan.util;

import java.util.ArrayList;
import java.util.List;

import org.ade.monitoring.keberadaan.Variable.TipePesanData;
import org.ade.monitoring.keberadaan.entity.Anak;
import org.ade.monitoring.keberadaan.entity.DataMonitoring;
import org.ade.monitoring.keberadaan.entity.DateMonitoring;
import org.ade.monitoring.keberadaan.entity.DayMonitoring;
import org.ade.monitoring.keberadaan.entity.Lokasi;
import org.ade.monitoring.keberadaan.entity.Peringatan;
import org.ade.monitoring.keberadaan.entity.IPesanData;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MonakJsonConverter {
	public static Peringatan convertJsonToPeringatan(String json){
		Peringatan peringatan = new Peringatan();
		try {
			JSONObject object = new JSONObject(json);
			peringatan.setIdMonitoring(object.getString(ID_MONITORING));
			Lokasi lokasi = new Lokasi();
			lokasi.setLatitude(object.getDouble(LATITUDE));
			lokasi.setLongitude(object.getDouble(LONGITUDE));
			peringatan.setLokasiAnak(lokasi);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return peringatan;
	}
	public static DataMonitoring convertJsonToDataMonitoring(String json){
		DataMonitoring dataMonitoring = new DataMonitoring();
		try {
			JSONObject object = new JSONObject(json);
			dataMonitoring.setIdMonitoring(object.getString(ID_MONITORING));
			dataMonitoring.setKeterangan(object.getString(KETERANGAN));
			dataMonitoring.setStatus(object.getInt(STATUS));
			
			Lokasi lokasi = new Lokasi();
			lokasi.setLatitude(object.getDouble(LATITUDE));
			lokasi.setLongitude(object.getDouble(LONGITUDE));
			dataMonitoring.setLokasi(lokasi);
			
			Anak anak = new Anak();
			anak.setNoHpAnak(object.getString(NO_HP_ANAK));
			anak.setIdOrtu(object.getString(ID_ORTU));
			dataMonitoring.setAnak(anak);
			
			dataMonitoring.setTolerancy(object.getInt(TOLERANSI));
			dataMonitoring.setWaktuMulai(object.getLong(MULAI));
			dataMonitoring.setWaktuSelesai(object.getLong(SELESAI));
			
			List<DayMonitoring> haris = new ArrayList<DayMonitoring>();
			JSONArray arrHaris = object.getJSONArray(HARIS);
			for(int i=0;i<arrHaris.length();i++){
				DayMonitoring hari = new DayMonitoring();
				hari.setHari(arrHaris.getInt(i));
				hari.setDataMonitoring(dataMonitoring);
				haris.add(hari);
			}
			dataMonitoring.setHaris(haris);
			
			List<DateMonitoring> tanggals = new ArrayList<DateMonitoring>();
			JSONArray arrTanggals = object.getJSONArray(TANGGALS);
			for(int i=0;i<arrTanggals.length();i++){
				DateMonitoring tanggal = new DateMonitoring();
				tanggal.setDataMonitoring(dataMonitoring);
				tanggals.add(tanggal);
			}
			dataMonitoring.setTanggals(tanggals);
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return dataMonitoring;
	}
	
	public static IPesanData convertJsonToPesanData(String json){
		IPesanData pesanData = null;
		try {
			JSONObject object = new JSONObject(json);
			int tipe = object.getInt(TIPE);
			if(tipe==TipePesanData.DATAMONITORING_BARU){
				pesanData=convertJsonToDataMonitoring(object.getJSONObject(DATAMONITORING).toString());
			}else{
				pesanData=convertJsonToPeringatan(object.getJSONObject(PERINGATAN).toString());
			}

			pesanData.setTipe(object.getInt(TIPE));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return pesanData;
	}
	
	public static String convertPesanDataToJson(IPesanData pesanData){
		String jsonTextResult = "";
		try {
			JSONObject jsonPesanData = new JSONObject();
			jsonPesanData.put(TIPE, pesanData.getTipe());
			if(pesanData.getTipe()==TipePesanData.DATAMONITORING_BARU){				
				jsonPesanData.put(DATAMONITORING, 
						MonakJsonConverter.convertDataMonitoringToJson((DataMonitoring) pesanData));	
			}else{
				jsonPesanData.put(PERINGATAN, convertPeringatanToJson((Peringatan) pesanData));
			}
			jsonTextResult = jsonPesanData.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonTextResult;
	}
	
	public static String  convertPeringatanToJson(Peringatan peringatan){

		if(peringatan!=null){
			JSONObject object = new JSONObject();
			try {
				object.put(ID_MONITORING, peringatan.getIdMonitoring());
				Lokasi lokasi = new Lokasi();
				object.put(LATITUDE, lokasi.getlatitude());
				object.put(LONGITUDE, lokasi.getLongitude());
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return object.toString();
		}
		return "";
	
	}
	
	public static  String convertDataMonitoringToJson(DataMonitoring dataMonitoring){
		if(dataMonitoring!=null){
			Lokasi lokasi 		= dataMonitoring.getLokasi();
			double latitude		= lokasi.getlatitude();
			double longitude 	= lokasi.getLongitude();
			long 	mulai 		= dataMonitoring.getWaktuMulai();
			long 	selesai 	= dataMonitoring.getWaktuSelesai();
			int 	status 		= dataMonitoring.getStatus();
			String idMonitoring	= dataMonitoring.getIdMonitoring();
			List<DayMonitoring> 	haris 		= dataMonitoring.getHaris();
			List<DateMonitoring> 	tanggals 	= dataMonitoring.getTanggals();
			int toleransi = dataMonitoring.getTolerancy();
			String idOrtu = dataMonitoring.getAnak().getIdOrtu();
			String keterangan = dataMonitoring.getKeterangan();
			String phoneNumber = dataMonitoring.getAnak().getNoHpAnak();
			
			JSONObject object = new JSONObject();
			try {
				object.put(ID_MONITORING, idMonitoring);
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
		}
		return "";
		
	}
	
	private final static String TIPE			= "tipe";
	private final static String PERINGATAN		= "peringatan";
	private final static String DATAMONITORING	= "dataMonitoring";
	private final static String ID_MONITORING	= "idMonitoring";
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
