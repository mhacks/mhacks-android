package com.mhacks.android.data.model;

/**
 * Created by boztalay on 6/3/15.
 */
public class User extends ModelObject {
    public String firstName;
    public String lastName;
    public String uid; // user's email address
    /*
    0 - hacker (can’t do anything whatsoever)
        Can continue as guest
    1 - sponsor (can make requests + need approvals)
    2 - organizer (can make requests + need approvals)
    3 - admin (do whatever the fuck they want)
     */
    public int role;

    public User() {
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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }
}
