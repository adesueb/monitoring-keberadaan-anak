package org.ade.monitoring.keberadaan.boundary.submenu;

import org.ade.monitoring.keberadaan.R;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public abstract class MultipleChoice extends Dialog{
	
	public MultipleChoice(Activity activity, Bundle bundle, boolean withDetail){
		super(activity);
		this.activity = activity;
		this.bundle = bundle;
		this.withDetail = withDetail;
	}
	
	public void hideDetail(){
		withDetail = false;
	}
	
	public Bundle getBundle(){
		return this.bundle;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.multiple_choice);
		Button buttonDetail = (Button) findViewById(R.id.multipleDetail);
		buttonDetail.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				onDetail(bundle);
			}
		});Button buttonEdit 	= (Button) findViewById(R.id.multipleEdit);
		buttonEdit.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				onEdit(bundle);
			}
		});
		Button buttonDelete = (Button) findViewById(R.id.multipleDelete);
		buttonDelete.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				onDelete(bundle);
			}
		});
		
		if(!withDetail){
			buttonDetail.setVisibility(View.INVISIBLE);
		}
	}
	
	protected Activity getActivity(){
		return this.activity;
	}
	
	protected abstract void onDelete(Bundle bundle);
	protected abstract void onEdit(Bundle bundle);
	protected abstract void onDetail(Bundle bundle);
	
	private final Activity activity;
	private final Bundle bundle;
	private boolean withDetail = true;

}
