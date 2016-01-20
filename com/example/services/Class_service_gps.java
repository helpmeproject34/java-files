package com.example.services;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.json.Class_server_details;
import com.example.json.JSONParser;
import com.example.login.Class_alreadyLogin;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

public class Class_service_gps extends Service {

	public static Thread t=null;
	Handler handler=new Handler();
	static JSONParser parser = new JSONParser();
	@Override
	public IBinder onBind(Intent arg0) {
		
		return null;
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		if(t==null&&Class_alreadyLogin.login_or_not(getApplicationContext()))
		{
			t=new Thread(new Runnable() {
				
				@Override
				public void run() {
					while(true)
					{
						handler.post(new Runnable() {
							
							@Override
							public void run() {
								
								Toast.makeText(getApplicationContext(), "running", Toast.LENGTH_SHORT).show();
							}
						});
						send();
						try {
							Thread.sleep(10000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							//e.printStackTrace();
						}
					}
					
				}
			});
			
			t.start();
		}
		
		return super.onStartCommand(intent, flags, startId);
	}
	
	private void send()
	{
		
		if(true)
		{
			
			String var_username=Class_alreadyLogin.username;
			String var_phone=Class_alreadyLogin.phone;
			String latitude=Math.random()*100+"";
			String longitude=Math.random()*100+"";
			
			String url=Class_server_details.server_ip+"/account/location";
			
			List<NameValuePair> params = new ArrayList<NameValuePair>();
	        params.add(new BasicNameValuePair("username", var_username));
	        params.add(new BasicNameValuePair("mobile",var_phone));
	        params.add(new BasicNameValuePair("latitude",latitude));
	        params.add(new BasicNameValuePair("longitude",longitude));
		
	        JSONObject json = parser.makeHttpRequest(url, "POST", params);
	        if(json==null)
	        {
	        	return ;
	        }
	        try {
	        	final int success;
	        	String success_value=json.getString("success");
	        	if(success_value.equals("True"))
	        	{
	        		success=1;
	        	}
	        	else
	        	{
	        		success=0;
	        	}
	        	handler.post(new Runnable() {
					
					@Override
					public void run() {
						
						if(success==1)
			        	{
			        		Toast.makeText(getApplicationContext(), "success=1",Toast.LENGTH_SHORT).show();
			        	
			        	}
			        	else
			        	{
			        		Toast.makeText(getApplicationContext(), "success=0",Toast.LENGTH_SHORT).show();
			        	}
					}
				});
	        	
	        		
	        	
			} catch (JSONException e1) {
				
			}
	        catch(NullPointerException e)
	        {
	        	
	        }
	        catch(Exception e)
	        {
	        	
	        }
		}
	}
}
