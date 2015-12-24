package com.mhacks.android.data.auth;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.arbrr.onehack.R;

/**
 * Created by damian on 2/4/15.
 */
public class AuthenticationActivity extends AccountAuthenticatorActivity implements View.OnClickListener {
  public static final String TAG = AuthenticationActivity.class.getSimpleName();
  public static final String TOKEN_TYPE = "api";

  public static final String EXTRA_ACCOUNT_TYPE = TAG + ":AccountType";
  public static final String EXTRA_TOKEN_TYPE = TAG + ":AuthType";

  private String mAccountType;
  private String mTokenType;

  private EditText mEmail;
  private EditText mPassword;
  private Button mLogInButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_authentication);

    final Intent intent = getIntent();
    mAccountType = intent.getStringExtra(EXTRA_ACCOUNT_TYPE);
    mTokenType = intent.getStringExtra(EXTRA_TOKEN_TYPE);
    if (TextUtils.isEmpty(mTokenType)) mTokenType = TOKEN_TYPE;

    mEmail = (EditText) findViewById(R.id.auth_email);
    mPassword = (EditText) findViewById(R.id.auth_password);
    mLogInButton = (Button) findViewById(R.id.auth_button);

    mLogInButton.setOnClickListener(this);
  }

  @Override
  public void onClick(View v) {
    final String email = mEmail.getText().toString();
    final String password = mPassword.getText().toString();
    if (!validate(email, password)) return;

    final ProgressDialog dialog = ProgressDialog.show(this, getString(R.string.logging_in), null, false, false);
    new AuthenticationTask(this, email, password) {
      @Override
      protected void onPostExecute(String token) {
        dialog.dismiss();
        if (token == null) {
          Toast.makeText(AuthenticationActivity.this, R.string.error_log_in, Toast.LENGTH_SHORT).show();
          return;
        }
        finishAuthentication(mEmail, token);
      }
    }.execute();
  }

  private boolean validate(final String email, final String password) {
    boolean result = true;
    if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
      mEmail.setError(getString(R.string.error_invalid_email));
      result = false;
    }
    if (TextUtils.isEmpty(password)) {
      mPassword.setError(getString(R.string.error_invalid_password));
      result = false;
    }
    return result;
  }

  private void finishAuthentication(String email, String token) {
    final AccountManager accountManager = AccountManager.get(this);
    final Account account = new Account(email, mAccountType);
    accountManager.addAccountExplicitly(account, null, null);
    accountManager.setAuthToken(account, mTokenType, token);

    final Intent intent = new Intent();
    intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, mAccountType);
    intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, email);
    intent.putExtra(AccountManager.KEY_AUTHTOKEN, token);

    setAccountAuthenticatorResult(intent.getExtras());
    setResult(RESULT_OK, intent);
    finish();
  }
}
