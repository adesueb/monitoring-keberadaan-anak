package org.ade.monitoring.keberadaan.koneksi;


import org.ade.monitoring.keberadaan.entity.IPesanData;

import android.content.Context;

public interface ReceiverKoneksi {

	public void menerimaPesanData( Context context, IPesanData pesaData );


}
