package com.zimny.socialfood.model;

import java.util.List;

/**
 * Created by ideo7 on 17.08.2017.
 */

public class Group {
    private String uid;
    private List<User> users;
    private Address address;
    private String uidAdmin;
    private List<Tag> tags;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getUidAdmin() {
        return uidAdmin;
    }

    public void setUidAdmin(String uidAdmin) {
        this.uidAdmin = uidAdmin;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public Group() {
    }

    public Group(String uid, List<User> users, Address address, String uidAdmin, List<Tag> tags) {
        this.uid = uid;
        this.users = users;
        this.address = address;
        this.uidAdmin = uidAdmin;
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "Group{" +
                "uid='" + uid + '\'' +
                ", users=" + users +
                ", address=" + address +
                ", uidAdmin='" + uidAdmin + '\'' +
                ", tags=" + tags +
                '}';
    }
}
