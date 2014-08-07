package com.codebase.locationgpstest.app;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.codebase.locationgpstest.LocationConstants;

public class LocationApplication extends Application {
	private static Context context;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		context = getApplicationContext();
		Log.e(LocationConstants.TAG_PROJECT, "Application on create");
		
		
	}
	public static Context getAppContext()
	{
		return context;
	}
	
	public static void makeToast(String message)
	{
	
		Toast.makeText(LocationApplication.getAppContext(), message, Toast.LENGTH_SHORT).show();
	}
}
