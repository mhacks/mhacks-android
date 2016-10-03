package com.mhacks.android.data.network;

/**
 * Created by boztalay on 6/3/15.
 * Updated by omkarmoghe on 10/2/16
 */
public class LoginParams {
    public String email;
    public String password;
    public String token;
    public Boolean isGcm = true;

    public LoginParams(String email, String password, String token) {
        this.email = email;
        this.password = password;
        this.token = token;
    }
}
