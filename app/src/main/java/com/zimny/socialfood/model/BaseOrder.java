package com.zimny.socialfood.model;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ideo7 on 04.09.2017.
 */

public class BaseOrder {

    private ArrayList<FoodOrder> foodOrders;
    private String uidGroup;
    private String uid;

    public BaseOrder() {
        this.foodOrders = new ArrayList<>();
        this.uidGroup = "";
        this.uid = "";
    }

    public BaseOrder(ArrayList<FoodOrder> foodsOrders, String uidGroup, boolean paying) {
        this.foodOrders = foodsOrders;
        this.uidGroup = uidGroup;
        this.uid = "";
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUidGroup() {
        return uidGroup;
    }

    public void setUidGroup(String uidGroup) {
        this.uidGroup = uidGroup;
    }

    @Exclude
    public ArrayList<FoodOrder> getFoodOrders() {
        return foodOrders;
    }

    public void setFoodOrders(ArrayList<FoodOrder> foodOrders) {
        this.foodOrders = foodOrders;
    }

    @Override
    public String toString() {
        return "BaseOrder{" +
                "uid="+uid+
                ", foodOrders=" + foodOrders +
                ", uidGroup='" + uidGroup + '\'' +
                '}';
    }
}
