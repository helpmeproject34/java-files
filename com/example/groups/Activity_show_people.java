package com.example.groups;

import java.util.ArrayList;

import com.example.alert_dialogs.Alert_ok;
import com.example.project_practise.R;
import com.example.project_practise.Response;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class Activity_show_people extends Activity {

	Handler handler;
	ListView listview;
	Adapter_groups adapter;
	ProgressBar progressbar;
	String var_username;
	String var_phone;
	String var_group_id;
	String var_group_name;
	String var_admin_username;
	String var_admin_phone;
	//Boolean admin_or_not;
	Thread updating_thread;
	TextView textview;
	Context this_context;
	int imp_value;
	boolean activity_visible;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent intent_recieved= getIntent();
		Bundle bundle=null ;
		if(intent_recieved!=null)
		{
			bundle=intent_recieved.getExtras();
		}
		
		if(bundle==null || intent_recieved==null)
		{
			Toast.makeText(getApplicationContext(), "Illeagal opening of activity",Toast.LENGTH_SHORT).show();
			finish();
		}
		else
			
		{
			setContentView(R.layout.activity_show_people);
			imp_value=0;
			activity_visible=true;
			var_username=bundle.getString("username");
			var_phone=bundle.getString("phone");
			var_group_id=bundle.getString("group_id");
			var_group_name=bundle.getString("group_name");
			var_admin_username=bundle.getString("group_admin_username");
			var_admin_phone=bundle.getString("group_admin_phone");
			setTitle(var_group_name+" on Map");
			progressbar=(ProgressBar)findViewById(R.id.progressbar_show_group_people);
			textview=(TextView)findViewById(R.id.textview_show_people);
			progressbar.setVisibility(View.INVISIBLE);
			this_context=this;
			handler=new Handler();
			
			start_refresh_thread(10000);
			
		}
	}
	@Override
	protected void onStart() {
		
		activity_visible=true;
		super.onStart();
		
	}
	@Override
	protected void onResume() {

		activity_visible=true;
		super.onResume();
		
	}
	@Override
	protected void onPause() {
		activity_visible=false;
		super.onPause();
	}
	private void finish_all()
	{
		if(activity_visible)
		{
			//finish();
			Alert_ok alert=new Alert_ok()
			{
				@Override
				public void onfalse() {
					
					super.onfalse();
					finish();
				}
				@Override
				public void ontrue() {
					// TODO Auto-generated method stub
					super.ontrue();
					finish();
				}
			};
			alert.ok_or_cancel(this_context, "","This group no longer exist");
		}
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_show_people, menu);
		
		return true;
	}
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		if(item.getItemId()==R.id.settings_show_people)
		{
			Intent intent=new Intent(getApplicationContext(),Activity_group_people.class);
			Bundle bundle=new Bundle();
			bundle.putString("username", var_username);
			bundle.putString("phone", var_phone);
			bundle.putString("group_name",var_group_name);
			bundle.putString("group_id",var_group_id);
			bundle.putString("group_admin_username",var_admin_username);
			bundle.putString("group_admin_phone",var_admin_phone);
			intent.putExtras(bundle);
			startActivity(intent);
		}
		else if(item.getItemId()==R.id.settings_group_exit)
		{
			Thread t=new Thread(new Runnable() {
				
				@Override
				public void run() {
					exit_group();
				}
			});
			t.start();
		}
		return super.onMenuItemSelected(featureId, item);
	}
	private void exit_group()
	{
		handler.post(new Runnable() {
			
			@Override
			public void run() {
				progressbar.setVisibility(View.VISIBLE);
			}
		});
		Class_exit_group.exit(var_username, var_phone, var_group_id);
		handler.post(new Runnable() {
			
			@Override
			public void run() {
				progressbar.setVisibility(View.INVISIBLE);
				finish();
			}
		});
	}
	private void start_refresh_thread(final int time)
	{
		
		 updating_thread=new Thread(new Runnable() {
		
			@Override
			public void run() {
				
				
				ArrayList<Class_locations> locations=new ArrayList<Class_locations>();
				String groupid=var_group_id;
				String username=var_username;
				String total="";
				while(true)
				{
					locations.clear();
					if(!Activity_show_people.this.isFinishing())
					{
						final Response result=Class_locations_provider.provide_locations(locations,groupid,username);
						total="";
						int length=locations.size();
						for(int j=0;j<length;j++)
						{
							total=total+(locations.get(j)).toString()+"\n";
						}
						final String total2=total;
						handler.post(new Runnable() {
							
							@Override
							public void run() {
								textview.setText(total2);
								Toast.makeText(getApplicationContext(), "people's locations are updated now",Toast.LENGTH_SHORT).show();
								if(result.value==-2)
								{
									imp_value=-2;
									finish_all();
								}
							}
						});
						
						try {
							Thread.sleep(time);
						} catch (InterruptedException e) {
							
						}
					}
					else
					{
						finish();
					}
					
					
				}
				
			}
			
		});
		updating_thread.start();
	}
	
	
	
	

}
