package com.buitio.builtpushnotification;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

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
//public class GCMIntentService extends GCMBaseIntentService {
//
//	private CharSequence data;
//	Handler handler = new Handler();
//
//	public GCMIntentService(){
//		super("test");
//	}
//
//	protected GCMIntentService(String senderId) {
//		super(senderId);
//		Log.i(TAG,senderId);
//	}
//
//	@Override
//	protected void onError(Context arg0, String arg1) {
//		Log.i(TAG,arg1);
//	}
//
//	@SuppressWarnings("deprecation")
//	@Override
//	protected void onMessage(Context context, Intent arg1) {
//		Intent notificationIntent;
//		Notification notification;
//		PendingIntent contentIntent;
//
//		/*
//		 * Notification design is define.
//		 */
//		data = arg1.getStringExtra("message");
//		Toast.makeText(context, arg1.getStringExtra("message"), Toast.LENGTH_LONG).show();
//		NotificationManager nManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
//		notificationIntent = new Intent(context, NotificationAlert.class);
//		notificationIntent.putExtra("message", data);
//		notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);
//		notification = new Notification(R.drawable.ic_launcher,data,System.currentTimeMillis());
//		notification.setLatestEventInfo(context,"Notification",data,contentIntent);
//		notification.flags = Notification.FLAG_AUTO_CANCEL;
//		notification.sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//		nManager.notify(0, notification);
//
//	}
//
//	@Override
//	protected void onRegistered(final Context arg0, final String arg1) {
//
//		Toast.makeText(arg0, arg1.toString(), Toast.LENGTH_LONG).show();
//
//		handler.post(new Runnable() {
//			@Override
//			public void run() {
//				createInstallation(arg0, arg1);
//
//			}
//		});
//
//	}
//
//	@Override
//	protected void onUnregistered(Context arg0, String arg1) {
//		Log.i(TAG,arg1);
//	}
//
//	public void createInstallation(final Context context, String deviceToken){
//
//		/*
//		 * BuiltInstallation is register user device with channel on built.io for GCM Notification
//		 *
//		 * There are many type of channel get use which trigger with event
//		 * Here eg. "classUid.object.create" which is show notification when inside class object created notification will receive.
//		 * For more refer : http://static.built.io/downloads/sdk-docs/android-docs/com/raweng/built/BuiltInstallation.html#createInstallation(java.lang.String, java.util.ArrayList, com.raweng.built.BuiltResultCallBack)
//		 *
//		 */
//		BuiltInstallation install = new BuiltInstallation();
//
//		ArrayList<String> installationChannels = new ArrayList<String>();
//		installationChannels.add("userphoto.object.create");
//		installationChannels.add("pushNotification");
//
//		install.createInstallation(deviceToken, installationChannels, new BuiltResultCallBack() {
//
//			@Override
//			public void onSuccess() {
//
//				/// installation is created successfully.
//				Toast.makeText(context, "New Installation created successfully...", Toast.LENGTH_SHORT).show();
//			}
//
//			@Override
//			public void onError(BuiltError builtErrorObject) {
//
//				/// builtErrorObject contains more details of error.
//				Toast.makeText(context, "error :"+builtErrorObject.getErrorMessage() , Toast.LENGTH_SHORT).show();
//			}
//
//			@Override
//			public void onAlways() {
//				/// write code here that user want to execute.
//				/// regardless of success or failure of the operation.
//			}
//		});
//	}
//


public class GCMIntentService extends IntentService {
    public static final int NOTIFICATION_ID = 1;
    private static final String TAG = GCMIntentService.class.getName();
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    public GCMIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that GCM
             * will be extended in the future with new message types, just ignore
             * any message types you're not interested in, or that you don't
             * recognize.
             */
            if (GoogleCloudMessaging.
                    MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                sendNotification("Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_DELETED.equals(messageType)) {
                sendNotification("Deleted messages on server: " +
                        extras.toString());
                // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                // This loop represents the service doing some work.
                for (int i=0; i<5; i++) {
                    Log.i(TAG, "Working... " + (i+1)
                            + "/5 @ " + SystemClock.elapsedRealtime());
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                    }
                }
                Log.i(TAG, "Completed work @ " + SystemClock.elapsedRealtime());
                // Post notification of received message.
                sendNotification("Received: " + extras.toString());
                Log.i(TAG, "Received: " + extras.toString());
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotification(String msg) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, NotificationActivity.class), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_builtio_close)
                        .setContentTitle("GCM Notification")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg))
                        .setContentText(msg);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}