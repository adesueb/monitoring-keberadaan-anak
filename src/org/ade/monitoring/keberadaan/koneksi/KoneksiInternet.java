package org.ade.monitoring.keberadaan.koneksi;


import org.ade.monitoring.keberadaan.entity.PesanData;


/**
 * Class KoneksiInternet
 */
public abstract class KoneksiInternet implements Koneksi {

   	public void kirimPesanData( PesanData pesanData ){
	  
  	}

	public abstract void menerimaPesanData(PesanData pesaData);





}
