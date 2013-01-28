package org.ade.monitoring.keberadaan;

import org.ade.monitoring.keberadaan.boundary.AdminActivity;
import org.ade.monitoring.keberadaan.boundary.DaftarAnak;
import org.ade.monitoring.keberadaan.map.view.Peta;
import org.ade.monitoring.keberadaan.service.MonakService;
import org.ade.monitoring.keberadaan.service.storage.DatabaseManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class MonitoringKeberadaan extends Activity implements View.OnClickListener{
	 /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_utama);
                
        ImageView menuDaftarAnak 	= (ImageView) findViewById(R.id.menuDaftarAnak);
        menuDaftarAnak.setOnClickListener(this);
        
        ImageView menuMap 			= (ImageView) findViewById(R.id.menuMap);
        menuMap.setOnClickListener(this);
        
        ImageView menuHelp 			= (ImageView) findViewById(R.id.menuHelp);
        menuHelp.setOnClickListener(this);
        
        ImageView menuSetting		= (ImageView) findViewById(R.id.menuSetting);
        menuSetting.setOnClickListener(this);
        startService(new Intent(MonakService.MONAK_SERVICE));
        Log.d("Monitoring keberadaan", "starting service");
    }
    
    private static void goToDaftarAnak(Context context){
        Intent intent = new Intent(context, DaftarAnak.class);
        context.startActivity(intent);
    }
    
    private static void goToMap(Context context){
    	Intent intent = new Intent(context, Peta.class);
    	context.startActivity(intent);
    }
    
    private static void goToHelp(Context context){
    	
    }
    
    private static void goToSetting(Context context){
    	Intent intent = new Intent(context, AdminActivity.class);
    	context.startActivity(intent);
    }

	public void onClick(View v) {
		switch(v.getId()){
			case R.id.menuDaftarAnak:{
				goToDaftarAnak(this);
				break;
			}case R.id.menuMap:{
				goToMap(this);
				break;
			}case R.id.menuHelp:{
				goToHelp(this);
				break;
			}case R.id.menuSetting:{
				goToSetting(this);
				break;
			}
		}
	}
    
}