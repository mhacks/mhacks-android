package com.mhacks.android.ui;

/**
 * Created by Damian Wieczorek <damianw@umich.edu> on 7/30/14.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mhacks.android.R;
import com.mhacks.android.data.model.Installation;
import com.mhacks.android.data.model.User;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends Activity implements
  TextView.OnEditorActionListener, View.OnClickListener {

  private EditText mUsername;
  private EditText mPassword;
  private Button mLoginButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    User.logOut();

    mUsername = (EditText) findViewById(R.id.username_field);
    mPassword = (EditText) findViewById(R.id.password_field);
    mLoginButton = (Button) findViewById(R.id.login_button);

    mPassword.setOnEditorActionListener(this);
    mLoginButton.setOnClickListener(this);
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

    ParseUser.logInInBackground(mUsername.getText().toString(), mPassword.getText().toString(), new LogInCallback() {
      @Override
      public void done(ParseUser parseUser, ParseException e) {
        if (e != null) {
          e.printStackTrace();
          Toast.makeText(LoginActivity.this, R.string.error_logging_in, Toast.LENGTH_SHORT).show();
          dialog.cancel();
          return;
        }
        dialog.dismiss();
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        Installation.getCurrentInstallation().setCurrentUser().saveEventually();
        finish();
      }
    });
  }

}
