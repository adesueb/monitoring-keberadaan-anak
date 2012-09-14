package org.ade.monitoring.keberadaan.util;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class HandlerEdit extends Handler{
	public HandlerEdit(IFormOperation formOperation){
		mFormOperation = formOperation;
	}
	@Override
	public void handleMessage(Message msg) {
		Bundle bundle = msg.getData();
		if(bundle!=null){
			mFormOperation.edit(bundle);
		}
	}
	private IFormOperation mFormOperation;
}

