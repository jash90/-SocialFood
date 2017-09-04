package com.zimny.socialfood.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ideo7 on 04.09.2017.
 */

public class BaseOrder {
    private List<FoodOrder> foodOrders;
    private String uidGroup;

    public BaseOrder() {
        this.foodOrders = new ArrayList<>();
        this.uidGroup = "";
    }

    public BaseOrder(List<FoodOrder> foodsOrders, String uidGroup, boolean paying) {
        this.foodOrders = foodsOrders;
        this.uidGroup = uidGroup;
    }

    public String getUidGroup() {
        return uidGroup;
    }

    public void setUidGroup(String uidGroup) {
        this.uidGroup = uidGroup;
    }


    public List<FoodOrder> getFoodOrders() {
        return foodOrders;
    }

    public void setFoodOrders(List<FoodOrder> foodOrders) {
        this.foodOrders = foodOrders;
    }

    @Override
    public String toString() {
        return "BaseOrder{" +
                "foodOrders=" + foodOrders +
                ", uidGroup='" + uidGroup + '\'' +
                '}';
    }
}
