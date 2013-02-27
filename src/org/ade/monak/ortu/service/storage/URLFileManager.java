package org.ade.monak.ortu.service.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.os.Environment;

public class URLFileManager {
	private final static File createFile(){
		String dir = Environment.getExternalStorageDirectory().getAbsolutePath();
		File file = new File(dir+PATH);
		return file;
	}
	
	private final static FileInputStream openReadFileLog(){
		try {
			File file = createFile();
			FileInputStream fis = new FileInputStream(file);
			return fis;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public final static String getIp(){
		FileInputStream fis = openReadFileLog();
		String ip ="";
		if(fis!=null){
			
			int read;
			try {
				while((read= fis.read())>-1){
					ip+=(char)read;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return ip;
	}
	
	

	private final static String PATH = "/MonakUrl.txt";
}
