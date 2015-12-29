package com.mhacks.android.data.model;

/**
 * Created by boztalay on 6/3/15.
 */
public class HackerRole extends ModelObject {
    public int user_id;
    public int hackathon_id;
    public int role;

    public enum Role {
        HACKER(1),
        ORGANIZER(2),
        SPONSOR(4),
        VOLUNTEER(8);

        private int bitMask;

        Role(int bitMask) {
            this.bitMask = bitMask;
        }

        boolean isRoleSelf(int role) {
            return ((role & bitMask) == bitMask);
        }
    }

    public HackerRole() {
    }

    public boolean isHacker() {
        return Role.HACKER.isRoleSelf(role);
    }

    public boolean isOrganizer() {
        return Role.ORGANIZER.isRoleSelf(role);
    }

    public boolean isSponsor() {
        return Role.SPONSOR.isRoleSelf(role);
    }

    public boolean isVolunteer() {
        return Role.VOLUNTEER.isRoleSelf(role);
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getHackathon_id() {
        return hackathon_id;
    }

    public void setHackathon_id(int hackathon_id) {
        this.hackathon_id = hackathon_id;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }
}
