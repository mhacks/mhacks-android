package com.mhacks.android.ui;

/**
 * Created by Damian Wieczorek <damianw@umich.edu> on 7/30/14.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mhacks.android.R;
import com.mhacks.android.data.model.User;

import java.util.Random;

public class LoginActivity extends Activity implements
  TextView.OnEditorActionListener, View.OnClickListener {

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
  public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
    if (i != EditorInfo.IME_ACTION_DONE) return false;
    attemptLogin();
    return true;
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

//    ParseUser.logInInBackground(mUsername.getText().toString(), mPassword.getText().toString(), new LogInCallback() {
//      @Override
//      public void done(ParseUser parseUser, ParseException e) {
//        if (e != null) {
//          e.printStackTrace();
//          Toast.makeText(LoginActivity.this, R.string.error_logging_in, Toast.LENGTH_SHORT).show();
//          dialog.cancel();
//          return;
//        }
//        dialog.dismiss();
//        startActivity(new Intent(LoginActivity.this, MainActivity.class));
//        Installation.getCurrentInstallation().setCurrentUser().saveEventually();
//        finish();
//      }
//    });
  }

}
