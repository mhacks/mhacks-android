package com.mhacks.android.data.auth;

import android.accounts.AuthenticatorException;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by damian on 2/4/15.
 */
public class AuthenticationTask extends AsyncTask<Void, Void, String> {
  public static final String TAG = AuthenticationTask.class.getSimpleName();

  private final Context mContext;
  protected final String mEmail;
  private final String mPassword;

  public AuthenticationTask(Context context, final String email, final String password) {
    mContext = context;
    mEmail = email;
    mPassword = password;
  }

  @Override
  protected String doInBackground(Void... params) {
    try {
      return AuthenticationUtils.authenticate(mContext, mEmail, mPassword);
    } catch (AuthenticatorException e) {
      Log.e(TAG, "Failed to authenticate", e);
      return null;
    }
  }
}
