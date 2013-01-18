package org.ade.monitoring.keberadaan.map.view;

import java.util.List;

import org.ade.monitoring.keberadaan.entity.Anak;
import org.ade.monitoring.keberadaan.service.gate.monak.SenderTrackingMode;

public class PetaTrackingController {
	public PetaTrackingController(Peta peta){
		this.sender = new SenderTrackingMode(peta);
	}
	public void refreshTrackingController(List<Integer> pilihanOverlay, List<Anak> anaks){
		for(int i:pilihanOverlay){
			Anak anak = anaks.get(i);
			
			senderRequestTrackAnak(anak);
  		}
	}
	
	public void senderRequestTrackAnak(Anak anak){
		if(anak!=null){
			sender.sendStartTracking(anak);	
		}
	}
	private final SenderTrackingMode sender;
	
}
