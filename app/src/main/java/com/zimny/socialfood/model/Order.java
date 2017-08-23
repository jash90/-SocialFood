package com.zimny.socialfood.model;

import java.util.List;

/**
 * Created by ideo7 on 17.08.2017.
 */

public class Order {
    private List<Food> foods;
    private String uid;
    private String uidUser;
    private String uidGroup;
    private boolean paying;

    public Order() {
    }

    public Order(List<Food> foods, String uid, String uidUser, String uidGroup, boolean paying) {
        this.foods = foods;
        this.uid = uid;
        this.uidUser = uidUser;
        this.uidGroup = uidGroup;
        this.paying = paying;
    }

    public List<Food> getFoods() {
        return foods;
    }

    public void setFoods(List<Food> foods) {
        this.foods = foods;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUidUser() {
        return uidUser;
    }

    public void setUidUser(String uidUser) {
        this.uidUser = uidUser;
    }

    public String getUidGroup() {
        return uidGroup;
    }

    public void setUidGroup(String uidGroup) {
        this.uidGroup = uidGroup;
    }

    public boolean isPaying() {
        return paying;
    }

    public void setPaying(boolean paying) {
        this.paying = paying;
    }

    @Override
    public String toString() {
        return "Order{" +
                "foods=" + foods +
                ", uid='" + uid + '\'' +
                ", uidUser='" + uidUser + '\'' +
                ", uidGroup='" + uidGroup + '\'' +
                ", paying=" + paying +
                '}';
    }
}
