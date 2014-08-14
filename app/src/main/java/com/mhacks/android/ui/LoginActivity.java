package com.mhacks.android.ui;

/**
 * Created by Damian Wieczorek <damianw@umich.edu> on 7/30/14.
 */

import android.app.Activity;
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
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
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

  private Button mLoginButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.activity_login);

    User.logOut();

    mLoginButton = (Button) findViewById(R.id.login_button);
    mLoginButton.setOnClickListener(this);
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
    attemptLogin();
  }

  private void attemptLogin() {
    final ProgressDialog dialog = new ProgressDialog(this);
    dialog.setIndeterminate(true);
    dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    dialog.setCancelable(false);
    dialog.setMessage(getString(R.string.logging_in));
    dialog.show();

    ParseFacebookUtils.logIn(FB_PERMISSIONS, this, new LogInCallback() {
      @Override
      public void done(ParseUser user, ParseException e) {
        if (e != null) {
          Log.d(TAG, "Facebook login failed.");
          Bugsnag.notify(e);
          return;
        }
        finish();
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
      }
    });
  }

}
