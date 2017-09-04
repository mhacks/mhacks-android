package com.mhacks.android.data.model;

/**
 * Created by omkarmoghe on 10/2/16.
 */
public class User extends ModelObject {
    public String name;
    public String school;
    public String email;
    public boolean canEditAnnouncements;
    public boolean canPostAnnouncements;
    public boolean canPerformScan;

    public User() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isCanEditAnnouncements() {
        return canEditAnnouncements;
    }

    public void setCanEditAnnouncements(boolean canEditAnnouncements) {
        this.canEditAnnouncements = canEditAnnouncements;
    }

    public boolean isCanPostAnnouncements() {
        return canPostAnnouncements;
    }

    public void setCanPostAnnouncements(boolean canPostAnnouncements) {
        this.canPostAnnouncements = canPostAnnouncements;
    }

    public boolean isCanPerformScan() {
        return canPerformScan;
    }

    public void setCanPerformScan(boolean canPerformScan) {
        this.canPerformScan = canPerformScan;
    }
}
