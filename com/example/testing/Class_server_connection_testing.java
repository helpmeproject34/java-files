package com.example.testing;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.example.json.Class_server_details;
import com.example.json.JSONParser;

public class Class_server_connection_testing {
static JSONParser parser = new JSONParser();
	
	
	public static void verification()
	{
		
		
		
		if(Class_server_details.server_on==1)
		{
			String url=Class_server_details.server_ip+"/account/checknumber";
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			
			
				params.add(new BasicNameValuePair(0+"","9970610243"));
				
	
			
			try {
				JSONObject json = parser.makeHttpRequest(url, "POST", params);
				String response=json.getString("success");
	        	String[] comma_sep=response.split(",");
	        	int len=comma_sep.length;
	        	for(int j=0;j<len;j++)
	        	{
	        		String[] split=comma_sep[j].split(":");
	        		
	        		
	        	}
	        	
			} catch (JSONException e1) {
				Log.d("exception", "json exception");
			}
	        catch(NullPointerException e)
	        {
	        	Log.d("exception", "null pointer exception");
	        }
	        catch(Exception e)
	        {
	        	Log.d("exception", "unknown exception");
	        }
		}
		
		
		
	}
	
}
