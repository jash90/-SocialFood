package com.zimny.socialfood.model;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;

/**
 * Created by ideo7 on 17.08.2017.
 */

public class Food {
    private String name;
    private double price;
    private ArrayList<Tag> tags;
    private String description;
    private String uid;
    private Restaurant restaurant;
    private String type;
    private String imageUpload;

    public Food() {
    }

    public Food(String name, double price, ArrayList<Tag> tags, String description, String uid, Restaurant restaurant, String type, String imageUpload) {
        this.name = name;
        this.price = price;
        this.tags = tags;
        this.description = description;
        this.uid = uid;
        this.restaurant = restaurant;
        this.type = type;
        this.imageUpload = imageUpload;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Exclude
    public ArrayList<Tag> getTags() {
        return tags;
    }

    public void setTags(ArrayList<Tag> tags) {
        this.tags = tags;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Exclude
    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImageUpload() {
        return imageUpload;
    }

    public void setImageUpload(String imageUpload) {
        this.imageUpload = imageUpload;
    }

//    @Override
//    public String toString() {
//        return String.format("%s, %.2f %s", name, price, restaurant);
//    }


    @Override
    public String toString() {
        return "Food{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", tags=" + tags +
                ", description='" + description + '\'' +
                ", uid='" + uid + '\'' +
                ", restaurant=" + restaurant +
                ", type='" + type + '\'' +
                ", imageUpload='" + imageUpload + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Food)) return false;
        Food food = (Food) o;
        return uid != null && uid.equals(food.uid);

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = name != null ? name.hashCode() : 0;
        temp = Double.doubleToLongBits(price);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (tags != null ? tags.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (uid != null ? uid.hashCode() : 0);
        result = 31 * result + (restaurant != null ? restaurant.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }
}
