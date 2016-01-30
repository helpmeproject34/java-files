package com.example.services;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.gps.Class_location_listener;
import com.example.json.Class_server_details;
import com.example.json.JSONParser;
import com.example.login.Class_alreadyLogin;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

public class Class_service_gps extends Service {

	public static Thread t=null;
	public boolean run_service=true;
	Handler handler=new Handler();
	static JSONParser parser = new JSONParser();
	LocationManager location_manager;
	public Location location;
	private static final long MIN_DISTANCE_FOR_UPDATE = 10;
	private static final long MIN_TIME_FOR_UPDATE = 1000 * 60 * 2;
	
	//Context context=getApplicationContext();
	
	public Class_service_gps()
	{
		
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		
		return null;
	}
	@Override
	@Deprecated
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		run_service=true;
	}
	@Override
	public void onDestroy() {
		
		super.onDestroy();
		run_service=false;
		t=null;
	}
	@Override
	public boolean stopService(Intent name) {
		// TODO Auto-generated method stub
		run_service=false;
		t=null;
		return super.stopService(name);
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		run_service=true;
		location_manager=(LocationManager)(getApplicationContext()).getSystemService(LOCATION_SERVICE);
		if(t==null&&Class_alreadyLogin.login_or_not(getApplicationContext()))
		{
			t=new Thread(new Runnable() {
				
				@Override
				public void run() {
					while(run_service&&Class_alreadyLogin.islogin)
					{
						handler.post(new Runnable() {
							
							@Override
							public void run() {
								
								Toast.makeText(getApplicationContext(), "running", Toast.LENGTH_SHORT).show();
								location=giveLocation();
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
			
			String latitude;//=Math.random()*100+"";
			String longitude;//=Math.random()*100+"";
			if(location!=null)
			{
				latitude=location.getLatitude()+"";
				longitude=location.getLongitude()+"";
			}
			else                                                   
			{
				latitude=-1+"";
				longitude=-1+"";
			}
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
	public Location giveLocation()
	{
		Location location=null;
		if(location_manager.isProviderEnabled(LocationManager.GPS_PROVIDER))
		{
			try
			{
				location_manager.requestLocationUpdates(LocationManager.GPS_PROVIDER,MIN_TIME_FOR_UPDATE,MIN_DISTANCE_FOR_UPDATE, new Class_location_listener());
				if(location_manager!=null)
				{
					location=location_manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
				}
			}
			catch(Exception e)
			{
				Toast.makeText(getApplicationContext(), "Exception occured while taking location from GPS", Toast.LENGTH_SHORT).show();
			}
			
		}
		else if(location_manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
		{
			try
			{
				location_manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,MIN_TIME_FOR_UPDATE,MIN_DISTANCE_FOR_UPDATE, new Class_location_listener());
				if(location_manager!=null)
				{
					location=location_manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
				}
			}
			catch(Exception e)
			{
				Toast.makeText(getApplicationContext(), "Exception occured while taking location from NETWORK", Toast.LENGTH_SHORT).show();
			}
		}
		return location;
	}
}
