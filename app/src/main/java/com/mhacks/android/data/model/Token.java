package com.mhacks.android.data.model;

/**
 * Created by Omkar Moghe on 2/18/2016.
 */
public class Token {
    public String token;
    public int preferences;
    public boolean isGcm = true;

    public Token() {
    }

    public Token(String token, int preferences) {
        this.token = token;
        this.preferences = preferences;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getPreferences() {
        return preferences;
    }

    public void setPreferences(int preferences) {
        this.preferences = preferences;
    }

    public boolean isGcm() {
        return isGcm;
    }
}
