package com.example.groups;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.example.json.Class_server_details;
import com.example.json.JSONParser;

public class Class_locations_provider {
	static JSONParser parser = new JSONParser();
	public static void provide_locations(ArrayList<Class_locations> locations,String groupid,String username)
	{
		if(Class_server_details.server_on==1)
		{
			String url=Class_server_details.server_ip+"/groupmembers";
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("groupid",groupid));
			
			try {
				JSONObject json = parser.makeHttpRequest(url, "POST", params);
				String response=json.getString("success");
	        	String[] comma_sep=response.split(",");
	        	int len=comma_sep.length;
	        	for(int j=0;j<len;j++)
	        	{
	        		String[] split=comma_sep[j].split(";");
	        		if(split[0].equals("False")||split.length!=5)
	        		{
	        			locations.add(new Class_locations(split[0],"split 1","split2","split 3","split 4"));
	        		}
	        		else
	        		{
	        			locations.add(new Class_locations(split[0],split[1],split[2],split[3],split[4]));
	        		}
	        		
	        		
	        	}
	        	
			} catch (JSONException e1) {
				Log.d("exception", "json exception from location provider");
			}
	        catch(NullPointerException e)
	        {
	        	Log.d("exception", "null pointer exception from location provider "+e.getMessage());
	        }
			catch(ArrayIndexOutOfBoundsException e)
			{
				Log.d("exception", "out of bound exception from location provider "+e.getMessage());
			}
	        catch(Exception e)
	        {
	        	Log.d("exception", "unknown exception from location provider "+e.getMessage());
	        }
		}
		else if(Class_server_details.server_on==0)
		{
			
		}
		
	}
}
