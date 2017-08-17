package com.zimny.socialfood.model;

import java.util.List;

/**
 * Created by ideo7 on 17.08.2017.
 */

public class Food {
    private String name;
    private double cost;
    private List<Tag> tags;
    private String descriptcion;
    private String uid;
    private String uidRestaurant;

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

    public String getDescriptcion() {
        return descriptcion;
    }

    public void setDescriptcion(String descriptcion) {
        this.descriptcion = descriptcion;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUidRestaurant() {
        return uidRestaurant;
    }

    public void setUidRestaurant(String uidRestaurant) {
        this.uidRestaurant = uidRestaurant;
    }

    public Food() {
    }

    public Food(String name, double cost, List<Tag> tags, String descriptcion, String uid, String uidRestaurant) {
        this.name = name;
        this.cost = cost;
        this.tags = tags;
        this.descriptcion = descriptcion;
        this.uid = uid;
        this.uidRestaurant = uidRestaurant;
    }

    @Override
    public String toString() {
        return "Food{" +
                "name='" + name + '\'' +
                ", cost=" + cost +
                ", tags=" + tags +
                ", descriptcion='" + descriptcion + '\'' +
                ", uid='" + uid + '\'' +
                ", uidRestaurant='" + uidRestaurant + '\'' +
                '}';
    }
}
