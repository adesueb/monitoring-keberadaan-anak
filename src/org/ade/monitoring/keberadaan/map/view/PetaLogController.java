package org.ade.monitoring.keberadaan.map.view;

import org.ade.monitoring.keberadaan.entity.Anak;
import org.ade.monitoring.keberadaan.service.gate.monak.SenderRequestLogMonak;

public class PetaLogController {

	public PetaLogController(Peta peta){
		this.sender 		= new SenderRequestLogMonak(peta);
	}
	
	public void action(Anak anak){
		if(anak!=null){
			sender.sendRequest(anak);
		}
		
	}	
	private final SenderRequestLogMonak sender;
}
