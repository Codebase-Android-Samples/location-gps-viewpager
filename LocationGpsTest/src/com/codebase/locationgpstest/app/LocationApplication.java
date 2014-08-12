package com.codebase.locationgpstest.app;

import android.app.Application;
import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import com.codebase.locationgpstest.LocationConstants;
import com.codebase.locationgpstest.app.Foreground.Listener;
import com.codebase.locationgpstest.utils.LocationUpdateListner;
import com.codebase.locationgpstest.utils.LocationUpdater;

public class LocationApplication extends Application implements Listener {
	private static Context context;
	private Foreground foreground;
	public static String TAG_LOCATION_APPLICATION = "TAG_LOCATION_APPLICATION";

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

		context = getApplicationContext();
		foreground = Foreground.get(this);
		foreground.addListener(this);
		Log.e(LocationConstants.TAG_PROJECT, "Application on create");
		LocationUpdater.getUpdate(context, new LocationUpdateListner() {

			@Override
			public void onLocationUpdate(Location location) {
				// TODO Auto-generated method stub
				Log.e(TAG_LOCATION_APPLICATION, "Location Found");
				System.out.println("Location found" + location);
				makeToast(String.format(
						"Location{provider: %s, latitude: %s, longitude: %s}",
						location.getProvider(), location.getLatitude(),
						location.getLongitude()));

			}
		});

	}

	public static Context getAppContext() {
		return context;
	}

	public static void makeToast(String message) {

		Toast.makeText(LocationApplication.getAppContext(), message,
				Toast.LENGTH_SHORT).show();
	}

	public static void LogError(String string) {
		Log.e(TAG_LOCATION_APPLICATION, string);
	}

	@Override
	public void onBecameForeground() {
		// TODO Auto-generated method stub
		LogError("Application is forground");

	}

	@Override
	public void onBecameBackground() {
		// TODO Auto-generated method stub
		LogError("Application is background");
	}
}
