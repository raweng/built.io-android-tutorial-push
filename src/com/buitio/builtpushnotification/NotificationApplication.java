package com.buitio.builtpushnotification;

import com.raweng.built.Built;

import android.app.Application;

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
public class NotificationApplication extends Application{

	@Override
	public void onCreate() {
		super.onCreate();
		
		/*
		 * Initialize the application for a project using built.io Application credentials "Application Api Key" 
		 * and "Application UID".
		 * 
		 */
		try {
			Built.initializeWithApiKey(NotificationApplication.this, "YOUR_APP_API_KEY", "YOUR_APP_UID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
