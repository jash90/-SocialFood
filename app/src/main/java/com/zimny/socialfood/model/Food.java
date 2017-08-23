package com.zimny.socialfood.model;

import java.util.List;

/**
 * Created by ideo7 on 17.08.2017.
 */

public class Food {
    private String name;
    private double price;
    private List<Tag> tags;
    private String description;
    private Long uid;
    private Restaurant restaurant;
    private String type;

    public Food() {
    }

    public Food(String name, double price, List<Tag> tags, String description, Long uid, Restaurant restaurant, String type) {
        this.name = name;
        this.price = price;
        this.tags = tags;
        this.description = description;
        this.uid = uid;
        this.restaurant = restaurant;
        this.type = type;
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

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

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

    @Override
    public String toString() {
        return "Food{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", tags=" + tags +
                ", description='" + description + '\'' +
                ", uid=" + uid +
                ", restaurant=" + restaurant +
                ", type='" + type + '\'' +
                '}';
    }
}
