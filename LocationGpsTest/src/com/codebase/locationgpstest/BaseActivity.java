package com.codebase.locationgpstest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.codebase.locationgpstest.app.LocationApplication;
import com.codebase.locationgpstest.utils.core.ResourceChecker;

public class BaseActivity extends Activity {
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(!ResourceChecker.isGPSActivated(BaseActivity.this))
		{
			 new AlertDialog.Builder(BaseActivity.this).setMessage("GPS required.").setCancelable(false).setPositiveButton("Enable GPS", new DialogInterface.OnClickListener() {
                 public void onClick(DialogInterface dialog, int id) {
                     	
                       ResourceChecker.openGPSSettings(BaseActivity.this);
                       BaseActivity.toastGPSStatus();
                 }
         }).setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                 public void onClick(DialogInterface dialog, int id) {
                         dialog.cancel();
                         BaseActivity.this.finish();
                 }
         }).create().show();
		}
	}
	
	@Override
	protected void onPause() {
		
		// TODO Auto-generated method stub
		super.onPause();
		
	}
	

	private static void toastGPSStatus()
	{
		LocationApplication.makeToast("Gps is "+ ResourceChecker.isGPSActivated(LocationApplication.getAppContext()));
	}
}
