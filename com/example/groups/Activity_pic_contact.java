package com.example.groups;

import com.example.contacts.Class_give_phones;
import com.example.databases.Db_functions;
import com.example.friends.Class_friend_object;
import com.example.friends.Class_verify_phone_number;
import com.example.project_practise.R;


import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class Activity_pic_contact extends Activity {

	Adapter_groups adapter;
	String var_username;
	String var_phone;
	ProgressBar progressbar;
	Handler handler;
	ListView listview;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pic_contact);
		Intent i=getIntent();
		Bundle bundle;
		if(i!=null)
		{
			bundle=i.getExtras();
			var_username=bundle.getString("username");
			var_phone=bundle.getString("phone");
			if(var_username!=null&&var_phone!=null)
			{
				handler=new Handler();
				adapter=new Adapter_groups();
				progressbar=(ProgressBar)findViewById(R.id.progressbar_pic_contact);
				progressbar.setVisibility(View.INVISIBLE);
				listview=(ListView)findViewById(R.id.listview_pic_contact);
				listview.setAdapter(adapter);
				Thread t=new Thread(new Runnable() {
					
					@Override
					public void run() {
						
						refresh_list();
					}
				});
				t.start();
				
				listview.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View view,
							int arg2, long arg3) {
						TextView name_tv=(TextView)view.findViewById(R.id.textview_layout_group_large);
						TextView phone_tv=(TextView)view.findViewById(R.id.textview_layout_group_medium);
						
						
						Intent i=new Intent(Activity_pic_contact.this.getApplicationContext(),Activity_group_people.class);
						Bundle bundle=new Bundle();
						bundle.putString("name", name_tv.getText().toString());
						bundle.putString("phone", phone_tv.getText().toString());
						i.putExtras(bundle);
						setResult(1,i);
						finish();
					}
				});
			}
			else
			{
				Toast.makeText(this, "illegal opening of activity",Toast.LENGTH_SHORT).show();
				finish();
			}
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		getMenuInflater().inflate(R.menu.activity_pic_contact, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		if(item.getItemId()==R.id.settings_pic_contact_refresh)
		{
			Thread t=new Thread(new Runnable() {
				
				@Override
				public void run() {
					refresh_list();
					
				}
			});
			t.start();
		}
		
		return super.onOptionsItemSelected(item);
	}
	public void load_contacts_from_database()
	{
		
		
		handler.post(new Runnable() {
			
			@Override
			
			public void run() {
				
				Db_functions db_funcs;
				 db_funcs=new Db_functions(Activity_pic_contact.this.getApplicationContext());
				 adapter.arraylist.clear();
				 Cursor cursor=db_funcs.read_table_contacts();
				 cursor.moveToFirst();
					while(cursor.moveToNext())
					{
						Class_group_object object=new Class_group_object(cursor.getString(1),cursor.getString(2),null);
						adapter.addItem(object);
					}
					db_funcs.close_all();
					adapter.notifyDataSetChanged();
			}
		});
		
	}
	
	public void refresh_list()
	{
		handler.post(new Runnable() {
			
			@Override
			public void run() {
				progressbar.setVisibility(View.VISIBLE);
				adapter.arraylist.clear();
			}
		});
		//Db_functions db_funcs=new Db_functions(this.getApplicationContext());
	//	db_funcs.delete_table_contacts();
		ContentResolver resolver=getContentResolver();
		
		String[] phone_numbers=Class_give_phones.give(resolver);
		int count=phone_numbers.length;
		String[] result=Class_verify_phone_number.verification(phone_numbers, count);
		
		for(int i=0;i<count;i++)
		{
			if(result[i].equals("1"))
			{
				
				String name="";
				String temp;
				Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phone_numbers[i]));
				Cursor cursor_names = resolver.query(uri, new String[]{PhoneLookup.DISPLAY_NAME}, null, null, null);
				if(cursor_names.moveToFirst())
				{
					while(cursor_names.moveToNext())
					{
						temp=cursor_names.getString(cursor_names.getColumnIndex(PhoneLookup.DISPLAY_NAME));
						if(temp.length()!=0)
						{
							name=temp;
						}
					}
				}
				cursor_names.close();	
				adapter.addItem(new Class_group_object(name,phone_numbers[i],null));
			}
			
		}
		//db_funcs.close_all();

		handler.post(new Runnable() {
			
			@Override
			public void run() {
				progressbar.setVisibility(View.INVISIBLE);
				adapter.notifyDataSetChanged();
			}
		});

	}
}
