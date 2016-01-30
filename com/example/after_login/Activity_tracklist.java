package com.example.after_login;


import java.util.ArrayList;


import com.example.contacts.Class_give_phones;
import com.example.project_practise.R;
import com.example.project_practise.Response;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class Activity_tracklist extends Activity {

	Button var_button_save;
	ListView var_listview;
	Adapter_tracklist adapter;
	ProgressBar progressbar;
	Handler handler;
	String[] phones;
	String var_phone;
	String var_username;
	ContentResolver resolver;
	ArrayList<Class_tracklist_object> list=new ArrayList<Class_tracklist_object>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent var_intent_recieved=getIntent();
		if(var_intent_recieved==null)
		{
			Toast.makeText(getApplicationContext(), "Illeagal opening of activity",Toast.LENGTH_SHORT).show();
			finish();
		}
		else
		{
			Bundle bundle;
			bundle=var_intent_recieved.getExtras();
			var_phone=bundle.getString("phone");
			var_username=bundle.getString("username");
			setContentView(R.layout.activity_tracklist);
			adapter=new Adapter_tracklist();
			var_listview=(ListView)findViewById(R.id.listview_activity_tracklist);
			var_listview.setAdapter(adapter);
			progressbar=(ProgressBar)findViewById(R.id.progressbar_activity_tracklist);
			
			resolver=getContentResolver();
			handler=new Handler();
			
			var_button_save=(Button)findViewById(R.id.button_activity_tracklist_save);
			var_button_save.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					save_all();
				}
			});
			
			
			var_listview.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int index, long id) {
					
					Class_tracklist_object object=adapter.arraylist.get(index);
					//Toast.makeText(getApplicationContext(), "clicked", Toast.LENGTH_SHORT).show();
					if(object.check==true)
					{
						object.check=false;
						CheckBox cb=(CheckBox)view.findViewById(R.id.checkbox_layout_tracklist);
						
						cb.setChecked(false);
					}
					else
					{
						object.check=true;
						CheckBox cb=(CheckBox)view.findViewById(R.id.checkbox_layout_tracklist);
						cb.setChecked(true);
					}
					adapter.notifyDataSetChanged();
				}
				
			});
		}
		
	
	}
	private void save_all()
	{
		progressbar.setVisibility(View.VISIBLE);
		ArrayList<String> list=new ArrayList<String>();
		ArrayList<String> blocklist=new ArrayList<String>();
		int len=adapter.arraylist.size();
		for(int i=0;i<len;i++)
		{
			if(adapter.arraylist.get(i).check==false)
			{
				list.add(adapter.arraylist.get(i).name);
				blocklist.add("1");
			}
			else
			{
				list.add(adapter.arraylist.get(i).name);
				blocklist.add("0");
			}
		}
		final ArrayList<String>phone_list=list;
		final ArrayList<String>block_list=blocklist;
		Thread t=new Thread(new Runnable() {
			Response result;
			@Override
			public void run() {
				result=Class_save_tracklist.save(phone_list,block_list,var_phone);
				handler.post(new Runnable() {
					
					@Override
					public void run() {
						Toast.makeText(getApplicationContext(), result.message, Toast.LENGTH_SHORT).show();
						progressbar.setVisibility(View.INVISIBLE);
					}
				});
			}
		});
		t.start();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_tracklist, menu);
		return true;
	}
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		if(item.getItemId()==R.id.settings_activity_tracklist_refresh)
		{
			refresh();
		}
		return super.onMenuItemSelected(featureId, item);
	}
	@Override
	protected void onStart() {
		super.onStart();
		progressbar.setVisibility(View.VISIBLE);
		Thread t=new Thread(new Runnable() {
			
			@Override
			public void run() {
				load_contacts();
				refresh();
			}
		});
		t.start();
		
		
	}
	private void load_contacts()
	{
		
		phones=Class_give_phones.give(resolver);
		
	}
	private void refresh()
	{
		Thread t=new Thread(new Runnable() {
			
			@Override
			public void run() {
				load_list();
			}
		});
		t.start();
	}
	private void load_list()
	{
		handler.post(new Runnable() {
			
			@Override
			public void run() {
				progressbar.setVisibility(View.VISIBLE);
			}
		});
		
		final Response result=Class_load_tracklist.load(list,phones,var_phone,resolver);
		
		handler.post(new Runnable() {
			
			@Override
			public void run() {
				
				adapter.notifyDataSetChanged();
				
				if(result.bool)
				{
					adapter.arraylist.clear();
					int length=list.size();
					for(int i=0;i<length;i++)
					{
						adapter.arraylist.add(list.get(i));
					}
					Toast.makeText(getApplicationContext(),result.message, Toast.LENGTH_SHORT).show();
				}
				else
				{
					Toast.makeText(getApplicationContext(),result.message, Toast.LENGTH_SHORT).show();
				}
				progressbar.setVisibility(View.INVISIBLE);
			}
		});
	}
}
