package com.mhacks.android.data.network;

/**
 * Created by boztalay on 6/3/15.
 */
public class LoginParams {
    public String email;
    public String password;
    //public String gcm_token;

    public LoginParams(String email, String password) {
        this.email = email;
        this.password = password;
        //this.gcm_token = gcm_token;
    }
}
