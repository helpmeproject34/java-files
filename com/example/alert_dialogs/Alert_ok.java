package com.example.alert_dialogs;

import com.example.project_practise.R;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class Alert_ok {

	
	public static void show(Context context,String message)
	{
		final Dialog dialog=new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_display);
		//dialog.setTitle(title);
		TextView tv=(TextView)dialog.findViewById(R.id.textview_dialog_display);
		tv.setText(message);
		Button b=(Button)dialog.findViewById(R.id.button_dialog_display);
		b.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
				
			}
		});
		
		dialog.show();
	}
	public  void  ok_or_cancel(Context context,String title,String message)
	{
		ok_or_cancel(context,title,message,"CANCEL","OK");
	}
	public  void ok_or_cancel(Context context,String title,String message,String left,String right)
	{
		
		final Dialog dialog=new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setContentView(R.layout.dialog_ok_cancel);
		//dialog.setTitle(title);
		TextView tv=(TextView)dialog.findViewById(R.id.textview_dialog_ok_cancel);
		tv.setText(message);
		Button left_button=(Button)dialog.findViewById(R.id.button_dialog_ok_cancel_left);
		left_button.setText(left);
		left_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
				onfalse();
			}
		});
		Button right_button=(Button)dialog.findViewById(R.id.button_dialog_ok_cancel_right);
		right_button.setText(right);
		right_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
				ontrue();
				
			}
		});
		
		dialog.show();
	}
	public void ontrue()
	{
	
	}
	public void onfalse()
	{
		
	}
	
}
