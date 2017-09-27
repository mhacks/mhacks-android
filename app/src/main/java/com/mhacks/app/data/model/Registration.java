package com.mhacks.app.data.model;

/**
 * Created by Riyu on 6/18/16.
 */
public class Registration {
    public String name;
    public String email;
    public int age;
    public String school;
    public boolean mentor_status;

    public Registration() {
    }

    public String getName() {return name;}

    public String getEmail() {return email;}

    public String getSchool() {return school;}

    public int age() {return age;}

    public boolean getMentorStatus() {return mentor_status;}
}
