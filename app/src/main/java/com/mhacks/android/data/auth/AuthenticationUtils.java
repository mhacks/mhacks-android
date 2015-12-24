package com.mhacks.android.data.auth;

import android.accounts.AuthenticatorException;
import android.content.Context;
import android.net.Uri;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by damian on 2/4/15.
 */
public abstract class AuthenticationUtils {
  public static final String TAG = AuthenticationUtils.class.getSimpleName();

  public static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");
  public static final String SESSIONS = "sessions";
  public static final String TOKEN = "token";

  public static final String EMAIL = "email";
  public static final String PASSWORD = "password";

  private AuthenticationUtils() {}

  public static String authenticate(Context context, String email, String password) throws AuthenticatorException {
    final Uri endpoint = ApiUtils.getEndpoint(context).buildUpon().appendPath(SESSIONS).build();

    // construct JSON for request
    final JSONObject jsonObject;
    try {
      jsonObject = new JSONObject()
        .put(EMAIL, email)
        .put(PASSWORD, password);
    } catch (JSONException e) {
      throw new AuthenticatorException("Failed to construct auth JSON", e);
    }

    // make the request
    final OkHttpClient client = new OkHttpClient();
    final RequestBody body = RequestBody.create(MEDIA_TYPE, jsonObject.toString());
    final Request request = new Request.Builder()
      .url(endpoint.toString())
      .post(body)
      .build();

    // grab the response
    final String response;
    try {
      response = client.newCall(request).execute().body().string();
    } catch (IOException e) {
      throw new AuthenticatorException("Failed to POST to auth endpoint", e);
    }

    // parse the response JSON and extract the token
    try {
      return new JSONObject(response).getString(TOKEN);
    } catch (JSONException e) {
      throw new AuthenticatorException("Failed to parse response JSON", e);
    }
  }
}
