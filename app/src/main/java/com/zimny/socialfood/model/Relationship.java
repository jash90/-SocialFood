package com.zimny.socialfood.model;

/**
 * Created by ideo7 on 17.08.2017.
 */

public class Relationship {
    private String uid;
    private String uidFriend1;
    private String uidFriend2;

    public Relationship() {
    }

    public Relationship(String uid, String uidFriend1, String uidFriend2) {
        this.uid = uid;
        this.uidFriend1 = uidFriend1;
        this.uidFriend2 = uidFriend2;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    @Override
    public String toString() {
        return "Relationship{" +
                "uid='" + uid + '\'' +
                ", uidFriend1='" + uidFriend1 + '\'' +
                ", uidFriend2='" + uidFriend2 + '\'' +
                '}';
    }
}
