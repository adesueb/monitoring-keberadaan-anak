package org.ade.monitoring.keberadaan.util;

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
			mFormOperation.add(bundle);
		}
	}
	private IFormOperation mFormOperation;
}
