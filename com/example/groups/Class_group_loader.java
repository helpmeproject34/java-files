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

public class Class_group_loader {
	static JSONParser parser = new JSONParser();
	public static void get_group_names(String username,String phone,ArrayList<Class_group_object> group_objects)
	{
		group_objects.clear(); // first clear all the objects from array list
		
		if(Class_server_details.server_on==1)
		{
			
			
			
			String url=Class_server_details.server_ip+"/checkgroup";
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			
			
			params.add(new BasicNameValuePair("username",username));
			params.add(new BasicNameValuePair("mobile",phone));
			
			try {
				JSONObject json = parser.makeHttpRequest(url, "POST", params);
				String response=json.getString("success");
				if(response.equals("False")||response.equals("")||response==null)
				{
					Log.e("False","false detected in Class_group_load");
				}
				else
				{
					String[] comma_sep=response.split(",");
		        	int len=comma_sep.length;
		        	for(int j=0;j<len;j++)
		        	{
		        		String[] split=comma_sep[j].split(":");
		        		if(split.length!=2)
		        		{
		        			group_objects.add(new Class_group_object(split[0],"Group id is : null","-1"));
		        		}
		        		else
		        		{
		        			group_objects.add(new Class_group_object(split[0],"Group id is : "+split[1],split[1]));
		        		}
		        		
		        	}
				}
	        	
	        	
			} catch (JSONException e1) {
				Log.d("exception", "json exception from class_group_load");
			}
	        catch(NullPointerException e)
	        {
	        	Log.d("exception", "null pointer exception from class_group_load");
	        }
	        catch(Exception e)
	        {
	        	Log.d("exception", "unknown exception from class_group_load "+e.getMessage());
	        }
		}
		else
		{
			group_objects.add(new Class_group_object("group 1","Group id is : "+"random","0"));
			group_objects.add(new Class_group_object("group 2","Group id is : "+"random","0"));
			group_objects.add(new Class_group_object("group 3","Group id is : "+"random","0"));
		}
	}
}
