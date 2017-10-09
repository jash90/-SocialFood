package com.zimny.socialfood.model;

import java.util.ArrayList;

/**
 * Created by ideo7 on 17.08.2017.
 */

public class Restaurant extends Info {
    private ArrayList<Food> foods;
    private String uid;
    private ArrayList<Tag> tags;
    private String name;
    private String imageUpload;

    public Restaurant() {
        this.setAddress(new Address());
    }

    public Restaurant(ArrayList<Food> foods, Address address, int phone, String uid, ArrayList<Tag> tags, String name, String imageUpload) {
        this.foods = foods;
        super.setAddress(address);
        super.setPhone(phone);
        this.uid = uid;
        this.tags = tags;
        this.name = name;
        this.imageUpload = imageUpload;
    }

    public Restaurant(String name, int phone, String imageUpload) {
        super.setPhone(phone);
        this.name = name;
        this.imageUpload = imageUpload;
    }

    public ArrayList<Food> getFoods() {
        return foods;
    }

    public void setFoods(ArrayList<Food> foods) {
        this.foods = foods;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public ArrayList<Tag> getTags() {
        return tags;
    }

    public void setTags(ArrayList<Tag> tags) {
        this.tags = tags;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUpload() {
        return imageUpload;
    }

    public void setImageUpload(String imageUpload) {
        this.imageUpload = imageUpload;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Restaurant)) return false;
        Restaurant that = (Restaurant) o;
        return uid != null && uid.equals(that.uid);
    }


    @Override
    public int hashCode() {
        int result = foods != null ? foods.hashCode() : 0;
        result = 31 * result + (super.getAddress() != null ? super.getAddress().hashCode() : 0);
        result = 31 * result + super.getPhone();
        result = 31 * result + (uid != null ? uid.hashCode() : 0);
        result = 31 * result + (tags != null ? tags.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (imageUpload != null ? imageUpload.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return String.format("%s %s", name, getAddress());
    }

}
