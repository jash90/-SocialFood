package com.zimny.socialfood.model;

/**
 * Created by ideo7 on 17.08.2017.
 */

public class Tag {
    private String name;
    private String uid;

    public Tag() {
    }

    public Tag(String name, String uid) {
        this.name = name;
        this.uid = uid;
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

    @Override
    public String toString() {
        return String.format("%s",name);
    }
}
