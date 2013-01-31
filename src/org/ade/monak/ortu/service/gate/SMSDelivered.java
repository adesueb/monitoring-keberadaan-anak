package org.ade.monak.ortu.service.gate;

import org.ade.monak.ortu.Variable.Status;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

public class SMSDelivered extends BroadcastReceiver{
	public SMSDelivered(Handler handler){
		mHandler = handler;
	}
	@Override
    public void onReceive(Context arg0, Intent arg1) {
        switch (getResultCode())
        {
            case Activity.RESULT_OK:
            	if(mHandler!=null){
            		mHandler.sendEmptyMessage(Status.SUCCESS);	
            	}
            	break;
            case Activity.RESULT_CANCELED:
            	if(mHandler!=null){
                	mHandler.sendEmptyMessage(Status.FAILED);	                		
            	}
                break;                        
        }
    }
	private final Handler mHandler;
}
