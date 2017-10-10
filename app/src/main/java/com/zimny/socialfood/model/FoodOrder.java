package com.zimny.socialfood.model;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;

/**
 * Created by ideo7 on 01.09.2017.
 */

public class FoodOrder extends Food {
    private int count;

    public FoodOrder() {
    }

    public FoodOrder(int count) {
        this.count = count;
    }

    public FoodOrder(String name, double price, ArrayList<Tag> tags, String description, String uid, Restaurant restaurant, String type, String imageUpload, int count) {
        super(name, price, tags, description, uid, restaurant, type, imageUpload);
        this.count = count;
    }

    public FoodOrder(Food food, int count) {
        super(food.getName(), food.getPrice(), food.getTags(), food.getDescription(), food.getUid(), food.getRestaurant(), food.getType(), food.getImageUpload());
        this.count = count;
    }

    @Exclude
    public Food getFood() {
        return new Food(super.getName(), super.getPrice(), super.getTags(), super.getDescription(), super.getUid(), super.getRestaurant(), super.getType(), super.getImageUpload());
    }

    @Exclude
    public void setFood(Food food) {
        this.setDescription(food.getDescription());
        this.setName(food.getName());
        this.setPrice(food.getPrice());
        this.setRestaurant(food.getRestaurant());
        this.setType(food.getType());
        this.setUid(food.getUid());
        this.setTags(food.getTags());
        this.setImageUpload(food.getImageUpload());
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String getImageUpload() {
        return super.getImageUpload();
    }

    @Override
    public String toString() {
        return String.format("%s %.2f zł %d szt.", super.getName(), super.getPrice(), getCount());
    }
}
