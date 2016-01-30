package com.example.friends;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.groups.Class_locations;
import com.example.json.Class_server_details;
import com.example.json.Class_server_error_codes;
import com.example.json.JSONParser;
import com.example.project_practise.Response;

public class  Class_check_tracking {

	static JSONParser parser = new JSONParser();
	public static Response check(String phone,String friend_phone,String friend_name ,Class_locations location)
	{
		Response result=new Response();
		result.value=0;
		result.bool=false;
		
		
		if(Class_server_details.server_on==1)
		{
			
			String url=Class_server_details.server_ip+"/blockcheckfriend";
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			
			
			params.add(new BasicNameValuePair("friend",friend_phone));
			params.add(new BasicNameValuePair("mobile",phone));
			
			result.message="cannot track this person now";
			try {
				JSONObject json = parser.makeHttpRequest(url, "POST", params);
				
				String response=json.getString("success");
				String latitude=json.getString("latitude");
				String longitude=json.getString("longitude");
				String last_updated=json.getString("last_updated");
				String friend_username="some username";//json.getString("username");
				if(response.equals("False")||response.equals("")||response==null)
				{
					result.message=friend_name+" blocked you\nSo you cannot track this person now.";
					result.value=-1;
				}
				else
				{
					result.message="You can track "+friend_name;
		        	result.bool=true;
		        	location.latitude=latitude;
		        	location.longitude=longitude;
		        	location.username=friend_username;
		        	location.phone=friend_phone;
		        	location.last_updated=last_updated;
				}
	        	
	        	
			} catch (JSONException e1) {
				
				result.message="Oops !! error occured\n"+e1.getMessage()+"\n"+Class_server_error_codes.give_error(parser.status_code);
				result.value=0;
			}
	        catch(NullPointerException e)
	        { 	
	        	result.message="Unable to connect to server. Please check your network connection \n"+e.getMessage()+"\n"+Class_server_error_codes.give_error(parser.status_code);
	        	result.value=0;
	        }
	        catch(Exception e)
	        {
	        	result.message="Unknown error occured\n"+e.getMessage()+"\n"+Class_server_error_codes.give_error(parser.status_code);
	        	result.value=0;
	        }
		}
		else
		{
			result.bool=false;
			result.message="not connected to server";
		}
		return result;
	}
}
