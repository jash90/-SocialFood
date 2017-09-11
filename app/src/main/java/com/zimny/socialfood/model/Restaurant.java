package com.zimny.socialfood.model;

import java.util.ArrayList;
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
    private String name;
    private String imageUpload;

    public Restaurant() {
        this.setAddress(new Address());
    }

    public Restaurant(ArrayList<Food> foods, Address address, int phone, String uid, List<Tag> tags, String name, String imageUpload) {
        this.foods = foods;
        this.address = address;
        this.phone = phone;
        this.uid = uid;
        this.tags = tags;
        this.name = name;
        this.imageUpload = imageUpload;
    }

    public Restaurant(String name, int phone, String imageUpload) {
        this.phone = phone;
        this.name = name;
        this.imageUpload = imageUpload;
    }

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



    public ArrayList<String> getTagss(){
        ArrayList<String> tags = new ArrayList<>();
        for (Tag tag : this.tags){
            tags.add(tag.getName());
        }
        return tags;
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
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + phone;
        result = 31 * result + (uid != null ? uid.hashCode() : 0);
        result = 31 * result + (tags != null ? tags.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (imageUpload != null ? imageUpload.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return String.format("%s %s", name, address);
    }

}
