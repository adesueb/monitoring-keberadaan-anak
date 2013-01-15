package org.ade.monitoring.keberadaan.map.view;

import java.util.List;

import org.ade.monitoring.keberadaan.entity.Anak;
import org.ade.monitoring.keberadaan.entity.Lokasi;
import org.ade.monitoring.keberadaan.service.BinderHandlerMonak;
import org.ade.monitoring.keberadaan.service.gate.monak.SenderTrackingMode;
import org.ade.monitoring.keberadaan.util.LokasisConverter;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class PetaTrackingController {
	public PetaTrackingController(Peta peta){
		this.peta = peta;
		this.sender = new SenderTrackingMode(peta);
	}
	public void refreshTrackingController(List<Integer> pilihanOverlay, List<Anak> anaks){
		for(int i:pilihanOverlay){
			Anak anak = anaks.get(i);
			sender.sendStartTracking(anak);
  		}
	}
	private final Peta peta;
	private final SenderTrackingMode sender;
	
}
