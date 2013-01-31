package org.ade.monak.ortu.service.gate;

import org.ade.monak.ortu.Variable.Status;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.telephony.SmsManager;

public class SMSSent extends BroadcastReceiver{

	public SMSSent(Handler handler){
		mHandler = handler;
	}
	@Override
    public void onReceive(Context arg0, Intent arg1) {
        switch (getResultCode()){
            case Activity.RESULT_OK:
            	break;
            case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
            	if(mHandler!=null){
            		mHandler.sendEmptyMessage(Status.FAILED);	
            	}
            	break;
            case SmsManager.RESULT_ERROR_NO_SERVICE:
            	if(mHandler!=null){
            		mHandler.sendEmptyMessage(Status.FAILED);	
            	}
            	break;
            case SmsManager.RESULT_ERROR_NULL_PDU:
            	if(mHandler!=null){
            		mHandler.sendEmptyMessage(Status.FAILED);	
            	}
            	break;
            case SmsManager.RESULT_ERROR_RADIO_OFF:
            	if(mHandler!=null){
            		mHandler.sendEmptyMessage(Status.FAILED);	
            	}
            	break;
        }
	}
	
	private final Handler mHandler;
	
}
