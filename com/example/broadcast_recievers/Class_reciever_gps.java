package com.example.broadcast_recievers;


import com.example.services.Class_service_gps;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;



import android.widget.Toast;

public class Class_reciever_gps extends BroadcastReceiver {

	 public  static  int GPS;
	public static Thread t=null;

	
	Context con;
	@Override
	public void onReceive(final Context context, Intent intent) {
		
		con=context;
		if(intent.getAction().matches("android.location.PROVIDERS_CHANGED"))
		{
			
			LocationManager manager=(LocationManager)context.getSystemService(context.LOCATION_SERVICE);
			if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER))
			{
	            GPS=1;
				Toast.makeText(context, "GPS turned ON", Toast.LENGTH_SHORT).show();
				Intent i=new Intent(context,Class_service_gps.class);
				context.stopService(i);
				context.startService(i);
			}
			else
			{
				Intent i=new Intent(context,Class_service_gps.class);
				context.stopService(i);
		    	GPS=0;
		    	Toast.makeText(context, "GPS turned OFF and SERVICE stopped", Toast.LENGTH_SHORT).show();
		    	t=null;
		    }
		
		
		}
	
	}
}