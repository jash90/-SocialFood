package com.zimny.socialfood.model;

import java.util.ArrayList;

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

    public Tag(String name) {
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

    @Override
    public String toString() {
        return String.format("%s", name);
    }
    public static ArrayList<String> getStringTags(ArrayList<Tag> tags){
        ArrayList <String> tagString = new ArrayList<>();
        for (Tag tag : tags){
            tagString.add(tag.getName());
        }
        return tagString;
    }
}
