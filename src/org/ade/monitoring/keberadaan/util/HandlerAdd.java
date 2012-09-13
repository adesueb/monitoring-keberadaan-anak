package org.ade.monitoring.keberadaan.util;

import org.ade.monitoring.keberadaan.Variable.Status;

import android.os.Handler;
import android.os.Message;

public class HandlerAdd extends Handler{
	public HandlerAdd(IFormOperation formOperation){
		mFormOperation = formOperation;
	}
	@Override
	public void handleMessage(Message msg) {
		switch(msg.what){
			case Status.SUCCESS:{
				mFormOperation.add();
			}
		}
	}
	private IFormOperation mFormOperation;
}
