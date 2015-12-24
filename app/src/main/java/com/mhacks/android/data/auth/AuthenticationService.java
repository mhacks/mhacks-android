package com.mhacks.android.data.auth;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by damian on 2/4/15.
 */
public class AuthenticationService extends Service {
  public static final String TAG = AuthenticationService.class.getSimpleName();

  private final Authenticator mAuthenticator = new Authenticator(this);

  @Override
  public IBinder onBind(Intent intent) {
    return mAuthenticator.getIBinder();
  }

}
