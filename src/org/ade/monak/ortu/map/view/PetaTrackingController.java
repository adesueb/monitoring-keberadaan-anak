package org.ade.monak.ortu.map.view;

import java.util.List;

import org.ade.monak.ortu.entity.Anak;
import org.ade.monak.ortu.service.gate.monak.SenderTrackingMode;

public class PetaTrackingController {
	public PetaTrackingController(Peta peta){
		this.sender = new SenderTrackingMode(peta);
		this.peta	= new Peta();
	}
	public void refreshTrackingController(List<Integer> pilihanOverlay, List<Anak> anaks){
		for(int i:pilihanOverlay){
			Anak anak = anaks.get(i);
			peta.getAnakTracks().add(anak);
			senderRequestTrackAnak(anak);
  		}
	}
	
	public void senderRequestTrackAnak(Anak anak){
		if(anak!=null){
			sender.sendStartTracking(anak);	
		}
	}
	
	
	private final SenderTrackingMode 	sender;
	private final Peta					peta;
	
}
