package org.ade.monitoring.keberadaan.service.koneksi;

import org.ade.monitoring.keberadaan.entity.Anak;
import org.ade.monitoring.keberadaan.entity.IPesanData;

public interface ISender {
	
	public void kirimPesanData(IPesanData pesanData);
	public void kirimRequestLokasiAnak(Anak anak);

}
