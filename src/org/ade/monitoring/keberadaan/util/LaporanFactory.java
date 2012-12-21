package org.ade.monitoring.keberadaan.util;

import java.util.ArrayList;
import java.util.List;

import org.ade.monitoring.keberadaan.Variable.Status;
import org.ade.monitoring.keberadaan.Variable.TipePesanData;
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
			namaAnaks.add(anak.getNamaAnak());
			values.add(anak.getPelanggarans().size());
			series.add("");
		}
		laporan.setValues(values);
		laporan.setCategories(namaAnaks);
		laporan.setSeries(null);
		
		return laporan;
	}
	
	public static Laporan createLaporanPelanggaranSeharusnya(List<Anak> anaks){
		List<String> namaAnaks  =new ArrayList<String>();
		List<Integer> values = new ArrayList<Integer>();
		List<String> series = new ArrayList<String>();
		
		Laporan laporan = null;
		
		for(Anak anak:anaks){
			namaAnaks.add(anak.getNamaAnak());
			values.add(anak.getPelanggarans().size());
			series.add("");
		}
		laporan.setValues(values);
		laporan.setCategories(namaAnaks);
		laporan.setSeries(null);
		
		
		return laporan;
	}
	
	public static Laporan createLaporanFromDataMonitoring(DataMonitoring monitoring){
		Laporan laporan = null;
		
		return laporan;
	}
	
	public static Laporan createLaporanFromPelanggaranTerlarang(Pelanggaran pelanggaran){
		Laporan laporan = null;
		
		return laporan;
	}
	
}
