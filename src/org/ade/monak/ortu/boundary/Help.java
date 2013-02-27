package org.ade.monak.ortu.boundary;

import org.ade.monak.ortu.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class Help extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help);
		
		image[0] = R.drawable.help1;
		image[1] = R.drawable.help2;
		image[2] = R.drawable.help3;
		
		final ImageView imageHelp = (ImageView) findViewById(R.id.imageHelp);
		
		Button next = (Button) findViewById(R.id.buttonNext);
		next.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				if(i==2){
					i=0;
				}else{
					i++;
				}
				imageHelp.setImageResource(image[i]);
			}
		});
		
		
		Button back = (Button) findViewById(R.id.buttonBack);
		back.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				if(i==0){
					i=2;
				}else{
					i--;
				}
				imageHelp.setImageResource(image[i]);
			}
		});
	}
	
	private int i = 0;

	private int[] image = new int[3];
}
