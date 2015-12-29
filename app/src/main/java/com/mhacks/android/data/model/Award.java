package com.mhacks.android.data.model;

/**
 * Created by boztalay on 6/3/15.
 */
public class Award extends ModelObject {
    public String name;
    public String prize;
    public String info;
    public int hackathon_id;
    public String company;

    public Award() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrize() {
        return prize;
    }

    public void setPrize(String prize) {
        this.prize = prize;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getHackathon_id() {
        return hackathon_id;
    }

    public void setHackathon_id(int hackathon_id) {
        this.hackathon_id = hackathon_id;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
}
