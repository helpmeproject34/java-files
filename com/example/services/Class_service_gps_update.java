package com.example.services;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.after_login.Class_GPS_statefinder;
import com.example.broadcast_recievers.Class_reciever_gps;
import com.example.json.Class_server_details;
import com.example.json.JSONParser;
import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;


public class Class_service_gps_update extends IntentService {

	
	private Handler handler=new Handler();
	static JSONParser parser = new JSONParser();
	public static Thread t=null;
	public Class_service_gps_update() {
		super(Class_service_gps_update.class.getName());
	}

	
	@Override
	protected void onHandleIntent(Intent intent) {
		//run_this();
		Toast.makeText(getApplicationContext(), "onHandleIntent() is called", Toast.LENGTH_SHORT).show();
	}
    @Override
    public void onStart(Intent intent, int startId) {
    	
    	super.onStart(intent, startId);
    	run_this();
    }
    private void run_this()
    {
    	Toast.makeText(getApplicationContext(), "service started", Toast.LENGTH_SHORT).show();
    	t=new Thread(new Runnable() {
			
			@Override
			public void run() {
				int i=0;
		    	while(true&&i<20)
		    	{
		    		handler.post(new Runnable() {
						
						@Override
						public void run() {
							Toast.makeText(getApplicationContext(), "running...", Toast.LENGTH_SHORT).show();
						}
					});
		    		
		    		
		    		send();
		    		
		    		
		    		try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						
					}
		    		i++;
		    	}
				
			}
    	});
    	t.start();    	
    	  	
    }
    private void send()
	{
		
		if(Class_server_details.server_on==1)
		{
			
			String var_username="gangadhar";
			String var_phone="8888888888";
			String latitude=Math.random()*100+"";
			String longitude=Math.random()*100+"";
			
			String url=Class_server_details.server_ip+"/android/project/update_location.php";
			
			List<NameValuePair> params = new ArrayList<NameValuePair>();
	        params.add(new BasicNameValuePair("username", var_username));
	        params.add(new BasicNameValuePair("phone",var_phone));
	        params.add(new BasicNameValuePair("latitude",latitude));
	        params.add(new BasicNameValuePair("longitude",longitude));
			
	        
	        
	        
	        JSONObject json = parser.makeHttpRequest(url, "POST", params);
	        if(json==null)
	        {
	        	return ;
	        }
	        try {
	        	final int success=json.getInt("success");
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
	private void print()
	{
		Toast.makeText(getApplicationContext(), "updating server....", Toast.LENGTH_SHORT).show();
	}
   private void garbage()
   {
	   if(t==null)
   	{
   		t=new Thread(new Runnable() {
   			
   			@Override
   			public void run() {
   				
   				while(true)
   				{	
   					handler.post(new Runnable() {
   						
   						@Override
   						public void run() {
   							print();
   							
   						}
   					});
   		
   					send();
   					try {
   						Thread.sleep(5000);
   					} catch (InterruptedException e) {
   						
   					}
   				}
   			}
   		});
       	t.start();
   	}
   }
}
	

