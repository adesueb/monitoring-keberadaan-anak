package org.ade.monitoring.keberadaan.map.view;

import java.util.List;

import org.ade.monitoring.keberadaan.Variable.VariableEntity;
import org.ade.monitoring.keberadaan.entity.Anak;
import org.ade.monitoring.keberadaan.entity.Lokasi;
import org.ade.monitoring.keberadaan.service.BinderHandlerMonak;
import org.ade.monitoring.keberadaan.service.gate.monak.SenderRequestLogMonak;
import org.ade.monitoring.keberadaan.util.LokasisConverter;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class PetaLogController {

	public PetaLogController(Peta peta){
		this.peta = peta;
		this.sender = new SenderRequestLogMonak(peta);
	}
	
	public void refreshLogController(List<Integer> pilihanOverlay, List<Anak> anaks){
		
		for(int i:pilihanOverlay){
			Anak anak = anaks.get(i);
			BinderHandlerMonak handlerBinder = peta.getBinderHandler();
			if(handlerBinder!=null){
				handlerBinder.bindUIHandlerWaitingLocation(new HandlerPetaWaitingLog(peta));
			}
			sender.sendRequest(anak);
  		}
	}
	
	private final Peta peta;
	
	private final SenderRequestLogMonak sender;
	
	private static class HandlerPetaWaitingLog extends Handler{

		public HandlerPetaWaitingLog(Peta peta){
			this.peta = peta;
		}
		
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Bundle bundle 	= msg.getData();
			String text 	= bundle.getString("textLog");
			String idAnak	= bundle.getString("idAnak");
			List<Lokasi> lokasis = LokasisConverter.covertTextToLokasis(text);
			peta.receiveLogFromAnak(idAnak, lokasis);
		}
		
		private final Peta peta;
	}
}
