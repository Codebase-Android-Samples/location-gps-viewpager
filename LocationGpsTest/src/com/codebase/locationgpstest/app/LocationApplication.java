package com.codebase.locationgpstest.app;

import com.codebase.locationgpstest.LocationConstants;

import android.app.Application;
import android.util.Log;

public class LocationApplication extends Application {
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.e(LocationConstants.TAG_PROJECT, "Application on create");
		
		
	}
	
	
}
