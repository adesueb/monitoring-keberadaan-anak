package org.ade.monak.ortu.util;

import org.ade.monak.ortu.entity.IEntity;

import android.os.Handler;

public class StorageHandler extends Handler{

	public StorageHandler(IEntity entity){
		this.entity = entity;
	}
	public void setEntity(IEntity entity){
		this.entity = entity;
	}
	public String getIdEntity(){
		return entity.getIdEntity();
	}
	private IEntity entity;

}
