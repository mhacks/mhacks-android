package com.mhacks.android.data.model;

import java.util.Date;
import java.util.List;

/**
 * Created by boztalay on 6/3/15.
 */
public class Hackathon extends ModelObject {
    public String name;
    public String info;
    public Date startTime;
    public Date endTime;
    public String logoUrl;
    public String hexColor;
    public List<HackerRole> hacker_roles;

    public Hackathon() {
    }

    public boolean isUserHacker(User user) {
        HackerRole hackerRole = getHackerRole();
        return hackerRole.isHacker();
    }

    public boolean isUserOrganizer(User user) {
        HackerRole hackerRole = getHackerRole();
        return hackerRole.isOrganizer();
    }

    public boolean isUserSponsor(User user) {
        HackerRole hackerRole = getHackerRole();
        return hackerRole.isSponsor();
    }

    public boolean isUserVolunteer(User user) {
        HackerRole hackerRole = getHackerRole();
        return hackerRole.isVolunteer();
    }

    public HackerRole getHackerRole() {
        if(hacker_roles.size() <= 0) {
            // This should never happen
            throw new RuntimeException("A hackathon was asked for its user role but didn't have any!");
        }

        return hacker_roles.get(0);
    }

    public String getHexColor() {
        return hexColor;
    }

    public void setHexColor(String hexColor) {
        this.hexColor = hexColor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public List<HackerRole> getHackerRoles() {
        return hacker_roles;
    }
}
