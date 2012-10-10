package org.ade.monitoring.keberadaan.koneksi;


import org.ade.monitoring.keberadaan.entity.PesanData;

import android.content.Context;

public interface ReceiverKoneksi {

	public void menerimaPesanData( Context context, PesanData pesaData );


}
