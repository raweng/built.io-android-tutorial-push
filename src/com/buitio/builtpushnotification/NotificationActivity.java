package com.buitio.builtpushnotification;

import java.util.ArrayList;

import com.google.android.gcm.GCMRegistrar;
import com.raweng.built.BuiltError;
import com.raweng.built.BuiltInstallation;
import com.raweng.built.BuiltResultCallBack;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

/**
 * This is built.io android tutorial.
 * 
 * Short introduction of some classes with some methods.
 * Contain classes: 1.BuiltInstallation
 * 
 * For quick start with built.io refer "http://docs.built.io/quickstart/index.html#android"
 * For GCM concept refer "http://developer.android.com/google/gcm/gcm.html"
 * 
 * @author raw engineering, Inc
 *
 */
public class NotificationActivity extends Activity {

	private static String TAG = "NotificationActivity";
	private static String SENDER_ID = "YOUR_SENDER_ID";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		GCMRegistrar.checkDevice(this);
		GCMRegistrar.checkManifest(this);
		
		String deviceToken = GCMRegistrar.getRegistrationId(this);
		
		Intent intent = new Intent();
		
		if(intent.hasExtra("regId")){
			createInstallation(intent.getStringExtra("regId"));
		}
		
		if (deviceToken.equals("")) {
			GCMRegistrar.register(this, SENDER_ID);
		} else {
			Log.v(TAG, "Already registered");
			Log.v(TAG, deviceToken);
			createInstallation(deviceToken);
		}
		
		
	}
	
	public void createInstallation(String deviceToken){
	
		/*
		 * BuiltInstallation is register user device with channel on built.io for GCM Notification
		 * 
		 * There are many type of channel get use which trigger with event
		 * Here eg. "classUid.object.create" which is show notification when inside class object created notification will receive.
		 * For more refer : http://static.built.io/downloads/sdk-docs/android-docs/com/raweng/built/BuiltInstallation.html#createInstallation(java.lang.String, java.util.ArrayList, com.raweng.built.BuiltResultCallBack)
		 * 
		 */
		BuiltInstallation installation = new BuiltInstallation();
		
		ArrayList<String> installationChannels = new ArrayList<String>();
		installationChannels.add("userphoto.object.create");
		installationChannels.add("pushNotification");
		
		installation.createInstallation(deviceToken, installationChannels, new BuiltResultCallBack() {
			
			@Override
			public void onSuccess() {
				
				/// installation is created successfully.
				Toast.makeText(NotificationActivity.this, "Installation created successfully...", Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onError(BuiltError builtErrorObject) {
				
				/// builtErrorObject contains more details of error.
				Toast.makeText(NotificationActivity.this, "error :"+builtErrorObject.getErrorMessage() , Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onAlways() {
				
				/// write code here that user want to execute.
				/// regardless of success or failure of the operation.
				finish();
			}
		});
		
		
	}
}
