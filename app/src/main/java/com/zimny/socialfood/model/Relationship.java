package com.zimny.socialfood.model;

/**
 * Created by ideo7 on 17.08.2017.
 */

public class Relationship {
    private String uidFriend1;
    private String uidFriend2;
    private Boolean invite;

    public Relationship() {
    }

    public Relationship(String uidFriend1, String uidFriend2, Boolean invite) {
        this.uidFriend1 = uidFriend1;
        this.uidFriend2 = uidFriend2;
        this.invite = invite;
    }

    public String getUidFriend1() {
        return uidFriend1;
    }

    public void setUidFriend1(String uidFriend1) {
        this.uidFriend1 = uidFriend1;
    }

    public String getUidFriend2() {
        return uidFriend2;
    }

    public void setUidFriend2(String uidFriend2) {
        this.uidFriend2 = uidFriend2;
    }

    public Boolean getInvite() {
        return invite;
    }

    public void setInvite(Boolean invite) {
        this.invite = invite;
    }

    @Override
    public String toString() {
        return "Relationship{" +
                "uidFriend1='" + uidFriend1 + '\'' +
                ", uidFriend2='" + uidFriend2 + '\'' +
                ", invite=" + invite +
                '}';
    }
}
