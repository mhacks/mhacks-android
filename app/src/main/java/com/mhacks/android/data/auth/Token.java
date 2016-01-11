package com.mhacks.android.data.auth;

import java.util.Date;

/**
 * Created by Omkar Moghe on 1/10/2016.
 */
public class Token {
    private String access_token;
    private String client;
    private Date   expiry;
    private String uid;
    private String token_type;

    public Token(String access_token,
                 String client,
                 Date expiry,
                 String uid,
                 String token_type) {
        this.access_token = access_token;
        this.client = client;
        this.expiry = expiry;
        this.uid = uid;
        this.token_type = token_type;
    }

    public Token() {
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public Date getExpiry() {
        return expiry;
    }

    public void setExpiry(Date expiry) {
        this.expiry = expiry;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }
}
