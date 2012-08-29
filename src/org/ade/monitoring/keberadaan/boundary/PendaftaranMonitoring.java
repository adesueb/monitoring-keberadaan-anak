package org.ade.monitoring.keberadaan.boundary;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

public class PendaftaranMonitoring extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	
	@Override
	protected Dialog onCreateDialog(int id) {
		
		switch(id){
//			case TampilanLaporanHarian.DATE_PERTAMA:{
//				this.status = TampilanLaporanHarian.DATE_PERTAMA;
//				return new DatePickerDialog(this,this.datePickerListener,tahun1,bulan1,tanggal1);
//			}case TampilanLaporanHarian.DATE_KEDUA:{
//				this.status = TampilanLaporanHarian.DATE_KEDUA;
//				return new DatePickerDialog(this,this.datePickerListener,tahun2,bulan2,tanggal2);
//				
//			}
		}
		return null;
		
	}
	
	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
		
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
