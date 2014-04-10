package com.buitio.builtpushnotification;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.raweng.built.userInterface.R;

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
public class NotificationAlert extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		/*
		 * Notification opens this dialog.
		 */
		AlertDialog.Builder alert = new AlertDialog.Builder(NotificationAlert.this);
		alert.setIcon(R.drawable.ic_launcher)
		.setTitle("Built Notification")
		.setMessage("New Photo added. Check out!!")
		.setPositiveButton("OK",new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int arg1) {
				dialog.cancel();
				finish();
			}
		}).create().show();
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}
}
