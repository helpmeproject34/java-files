package com.example.friends;

import java.util.HashMap;



import com.example.alert_dialogs.Alert_ok;
import com.example.groups.Class_locations;
import com.example.project_practise.R;
import com.example.project_practise.Response;
import com.example.tracking.Activity_tracking_friend;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class Activity_showfriends extends Activity {

	Adapter_friends adapter;
	ProgressBar progressbar;
	Handler handler;
	RelativeLayout layout;
	String var_username;
	String var_phone;
	Bundle bundle;
	Context this_context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_showfriends);
		
		 Intent var_intent_received=getIntent();
		 if(var_intent_received==null||var_intent_received.getExtras()==null)
		 {
			 Toast.makeText(getApplicationContext(), "Illeagal opening of activity",Toast.LENGTH_SHORT).show();
			 finish();
		 }
		 else
		 {
			 this_context=this;	
			 bundle=var_intent_received.getExtras();
			 var_username=bundle.getString("username");
			 var_phone=bundle.getString("phone");
			 adapter=new Adapter_friends();
				ListView listview=(ListView)findViewById(R.id.listview_showfriends);
				listview.setAdapter(adapter);
				
				progressbar=(ProgressBar)findViewById(R.id.progressbar_showfriends);
				progressbar.setVisibility(View.INVISIBLE);
				handler=new Handler();
				layout=(RelativeLayout)findViewById(R.id.relativelayout_activity_showfriends);
				progressbar.setVisibility(View.VISIBLE);
				listview.setOnItemClickListener(new OnItemClickListener() {
				
					
					@Override
					public void onItemClick(AdapterView<?> arg0, View view, int index,
							long arg3) {
						
						progressbar.setVisibility(View.VISIBLE);
						
						final Class_friend_object object=adapter.list.get(index);
						final String friend_phone=object.phone;
						final String friend_name=object.name;
						Thread t=new Thread(new Runnable() {
							
							@Override
							public void run() {
								
								Class_locations location=new Class_locations();
								final Response result=Class_check_tracking.check(var_phone,friend_phone,friend_name,location);
								if(result.bool)
								{
									handler.post(new  Runnable() {
										public void run() {
											Toast.makeText(getApplicationContext(),result.message,Toast.LENGTH_SHORT).show();
											
											progressbar.setVisibility(View.INVISIBLE);
											Intent intent=new Intent(getApplicationContext(),Activity_tracking_friend.class);
											Bundle bundle=new Bundle();
											bundle.putString("username",var_username);
											bundle.putString("phone", var_phone);
											bundle.putString("friend_name", friend_name);
											bundle.putString("friend_phone", friend_phone);
											intent.putExtras(bundle);
											startActivity(intent);
											
										}
									});
								}
								else
								{
									handler.post(new  Runnable() {
										public void run() {
											//Toast.makeText(getApplicationContext(),result.message,Toast.LENGTH_SHORT).show();
											Alert_ok.show(this_context, result.message);
											progressbar.setVisibility(View.INVISIBLE);
										}
									});
								}
							}
						});
						t.start();
					}
				});
				
				refresh();
		 }
		
		
		
	}
	private void clean()
	{
		int len=layout.getChildCount();
		for(int i=0;i<len;i++)
		{
			View view=layout.getChildAt(i);
			if(view instanceof Button )
			{
				layout.removeViewAt(i);
			}
			else if(view instanceof TextView)
			{
				layout.removeViewAt(i);
			}
		}
	}
	private void refresh()
	{
		adapter.list.clear();
		clean();
		Thread t=new Thread(new Runnable() {
			
			@Override
			public void run() {
				handler.post(new Runnable() {
					
					@Override
					public void run() {
						progressbar.setVisibility(View.VISIBLE);
						
					}
				});
				retrieve_contacts();
				handler.post(new Runnable() {
					
					@Override
					public void run() {
						progressbar.setVisibility(View.INVISIBLE);
						
					}
				});
			}
		});
		t.start();
	}
	public void retrieve_contacts()
	{
		
		ContentResolver resolver=getContentResolver();
		//String[] phones=Class_give_phones.give(resolver);
		
		
		Cursor cursor_phones=resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null, null, null);
		
		int count=cursor_phones.getCount();
		String phone_numbers[]=new String[count];
		HashMap hashmap=new HashMap();
	
		
		int i=0;
		if(cursor_phones.moveToFirst())
		{
			while(cursor_phones.moveToNext())
			{
				String phone_num = cursor_phones.getString(cursor_phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
				phone_num=Class_purify_phone_number.purify(phone_num.trim());
			
				if(phone_num.trim().length()==10)
				{
					
					if(!hashmap.containsKey(phone_num))
					{
						hashmap.put(phone_num, null);
						phone_numbers[i]=phone_num;
						
						i++;
					}
				}
			}
			count=i;
			cursor_phones.close();
				final String[] result=Class_verify_phone_number.verification(phone_numbers, count);
				for(i=0;i<count;i++)
				{
					if(result[i].equals("1"))
					{
						//String id=cursor_phones.getString(cursor_phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
						String name="";
						String temp="";
						Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phone_numbers[i]));
						Cursor cursor_names = resolver.query(uri, new String[]{PhoneLookup.DISPLAY_NAME}, null, null, null);
						if(cursor_names.moveToFirst())
						{
							while(cursor_names.moveToNext())
							{
								temp=cursor_names.getString(cursor_names.getColumnIndex(PhoneLookup.DISPLAY_NAME));
								if(temp.length()>0)
								{
									name=temp;
								}
							}
						}
						cursor_names.close();
						
						
							final Class_friend_object obj=new Class_friend_object(name,phone_numbers[i]);
							handler.post(new Runnable() {
								
								@Override
								public void run() {
									
									adapter.list.add(obj);
									adapter.notifyDataSetChanged();
								}
							});
						
						
						
					}
				}
				handler.post(new Runnable() {
					
					@Override
					public void run() {
						if(adapter.list.size()==0)
						{
							
							if(result[result.length-1].equals("1"))
							{
								Toast.makeText(getApplicationContext(),"No friends to show", Toast.LENGTH_SHORT).show();
								TextView tv=new TextView(getApplicationContext());
								tv.setText("No Friends to show");
								LayoutParams params=new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
								tv.setLayoutParams(params);
								tv.setVisibility(View.VISIBLE);
								layout.addView(tv);
							}
							else
							{
								//Toast.makeText(getApplicationContext(),"Unable to connect to server\nCheck internet connection\nPlease try again", Toast.LENGTH_SHORT).show();
								Alert_ok alert=new Alert_ok()
								{
									@Override
									public void ontrue() {
										
										super.ontrue();
										refresh();
									}
									@Override
									public void onfalse() {
										
										super.onfalse();
										
									}
								};
								alert.ok_or_cancel(this_context, "","Unable to connect to server\nCheck internet connection\nPlease try again","CANCEL","REFRESH");
								Button b=new Button(getApplicationContext());
								b.setText("Refresh");
								b.setOnClickListener(new OnClickListener() {
									
									@Override
									public void onClick(View arg0) {
										layout.removeView(arg0);
										refresh();
										
									}
								});
								LayoutParams params=new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
								b.setLayoutParams(params);
								layout.addView(b);
							}
							
						}
						else
						{
							Toast.makeText(getApplicationContext(),"Complete List loaded", Toast.LENGTH_LONG).show();
						}
						
					}
				});
				

		}
		handler.post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				progressbar.setVisibility(View.INVISIBLE);
			}
		});
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_showfriends, menu);
		return false;
	}

}
