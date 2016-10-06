package com.mhacks.android.data.model;

/**
 * Created by Omkar Moghe on 10/4/2016.
 */

public class Login {
    public String token;
    public User user;

    public Login() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
