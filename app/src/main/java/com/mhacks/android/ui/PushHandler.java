package com.mhacks.android.ui;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.parse.ParsePushBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

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
        intent.setClass(context, MainActivity.class);
        intent.setAction(MainActivity.class.getName());
        intent.setFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        context.startActivity(intent);
    }

    @Override
    protected Class<? extends Activity> getActivity(Context context, Intent intent) {
        return super.getActivity(context, intent);
    }
}
