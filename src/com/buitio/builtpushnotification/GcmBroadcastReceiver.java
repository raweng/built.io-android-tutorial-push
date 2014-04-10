package com.buitio.builtpushnotification;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

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
public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {

	@Override
    public void onReceive(Context context, Intent intent) {
		 // Explicitly specify that GcmIntentService will handle the intent.
        ComponentName comp = new ComponentName(context.getPackageName(), GCMIntentService.class.getName());
        // Start the service, keeping the device awake while it is launching.
        startWakefulService(context, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);
    }

}