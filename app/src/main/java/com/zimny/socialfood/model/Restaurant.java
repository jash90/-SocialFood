package com.zimny.socialfood.model;

import java.util.List;

/**
 * Created by ideo7 on 17.08.2017.
 */

public class Restaurant {
    private List<Food> foods;
    private Address address;
    private int phone;
    private String uid;
    private List<Tag> tags;

    public List<Food> getFoods() {
        return foods;
    }

    public void setFoods(List<Food> foods) {
        this.foods = foods;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public Restaurant() {
    }

    public Restaurant(List<Food> foods, Address address, int phone, String uid, List<Tag> tags) {
        this.foods = foods;
        this.address = address;
        this.phone = phone;
        this.uid = uid;
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "foods=" + foods +
                ", address=" + address +
                ", phone=" + phone +
                ", uid='" + uid + '\'' +
                ", tags=" + tags +
                '}';
    }
}
