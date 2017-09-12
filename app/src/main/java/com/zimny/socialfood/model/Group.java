package com.zimny.socialfood.model;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.List;


public class Group extends Info{
    private String uid;
    private ArrayList<User> users;
    private String uidAdmin;
    private ArrayList<Tag> tags;
    private String name;
    private String imageUpload;

    public Group() {
    }

    public Group(String uid, ArrayList<User> users, Address address, String uidAdmin, ArrayList<Tag> tags, String name, String imageUpload,int phone) {
        this.uid = uid;
        this.users = users;
        super.setAddress(address);
        super.setPhone(phone);
        this.uidAdmin = uidAdmin;
        this.tags = tags;
        this.name = name;
        this.imageUpload = imageUpload;
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

    public String getUidAdmin() {
        return uidAdmin;
    }

    public void setUidAdmin(String uidAdmin) {
        this.uidAdmin = uidAdmin;
    }

    public String getImageUpload() {
        return imageUpload;
    }

    public void setImageUpload(String imageUpload) {
        this.imageUpload = imageUpload;
    }

    @Exclude
    public ArrayList<Tag> getTags() {
        return tags;
    }

    public void setTags(ArrayList<Tag> tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return String.format("%s %s %s %s", name, getAddress(), users, tags);
    }
}
