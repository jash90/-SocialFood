package com.zimny.socialfood.model;

import java.util.List;

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

    public FoodOrder(Food food, int count) {
        super(food.getName(), food.getPrice(), food.getTags(), food.getDescription(), food.getUid(), food.getRestaurant(), food.getType());
        this.count = count;
    }

    public FoodOrder(String name, double price, List<Tag> tags, String description, String uid, Restaurant restaurant, String type, int count) {
        super(name, price, tags, description, uid, restaurant, type);
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "FoodOrder{" +
                "Food=" + super.toString() +
                "count=" + count +
                '}';
    }
}
