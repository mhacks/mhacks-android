package com.mhacks.android.ui;

/**
 * Created by Damian Wieczorek <damianw@umich.edu> on 7/30/14.
 */

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.bugsnag.android.Bugsnag;
import com.google.common.collect.ImmutableList;
import com.mhacks.android.R;
import com.mhacks.android.data.model.User;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseTwitterUtils;
import com.parse.ParseUser;

import java.util.Random;


public class LoginActivity extends Activity implements View.OnClickListener {
  public static final String TAG = "LoginActivity";

  public static final int[] BACKGROUNDS = {
    R.drawable.poly1,
    R.drawable.poly2,
    R.drawable.poly3,
    R.drawable.poly4,
    R.drawable.poly5,
    R.drawable.poly6,
    R.drawable.poly7,
    R.drawable.poly8,
    R.drawable.poly9,
    R.drawable.poly10,
    R.drawable.poly11,
    R.drawable.poly12,
    R.drawable.poly13,
    R.drawable.poly14,
    R.drawable.poly15,
    R.drawable.poly16,
    R.drawable.poly17,
    R.drawable.poly18,
    R.drawable.poly19,
    R.drawable.poly20,
    R.drawable.poly21
  };

  public static final ImmutableList<String> FB_PERMISSIONS = ImmutableList.<String>builder()
    .add("public_profile")
    .add("user_friends")
    .add("user_about_me")
    .add("user_relationships")
    .add("user_birthday")
    .add("user_location").build();

  private Button mFacebookButton;
  private Button mTwitterButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.activity_login);

    User.logOut();

    mFacebookButton = (Button) findViewById(R.id.facebook_login_button);
    mTwitterButton = (Button) findViewById(R.id.twitter_login_button);

    mFacebookButton.setOnClickListener(this);
    mTwitterButton.setOnClickListener(this);
  }

  @Override
  protected void onResume() {
    super.onResume();
    ((ImageView)findViewById(R.id.login_background)).setImageResource(BACKGROUNDS[new Random().nextInt(BACKGROUNDS.length)]);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
  }

  @Override
  public void onClick(View view) {
    final ProgressDialog dialog = new ProgressDialog(this);
    dialog.setIndeterminate(true);
    dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    dialog.setCancelable(false);
    dialog.setMessage(getString(R.string.logging_in));
    dialog.show();

    switch (view.getId()) {
      case R.id.facebook_login_button:
        ParseFacebookUtils.logIn(FB_PERMISSIONS, this, new LogInCallback(dialog, false));
        break;
      case R.id.twitter_login_button:
        ParseTwitterUtils.logIn(this, new LogInCallback(dialog, true));
        break;
    }

  }

  private class LogInCallback extends com.parse.LogInCallback {
    private final Dialog mmDialog;
    private final boolean mmTwitter;

    public LogInCallback(Dialog dialog, boolean twitter) {
      mmDialog = dialog;
      mmTwitter = twitter;
    }

    @Override
    public void done(final ParseUser parseUser, ParseException e) {
      if (parseUser == null) {
        error(e != null ? e : new ParseException(ParseException.USERNAME_MISSING, "Login failed"));
        return;
      }

      if (mmTwitter) {
        ((User) parseUser).new TwitterFetchTask() {
          @Override
          protected void onPostExecute(Exception e) {
            super.onPostExecute(e);
            if (e != null) {
              error(e);
              return;
            }
            success(parseUser);
          }
        }.execute();
      }
      else {
        success(parseUser);
      }

    }

    private void error(Exception e) {
      Log.d(TAG, "Login failed.");
      Bugsnag.notify(e);
      mmDialog.cancel();
    }

    private void success(ParseUser user) {
      mmDialog.dismiss();
      finish();
      startActivity(new Intent(LoginActivity.this, MainActivity.class));
    }
  }

}
