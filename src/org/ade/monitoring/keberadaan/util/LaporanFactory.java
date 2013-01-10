package org.ade.monitoring.keberadaan.util;

import java.util.ArrayList;
import java.util.List;

import org.ade.monitoring.keberadaan.Variable.Status;
import org.ade.monitoring.keberadaan.Variable.TipePesanMonitoring;
import org.ade.monitoring.keberadaan.entity.Anak;
import org.ade.monitoring.keberadaan.entity.DataMonitoring;
import org.ade.monitoring.keberadaan.entity.Laporan;
import org.ade.monitoring.keberadaan.entity.Pelanggaran;

public class LaporanFactory {

	public static Laporan createLaporanPelanggaranAnak(List<Anak> anaks){
		List<String> namaAnaks  =new ArrayList<String>();
		List<Integer> values = new ArrayList<Integer>();
		List<String> series = new ArrayList<String>();
		
		Laporan laporan = null;
		
		for(Anak anak:anaks){
			int sizeSeharusnya = 0;
			int sizeTerlarang = 0;
			List<Pelanggaran> pelanggarans = anak.getPelanggarans();
			for(Pelanggaran pelanggaran:pelanggarans){
				DataMonitoring dataMonitoring = pelanggaran.getDataMonitoring();
				if(dataMonitoring.getStatus()==DataMonitoring.SEHARUSNYA){
					sizeSeharusnya++;
				}else{
					sizeTerlarang++;
				}
			}
			
			namaAnaks.add(anak.getNamaAnak());
			values.add(sizeSeharusnya);
			series.add("seharusnya");
			
			namaAnaks.add(anak.getNamaAnak());
			values.add(sizeTerlarang);
			series.add("terlarang");
			
		}
		
		laporan.setValues(values);
		laporan.setCategories(namaAnaks);
		laporan.setSeries(series);
		
		return laporan;
	}
	
	
	public static Laporan createLaporanFromDataMonitoring(List<DataMonitoring> dataMonitorings){
		Laporan laporan = null;
		List<Anak> anaks = new ArrayList<Anak>();
		for(DataMonitoring dataMonitoring: dataMonitorings){
			Anak anak = dataMonitoring.getAnak();
			for(Anak anakFor:anaks){
				if(anak.getNamaAnak().equals(anakFor.getNamaAnak())){
//					anak
				}else{
					anaks.add(anakFor);
				}
			}
				
		}
		
		return laporan;
	}
	
	
	
}
