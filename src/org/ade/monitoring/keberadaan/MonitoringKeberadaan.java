package org.ade.monitoring.keberadaan;

import org.ade.monitoring.keberadaan.boundary.DaftarAnak;
import org.ade.monitoring.keberadaan.boundary.DaftarMonitoring;
import org.ade.monitoring.keberadaan.service.BackgroundService;

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
        
        ImageView menuDaftarAnak 		= (ImageView) findViewById(R.id.menuDaftarAnak);
        menuDaftarAnak.setOnClickListener(this);
        
        ImageView menuDaftarMonitoring 	= (ImageView) findViewById(R.id.menuDaftarMonitor);
        menuDaftarMonitoring.setOnClickListener(this);
        
        ImageView menuReport 			= (ImageView) findViewById(R.id.menuReport);
        menuReport.setOnClickListener(this);
        
        ImageView menuHelp 				= (ImageView) findViewById(R.id.menuHelp);
        menuHelp.setOnClickListener(this);
        
        startService(new Intent(this,BackgroundService.class));
        Log.d("Monitoring keberadaan", "starting service");
    }
    
    private static void goToDaftarAnak(Context context){
        Intent intent = new Intent(context, DaftarAnak.class);
        context.startActivity(intent);
    }
    
    private static void goToDaftarMonitoring(Context context){
        Intent intent = new Intent(context, DaftarMonitoring.class);
        context.startActivity(intent);
    }
    
    private static void goToReport(Context context){
        // TODO : to report activity....
    }
    
    private static void goToHelp(Context context){
    	// TODO : to help activity......
    }

	public void onClick(View v) {
		switch(v.getId()){
			case R.id.menuDaftarAnak:{
				goToDaftarAnak(this);
				break;
			}case R.id.menuDaftarMonitor:{
				goToDaftarMonitoring(this);
				break;
			}case R.id.menuReport:{
				goToReport(this);
				break;
			}case R.id.menuHelp:{
				goToHelp(this);
				break;
			}
		}
	}
    
}