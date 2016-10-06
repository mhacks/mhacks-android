package com.mhacks.android.data.network;

/**
 * Created by boztalay on 6/3/15.
 * Updated by omkarmoghe on 10/2/16
 */
public class LoginParams {
    public String username;
    public String password;
    public String token = "";
    public boolean isGcm = true;

    public LoginParams() {
    }

    public LoginParams(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isGcm() {
        return isGcm;
    }

    public void setGcm(boolean gcm) {
        isGcm = gcm;
    }
}
