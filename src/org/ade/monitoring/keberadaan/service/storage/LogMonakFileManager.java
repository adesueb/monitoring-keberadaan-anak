package org.ade.monitoring.keberadaan.service.storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.os.Environment;

public class LogMonakFileManager {

	public static final void debug(String debug){
		FileOutputStream fos = openWriteFileLog();
		if(fos==null||debug==null){
			return;
		}else{
			String text = debug+",:"; 
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
	
	private static final File createFile(){
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
	
	public final static void clearLog(){
		File file = createFile();
		file.delete();
	}
	
	private final static String PATH = "/MonakLog.txt";

}
