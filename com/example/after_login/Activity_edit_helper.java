package com.example.after_login;

import com.example.project_practise.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

import android.widget.Spinner;


public class Activity_edit_helper extends Activity {

	Spinner spinner;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_helper);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_edit_helper, menu);
		return false;
	}

}
