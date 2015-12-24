package com.mhacks.android.data.model;

/**
 * Created by boztalay on 6/3/15.
 */
public class User extends ModelObject {
    public String gcm_token;
    public String email;
    public String password;
    public String firstName;
    public String lastName;
    public String company;
    public String twitter;
    public boolean admin;

    public String getGcm_token() {
        return gcm_token;
    }

    public void setGcm_token(String gcm_token) {
        this.gcm_token = gcm_token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
}
