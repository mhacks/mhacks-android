package com.mhacks.android.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.parse.ParsePushBroadcastReceiver;

/**
 * Created by Omkar Moghe on 1/12/2015.
 *
 * Custom ParsePushBroadcastReciever to handle JSON payloads sent via push notifications from Parse.
 */
public class PushHandler extends ParsePushBroadcastReceiver {

    public static final String TAG = "PushHandler";

    @Override
    protected void onPushReceive(Context context, Intent intent) {
        super.onPushReceive(context, intent);
        Log.d(TAG, "push received");
    }

    @Override
    protected void onPushDismiss(Context context, Intent intent) {
        super.onPushDismiss(context, intent);
        Log.d(TAG, "push dismissed");
    }

    @Override
    protected void onPushOpen(Context context, Intent intent) {
        super.onPushOpen(context, intent);
        Log.d(TAG, "push opened");
    }
}
