package com.zimny.socialfood.model;

import java.util.List;

/**
 * Created by ideo7 on 17.08.2017.
 */

public class Food {
    private String name;
    private double cost;
    private List<Tag> tags;
    private String description;
    private Long uid;
    private Restaurant restaurant;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
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

    public Food() {
    }

    public Food(String name, double cost, List<Tag> tags, String description, Long uid, Restaurant restaurant) {
        this.name = name;
        this.cost = cost;
        this.tags = tags;
        this.description = description;
        this.uid = uid;
        this.restaurant = restaurant;
    }

    @Override
    public String toString() {
        return "Food{" +
                "name='" + name + '\'' +
                ", cost=" + cost +
                ", tags=" + tags +
                ", description='" + description + '\'' +
                ", uid=" + uid +
                ", restaurant=" + restaurant +
                '}';
    }
}
