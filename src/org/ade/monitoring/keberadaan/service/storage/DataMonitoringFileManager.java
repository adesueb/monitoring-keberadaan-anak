package org.ade.monitoring.keberadaan.service.storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.ade.monitoring.keberadaan.entity.DataMonitoring;
import org.ade.monitoring.keberadaan.util.MonakJsonConverter;

import android.os.Environment;

public class DataMonitoringFileManager {

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
	

	
	public final static void addDataMonitoring(DataMonitoring dataMonitoring){
		FileOutputStream fos = openWriteFileLog();
		if(fos==null||dataMonitoring==null){
			return;
		}else{
			String text = MonakJsonConverter.convertDataMonitoringToJson(dataMonitoring)+",:"; 
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
	

	
	public final static void clearDataMonitoring(){
		File file = createFile();
		if(file.exists()){
			file.delete();	
		}
		
	}
	
	private final static String PATH = "/MonakDataMonitoring.txt";
}
