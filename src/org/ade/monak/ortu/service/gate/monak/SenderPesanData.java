package org.ade.monak.ortu.service.gate.monak;

import org.ade.monak.ortu.Variable.Status;
import org.ade.monak.ortu.Variable.TipePesanMonak;
import org.ade.monak.ortu.entity.Anak;
import org.ade.monak.ortu.entity.DataMonitoring;
import org.ade.monak.ortu.entity.IPesanData;
import org.ade.monak.ortu.service.gate.ASenderMonak;
import org.ade.monak.ortu.util.IDGenerator;

import android.content.Context;
import android.os.Handler;

public class SenderPesanData extends ASenderMonak{
	
	public SenderPesanData(Context context, Handler handler){
		super(context);
		this.context	= context;
		this.handler	= handler;
	}
	public void sendDataMonitoringBaru(DataMonitoring dataMonitoring){
		dataMonitoring.setTipe(TipePesanMonak.DATAMONITORING_BARU);
		pesanData 		= dataMonitoring;
		kirimPesanData(pesanData);
	}
	
	public void sendDataMonitoringUpdate(DataMonitoring dataMonitoring){
		dataMonitoring.setTipe(TipePesanMonak.DATAMONITORING_UPDATE);
		pesanData 		= dataMonitoring;
		kirimPesanData(pesanData);
	}
	
	public void sendDataMonitoringDelete(DataMonitoring dataMonitoring){
		dataMonitoring.setTipe(TipePesanMonak.DATAMONITORING_DELETE);
		pesanData 		= dataMonitoring;
		kirimPesanData(pesanData);
	}
	
	
	private void kirimPesanData( IPesanData pesanData ){
		Anak anak = null;
		String pesan = "";
		
		switch(pesanData.getTipe()){
			case TipePesanMonak.DATAMONITORING_BARU:{
			}case TipePesanMonak.DATAMONITORING_UPDATE:{
			}case TipePesanMonak.DATAMONITORING_DELETE:{
				DataMonitoring dataMonitoring = (DataMonitoring) pesanData;
				anak	= dataMonitoring.getAnak();
				IDGenerator idGenerator = new IDGenerator(context, null);
				pesan = pesanData.getTipe()+","+pesanData.getJsonPesanData()+","+idGenerator.getIdOrangTua();
				break;
			}
		}
		kirimPesan(anak, pesan);
	}
	
	
	public void onSuccess(int tipeKoneksi){
		if(handler==null) return;
		handler.sendEmptyMessage(Status.SUCCESS);	
		
	}
	
	public void onFailed(int tipeKoneksi){
		if(handler==null) return;
		handler.sendEmptyMessage(Status.FAILED);
		
	}
	
	private IPesanData 				pesanData;
	private final Handler			handler;
	private final Context			context;

}
