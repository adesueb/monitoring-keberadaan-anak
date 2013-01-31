package org.ade.monak.ortu.map.view;

import org.ade.monak.ortu.entity.Anak;
import org.ade.monak.ortu.service.gate.monak.SenderRequestLogMonak;

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
