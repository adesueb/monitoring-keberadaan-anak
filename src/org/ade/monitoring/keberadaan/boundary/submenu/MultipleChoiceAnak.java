package org.ade.monitoring.keberadaan.boundary.submenu;

import org.ade.monitoring.keberadaan.R;
import org.ade.monitoring.keberadaan.Variable.Operation;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MultipleChoiceAnak extends Dialog{
	
	public MultipleChoiceAnak(Activity activity, Bundle bundle, boolean withDetail){
		super(activity);
		this.activity = activity;
		this.bundle = bundle;
		this.withDetail = withDetail;
	}
	
	public void hideDetail(){
		withDetail = false;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.multiple_choice);
		Button buttonDetail = (Button) findViewById(R.id.multipleDetail);
		buttonDetail.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				activity.showDialog(Operation.DETAIL, bundle);
			}
		});Button buttonEdit 	= (Button) findViewById(R.id.multipleEdit);
		buttonEdit.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				activity.showDialog(Operation.EDIT, bundle);
			}
		});
		Button buttonDelete = (Button) findViewById(R.id.multipleDelete);
		buttonDelete.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				activity.showDialog(Operation.DELETE, bundle);
			}
		});
		
		if(!withDetail){
			buttonDetail.setVisibility(View.INVISIBLE);
		}
	}
	
	private final Activity activity;
	private final Bundle bundle;
	private boolean withDetail = true;

}
