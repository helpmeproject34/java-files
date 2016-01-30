package com.example.after_login;

import java.util.ArrayList;

import com.example.project_practise.R;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class Adapter_tracklist extends BaseAdapter{

	ArrayList<Class_tracklist_object> arraylist;
	public Adapter_tracklist()
	{
		arraylist=new ArrayList<Class_tracklist_object>();
	}
	@Override
	public int getCount() {
		
		return arraylist.size();
	}

	@Override
	public Object getItem(int index) {
	
		return arraylist.get(index);
	}

	@Override
	public long getItemId(int id) {
		
		return id;
	}

	@Override
	public View getView(int index, View view, ViewGroup parent) {
		if(view==null)
		{
			LayoutInflater inflater= LayoutInflater.from(parent.getContext());
			view=inflater.inflate(R.layout.layout_tracklist,parent,false);	
		}
		Class_tracklist_object object=arraylist.get(index);
		final String details=object.phone+"\n"+object.name;
		TextView tv=(TextView)(view.findViewById(R.id.textview_layout_tracklist));
		tv.setText(details);
		CheckBox cb=(CheckBox)(view.findViewById(R.id.checkbox_layout_tracklist));
		if(object.check)
		{
			cb.setChecked(true);
		}
		else
		{
			cb.setChecked(false);
		}
		return view;
	}

}
