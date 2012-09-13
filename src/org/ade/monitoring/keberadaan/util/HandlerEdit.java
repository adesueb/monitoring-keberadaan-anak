package org.ade.monitoring.keberadaan.util;

import org.ade.monitoring.keberadaan.Variable.Status;

import android.os.Handler;
import android.os.Message;

public class HandlerEdit extends Handler{
	public HandlerEdit(IFormOperation formOperation){
		mFormOperation = formOperation;
	}
	@Override
	public void handleMessage(Message msg) {
		switch(msg.what){
			case Status.SUCCESS:{
				mFormOperation.edit();
			}
		}
	}
	private IFormOperation mFormOperation;
}

