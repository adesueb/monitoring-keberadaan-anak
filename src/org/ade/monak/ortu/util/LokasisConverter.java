package org.ade.monak.ortu.util;

import java.util.ArrayList;
import java.util.List;

import org.ade.monak.ortu.entity.Lokasi;
import org.ade.monak.ortu.service.storage.LogMonakFileManager;

public class LokasisConverter {

	/*
	 * format text : lokasi:lokasi:lokasi:
	 */
	public final static List<Lokasi> covertTextToLokasis(String text){
		List<Lokasi> lokasis = new ArrayList<Lokasi>(); 
		String[]splitterText = text.split(":");
		for(int i=0;i<splitterText.length-1;i++){
			lokasis.add(convertTextToSingleLokasi(splitterText[i]));
		}
		return lokasis;
	}
	
	/*
	 * format text : time,mLatitude,mLongitude,
	 */
	public final static Lokasi convertTextToSingleLokasi(String text){
		String[]splitText = text.split(",");
		LogMonakFileManager.debug("lokasi satu pesatu textnya adalah : "+text);
		Lokasi lokasi = new Lokasi();
		lokasi.setLatitude(Double.parseDouble(splitText[1]));
		lokasi.setLongitude(Double.parseDouble(splitText[2]));
		lokasi.setTime(Long.parseLong(splitText[0]));
		return lokasi;
	}
	
	public final static String convertLokasisToText(List<Lokasi> lokasis){
		String text = "";
		for(Lokasi lokasi:lokasis){
			text = text + lokasi.toString()+",:";
		}
		return text;
	}
}
