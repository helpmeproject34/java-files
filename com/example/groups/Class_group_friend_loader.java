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

public class Class_group_friend_loader {

	static JSONParser parser = new JSONParser();
	public static void load_friends(ArrayList<Class_group_object>friends_objects,String username,String phone,String groupid)
	{
		
		if(Class_server_details.server_on==1)
		{
			friends_objects.clear();
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
	        		if(split.length!=5)
	        		{
	        			friends_objects.add(new Class_group_object(split[0],"split 1","split 2"));
	        		}
	        		else
	        		{
	        			friends_objects.add(new Class_group_object(split[0],split[1],split[2]));
	        		}
	        		//(new Class_locations(split[0],split[1],split[2],split[3],split[4]));
	        	}
	        	
			} catch (JSONException e) {
				Log.d("exception", "json exception from group_friend_loader "+e.getMessage());
			}
	        catch(NullPointerException e)
	        {
	        	Log.d("exception", "null pointer exception from group_friend_loader "+e.getMessage());
	        }
			catch(ArrayIndexOutOfBoundsException e)
			{
				Log.d("exception", "out of bound exception from group_friend_loader "+e.getMessage());
			}
	        catch(Exception e)
	        {
	        	Log.d("exception", "unknown exception from group_friend_loader "+e.getMessage());
	        }
		}
		else if(Class_server_details.server_on==0)
		{
			
		}
	}
	
}
