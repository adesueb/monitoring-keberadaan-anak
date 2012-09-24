package org.ade.monitoring.keberadaan.lokasi;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.provider.Settings;

public class GpsSwitchManager {
	
	public GpsSwitchManager(Context context){
		mContext = context;
	}

	public void turnGPSOn(){
	    String provider = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

	    if(!provider.contains("gps")){ //if gps is disabled
	        final Intent poke = new Intent();
	        poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider"); 
	        poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
	        poke.setData(Uri.parse("3")); 
	        mContext.sendBroadcast(poke);
	    }
	}

	public void turnGPSOff(){
		String provider = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
	    
	    if(provider.contains("gps")){ //if gps is enabled
	        final Intent poke = new Intent();
	        poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
	        poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
	        poke.setData(Uri.parse("3")); 
	        mContext.sendBroadcast(poke);
	    }
	}

	public boolean canToggleGPS() {
	    PackageManager pacman = mContext.getPackageManager();
	    PackageInfo pacInfo = null;

	    try {
	        pacInfo = pacman.getPackageInfo("com.android.settings", PackageManager.GET_RECEIVERS);
	    } catch (NameNotFoundException e) {
	        return false; //package not found
	    }

	    if(pacInfo != null){
	        for(ActivityInfo actInfo : pacInfo.receivers){
	            //test if recevier is exported. if so, we can toggle GPS.
	            if(actInfo.name.equals("com.android.settings.widget.SettingsAppWidgetProvider") && actInfo.exported){
	                return true;
	            }
	        }
	    }

	    return false; //default
	}
	
	private final Context mContext;
	
}
