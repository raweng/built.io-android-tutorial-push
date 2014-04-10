package com.buitio.builtpushnotification;

import java.util.ArrayList;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gcm.GCMBaseIntentService;
import com.raweng.built.BuiltError;
import com.raweng.built.BuiltInstallation;
import com.raweng.built.BuiltResultCallBack;
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
public class GCMIntentService extends GCMBaseIntentService {

	private CharSequence data;
	Handler handler = new Handler();

	public GCMIntentService(){
		super("test");
	}

	protected GCMIntentService(String senderId) {
		super(senderId);
		Log.i(TAG,senderId);
	}

	@Override
	protected void onError(Context arg0, String arg1) {
		Log.i(TAG,arg1);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onMessage(Context context, Intent arg1) {
		Intent notificationIntent;
		Notification notification;
		PendingIntent contentIntent;

		/*
		 * Notification design is define.
		 */
		data = arg1.getStringExtra("message");
		Toast.makeText(context, arg1.getStringExtra("message"), Toast.LENGTH_LONG).show();
		NotificationManager nManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
		notificationIntent = new Intent(context, NotificationAlert.class);
		notificationIntent.putExtra("message", data);
		notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);
		notification = new Notification(R.drawable.ic_launcher,data,System.currentTimeMillis());
		notification.setLatestEventInfo(context,"Notification",data,contentIntent);
		notification.flags = Notification.FLAG_AUTO_CANCEL;
		notification.sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		nManager.notify(0, notification);

	}

	@Override
	protected void onRegistered(final Context arg0, final String arg1) {

		Toast.makeText(arg0, arg1.toString(), Toast.LENGTH_LONG).show();

		handler.post(new Runnable() {
			@Override
			public void run() {
				createInstallation(arg0, arg1);

			}
		});

	}

	@Override
	protected void onUnregistered(Context arg0, String arg1) {
		Log.i(TAG,arg1);
	}

	public void createInstallation(final Context context, String deviceToken){

		/*
		 * BuiltInstallation is register user device with channel on built.io for GCM Notification
		 * 
		 * There are many type of channel get use which trigger with event
		 * Here eg. "classUid.object.create" which is show notification when inside class object created notification will receive.
		 * For more refer : http://static.built.io/downloads/sdk-docs/android-docs/com/raweng/built/BuiltInstallation.html#createInstallation(java.lang.String, java.util.ArrayList, com.raweng.built.BuiltResultCallBack)
		 * 
		 */
		BuiltInstallation install = new BuiltInstallation();

		ArrayList<String> installationChannels = new ArrayList<String>();
		installationChannels.add("userphoto.object.create");
		installationChannels.add("pushNotification");

		install.createInstallation(deviceToken, installationChannels, new BuiltResultCallBack() {

			@Override
			public void onSuccess() {

				/// installation is created successfully.
				Toast.makeText(context, "New Installation created successfully...", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onError(BuiltError builtErrorObject) {

				/// builtErrorObject contains more details of error.
				Toast.makeText(context, "error :"+builtErrorObject.getErrorMessage() , Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onAlways() {
				/// write code here that user want to execute.
				/// regardless of success or failure of the operation.
			}
		});
	}


}
