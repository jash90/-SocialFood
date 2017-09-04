package com.zimny.socialfood.model;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.List;


public class Group {
    private String uid;
    private ArrayList<User> users;
    private Address address;
    private String uidAdmin;
    private List<Tag> tags;
    private String name;

    public Group() {
    }

    public Group(String uid, ArrayList<User> users, Address address, String uidAdmin, List<Tag> tags, String name) {
        this.uid = uid;
        this.users = users;
        this.address = address;
        this.uidAdmin = uidAdmin;
        this.tags = tags;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Exclude
    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
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

    @Exclude
    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return String.format("%s %s %s %s", name, address, users, tags);
    }
}
