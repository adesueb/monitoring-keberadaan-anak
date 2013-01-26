package org.ade.monitoring.keberadaan.service.storage;

import java.io.File;
//import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;

import org.ade.monitoring.keberadaan.entity.Lokasi;

import android.os.Environment;

public class LocationFileManager {

	private final static File createFile(){
		String dir = Environment.getExternalStorageDirectory().getAbsolutePath();
		File file = new File(dir+PATH);
		return file;
	}
	
	private final static FileOutputStream openWriteFileLog(){
		try {
			File file = createFile();
			FileOutputStream fos = new FileOutputStream(file,true);
			return fos;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
	
//	private FileInputStream openReadFileLog(){
//		
//		try {
//			FileInputStream fis = new FileInputStream(file);
//			return fis;
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//			return null;
//		}
//		
//	}
	
	public final static void addLocation(Lokasi lokasi){
		FileOutputStream fos = openWriteFileLog();
		if(fos==null||lokasi==null){
			return;
		}else{
			String text = lokasi.toString()+",:"; 
			try {
				fos.write(text.getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try {
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
//	public List<Lokasi> getAllLokasiFromLog(){
//		List<Lokasi> lokasis = new ArrayList<Lokasi>();
//		FileInputStream fis = openReadFileLog();
//		int karakterInteger = 0;
//		try {
//			String text = "";
//			int counterText = 0;
//			Lokasi lokasi = new Lokasi();
//			while((karakterInteger = fis.read())!=-1){
//				char karakter = (char) karakterInteger;
//				if(karakter==':'){
//					counterText = 0;
//					text = "";
//					lokasis.add(lokasi);
//					lokasi = new Lokasi();
//				}else if(karakter==','){
//					switch (counterText){
//						case 0:{
//							lokasi.setTime(Long.parseLong(text));		
//							break;
//						}case 1:{
//							lokasi.setLatitude(Long.parseLong(text));
//							break;
//						}case 2:{
//							lokasi.setLongitude(Long.parseLong(text));
//							break;
//						}
//					}
//					counterText ++;
//					text = "";
//				}else{
//					text = text+karakter;
//				}
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//		try {
//			fis.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//		return lokasis;
//	}
	
	public final static void clearLocation(){
		File file = createFile();
		if(file.exists()){
			file.delete();	
		}
		
	}
	
	private final static String PATH = "/MonaklocationTracking.txt";
}
