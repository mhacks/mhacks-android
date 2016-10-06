package com.mhacks.android.data.model;

/**
 * Created by Omkar Moghe on 2/18/2016.
 */
public class Token {
    public String name;
    public String registrationId;
    public boolean active = true;

    public Token() {
    }

    public Token(String registrationId) {
        this.registrationId = registrationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
