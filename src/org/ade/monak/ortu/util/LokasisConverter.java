package org.ade.monak.ortu.util;

import java.util.ArrayList;
import java.util.List;

import org.ade.monak.ortu.entity.Lokasi;

public class LokasisConverter {

	/*
	 * format text : lokasi:lokasi:lokasi:
	 */
	public final static List<Lokasi> covertTextToLokasis(String text){
		List<Lokasi> lokasis = new ArrayList<Lokasi>(); 
		String[]splitterText = text.split(":");
		for(int i=0;i<splitterText.length-1;i++){
			Lokasi lokasi = convertTextToSingleLokasi(splitterText[i]);
			if(lokasi!=null){
				lokasis.add(lokasi);	
			}
			
		}
		return lokasis;
	}
	
	/*
	 * format text : time,mLatitude,mLongitude,
	 */
	public final static Lokasi convertTextToSingleLokasi(String text){
		try{
			String[]splitText = text.split(",");
			Lokasi lokasi = new Lokasi();
			lokasi.setLatitude(Double.parseDouble(splitText[1]));
			lokasi.setLongitude(Double.parseDouble(splitText[2]));
			lokasi.setTime(Long.parseLong(splitText[0]));	
			return lokasi;
		}catch(ArrayIndexOutOfBoundsException exception){
			return null;
		}
		
	}
	
	public final static String convertLokasisToText(List<Lokasi> lokasis){
		String text = "";
		for(Lokasi lokasi:lokasis){
			text = text + lokasi.toString()+",:";
		}
		return text;
	}
}
