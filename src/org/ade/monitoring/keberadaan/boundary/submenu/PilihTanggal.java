package org.ade.monitoring.keberadaan.boundary.submenu;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;

public class PilihTanggal extends DatePickerDialog{

	public PilihTanggal(Context context) {
		super(context, datePickerListener,2012,8,12);
//		case TampilanLaporanHarian.DATE_PERTAMA:{
//		this.status = TampilanLaporanHarian.DATE_PERTAMA;
//		return new DatePickerDialog(this,this.datePickerListener,tahun1,bulan1,tanggal1);
//	}case TampilanLaporanHarian.DATE_KEDUA:{
//		this.status = TampilanLaporanHarian.DATE_KEDUA;
//		return new DatePickerDialog(this,this.datePickerListener,tahun2,bulan2,tanggal2);
//		
//	}
	}
	
	private static DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
		
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			
//			String dateSelected = dayOfMonth+"-"+ARRMONTH[monthOfYear]+"-"+year;
//					
//			switch(status){
//				case TampilanLaporanHarian.DATE_PERTAMA:{
//					TampilanLaporanHarian.this.setKalenderPertama(dayOfMonth, monthOfYear, year);
//					TampilanLaporanHarian.this.tanggalPertama = dateSelected;
//					TampilanLaporanHarian.this.editTanggalPertama.setText(dateSelected);
//					break;
//				}case TampilanLaporanHarian.DATE_KEDUA:{
//					TampilanLaporanHarian.this.setKalenderPertama(dayOfMonth, monthOfYear, year);
//					TampilanLaporanHarian.this.tanggalKedua = dateSelected;
//					TampilanLaporanHarian.this.editTanggalKedua.setText(dateSelected);
//					break;
//				}
//			}
//			
//			Toast.makeText(TampilanLaporanHarian.this, "Selected Date is ="
//			+dateSelected, Toast.LENGTH_SHORT).show();
		}
	};

}
