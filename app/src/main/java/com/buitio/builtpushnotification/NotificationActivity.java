package com.buitio.builtpushnotification;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.raweng.built.Built;
import com.raweng.built.BuiltApplication;
import com.raweng.built.BuiltError;
import com.raweng.built.BuiltInstallation;
import com.raweng.built.BuiltResultCallBack;
import com.raweng.built.utilities.BuiltConstant;

import java.io.IOException;
import java.util.ArrayList;

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

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String PROPERTY_APP_VERSION = "appVersion";
    public static final String PROPERTY_REG_ID = "registration_id";

    private static String SENDER_ID = "710786587727";
    private GoogleCloudMessaging gcm;

    private Context context;
    String regid;

    private static String TAG = "NotificationActivity";
    private BuiltApplication builtApplication;


    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


        /*
         * New implementation here.
         */
        context = getApplicationContext();
        if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(this);
            regid = getRegistrationId(context);

            if (regid.isEmpty()) {
                registerInBackground();
            }
        } else {
            Log.i(TAG, "No valid Google Play Services APK found.");
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
        try
        {
            builtApplication = Built.application(context , "blte7dde07ab26ae90e");

            BuiltInstallation installation = builtApplication.installation();

            ArrayList<String> installationChannels = new ArrayList<String>();
            installationChannels.add("userphoto.object.create");
            installationChannels.add("pushNotification");

            installation.createInstallationInBackground(deviceToken, null, new BuiltResultCallBack() {

//            @Override
//            public void onSuccess() {
//
//                /// installation is created successfully.
//            }
//
//            @Override
//            public void onError(BuiltError builtErrorObject) {
//
//                /// builtErrorObject contains more details of error.
//            }
//
//            @Override
//            public void onAlways() {
//
//                /// write code here that user want to execute.
//                /// regardless of success or failure of the operation.
//                finish();
//            }

                @Override
                public void onCompletion(BuiltConstant.ResponseType responseType, BuiltError builtError) {

                    if (builtError == null){
                        Toast.makeText(NotificationActivity.this, "Installation created successfully...", Toast.LENGTH_SHORT).show();
                        storeRegistrationId(context, regid);
                    }else {
                        Toast.makeText(NotificationActivity.this, "error :" + builtError.getErrorMessage(), Toast.LENGTH_SHORT).show();
                    }

                    finish();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }


	}

    private boolean checkPlayServices() {
        int resultCode =  GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing registration ID is not guaranteed to work with
        // the new app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    /**
     * @return Application's {@code SharedPreferences}.
     */
    private SharedPreferences getGCMPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the registration ID in your app is up to you.
        return getSharedPreferences(NotificationActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }

    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }



    /**
     * Registers the application with GCM servers asynchronously.
     * <p>
     * Stores the registration ID and app versionCode in the application's
     * shared preferences.
     */
    private void registerInBackground() {
        new AsyncTask() {

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);

                Log.d(TAG , "----------onPostExecute----registration id---------" +o.toString());
            }

            @Override
            protected Object doInBackground(Object[] params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regid = gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + regid;

                    // You should send the registration ID to your server over HTTP,
                    // so it can use GCM/HTTP or CCS to send messages to your app.
                    // The request to your server should be authenticated if your app
                    // is using accounts.
                    sendRegistrationIdToBackend();

                    // For this demo: we don't need to send it because the device
                    // will send upstream messages to a server that echo back the
                    // message using the 'from' address in the message.

                    // Persist the registration ID - no need to register again.
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                return msg;

            }
        }.execute(null, null, null);
    }

    private void storeRegistrationId(Context context, String regid) {

        final SharedPreferences prefs = getGCMPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regid);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }

    private void sendRegistrationIdToBackend() {

        //Send registration id to built.
        createInstallation(regid);
    }
}
