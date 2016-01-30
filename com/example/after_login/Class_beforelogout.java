package com.example.after_login;

import com.example.login.Class_alreadyLogin;


public class Class_beforelogout {

	public static void beforelogout()
	{
		Class_alreadyLogin.phone="-1";
		Class_alreadyLogin.username="-1";
		Class_alreadyLogin.islogin=false;
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
	}
}
