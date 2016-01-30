package com.example.tracking;

import com.example.alert_dialogs.Alert_ok;
import com.example.friends.Class_check_tracking;
import com.example.groups.Class_locations;
import com.example.project_practise.R;
import com.example.project_practise.Response;


import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

public class Activity_tracking_friend extends Activity {

	Bundle bundle;
	String var_friend_name;
	String var_friend_phone;
	String var_username;
	String var_phone;
	Class_locations location;
	TextView textview;
	Context context;
	boolean destroyed;
	Handler handler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tracking_friend);
		Intent var_intent_received=getIntent();
		 if(var_intent_received==null||var_intent_received.getExtras()==null)
		 {
			 Toast.makeText(getApplicationContext(), "Illeagal opening of activity",Toast.LENGTH_SHORT).show();
			 finish();
		 }
		 else
		 {
			 bundle=var_intent_received.getExtras();
			 var_username=bundle.getString("username");
			 var_phone=bundle.getString("phone");
			 var_friend_name=bundle.getString("friend_name");
			 var_friend_phone=bundle.getString("friend_phone");
			 location=new Class_locations();
			 context=this;
			 
			 handler=new Handler();
			 textview=(TextView)findViewById(R.id.textview_activity_tracking_friend);
			 textview.setText(var_username+" "+var_phone+"is tracking "+var_friend_name+" "+var_friend_phone);
		 }
		 Thread t=new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				while(!destroyed)
				 {
					 refresh();
					 
					 try {
						Thread.sleep(10000);
					} catch (final InterruptedException e) {
						handler.post(new Runnable() {
							
							@Override
							public void run() {
								Alert_ok.show(context,"Interrupeed exception occured in thread\n"+e.getMessage());
							}
						});
						
					}
				 }
			}
		});
		 t.start();
		 
	}
	@Override
	protected void onDestroy() {
		
		super.onDestroy();
		destroyed=true;
	}
	@Override
	protected void onStop() {
		destroyed=true;
		super.onStop();
	}
	@Override
	protected void onStart() {
		destroyed=false;
		super.onStart();
	}
	private void refresh()
	{
		
		Thread t=new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				final Response result=Class_check_tracking.check(var_phone, var_friend_phone,var_friend_name, location);
				handler.post(new Runnable() {
					
					@Override
					public void run() {
						if(result.bool==true)
						{
							Toast.makeText(context, "Updated now....",Toast.LENGTH_SHORT).show();
							textview.setText(var_friend_name+" is at \nlatitude:"+location.latitude+"\nlongitude:"+location.longitude+"\nlast updated at:"+location.last_updated);
						}
						else
						{
							if(result.value==0)
							{
								Alert_ok.show(context, result.message);
							}
							else if(result.value==-1)
							{
								Alert_ok.show(context, result.message);
								finish();
							}
							
						}
					}
				});
				
			}
		});
		t.start();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_tracking_friend, menu);
		return false;
	}

}
