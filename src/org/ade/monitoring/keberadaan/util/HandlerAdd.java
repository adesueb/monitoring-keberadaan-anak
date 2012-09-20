package org.ade.monitoring.keberadaan.util;

import org.ade.monitoring.keberadaan.Variable.Operation;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class HandlerAdd extends Handler{
	public HandlerAdd(IFormOperation formOperation){
		mFormOperation = formOperation;
	}
	@Override
	public void handleMessage(Message msg) {
		Bundle bundle = msg.getData();
		if(bundle!=null){
			mFormOperation.onSave(bundle, Operation.ADD);
		}
	}
	private IFormOperation mFormOperation;
}
