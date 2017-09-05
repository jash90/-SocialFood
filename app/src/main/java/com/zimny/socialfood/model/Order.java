package com.zimny.socialfood.model;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ideo7 on 17.08.2017.
 */

public class Order extends BaseOrder {
    private Date date;
    private boolean paying;
    private String uidUser;

    public Order() {
    }

    public Order(Date date, boolean paying) {
        this.date = date;
        this.paying = paying;
    }

    public Order(ArrayList<FoodOrder> foodsOrders, String uidGroup, Date date, boolean paying, String uidUser) {
        super(foodsOrders, uidGroup, paying);
        this.date = date;
        this.paying = paying;
        this.uidUser = uidUser;
    }

    public Order(BaseOrder baseOrder, Date date, boolean paying, String uidUser) {
        super(baseOrder.getFoodOrders(), baseOrder.getUidGroup(), paying);
        this.date = date;
        this.paying = paying;
        this.uidUser = uidUser;
    }
    @Exclude
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isPaying() {
        return paying;
    }

    public void setPaying(boolean paying) {
        this.paying = paying;
    }

    public String getUidUser() {
        return uidUser;
    }

    public void setUidUser(String uidUser) {
        this.uidUser = uidUser;
    }

    @Override
    public String toString() {
        return "Order{" +
                "BaseOrder=" + super.toString() +
                "date=" + date +
                ", paying=" + paying +
                ", uidUser='" + uidUser + '\'' +
                '}';
    }
}
