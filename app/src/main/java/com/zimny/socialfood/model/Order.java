package com.zimny.socialfood.model;

import java.util.ArrayList;

/**
 * Created by ideo7 on 17.08.2017.
 */

public class Order extends BaseOrder {
    private String date;
    private boolean paying;
    private String uidUser;

    public Order() {
    }

    public Order(String date, boolean paying) {
        this.date = date;
        this.paying = paying;
    }

    public Order(ArrayList<FoodOrder> foodsOrders, String uidGroup, String date, boolean paying, String uidUser) {
        super(foodsOrders, uidGroup, paying);
        this.date = date;
        this.paying = paying;
        this.uidUser = uidUser;
    }

    public Order(BaseOrder baseOrder, String date, boolean paying, String uidUser) {
        super(baseOrder.getFoodOrders(), baseOrder.getUidGroup(), paying);
        this.date = date;
        this.paying = paying;
        this.uidUser = uidUser;
    }

    public Order(ArrayList<FoodOrder> foodsOrders, String uidGroup, boolean paying) {
        super(foodsOrders, uidGroup, paying);
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
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
    public String getUid() {
        return super.getUid();
    }

    @Override
    public void setUid(String uid) {
        super.setUid(uid);
    }

    @Override
    public String getUidGroup() {
        return super.getUidGroup();
    }

    @Override
    public void setUidGroup(String uidGroup) {
        super.setUidGroup(uidGroup);
    }

    @Override
    public ArrayList<FoodOrder> getFoodOrders() {
        return super.getFoodOrders();
    }

    @Override
    public void setFoodOrders(ArrayList<FoodOrder> foodOrders) {
        super.setFoodOrders(foodOrders);
    }

    @Override
    public boolean equals(Object o) {
        Boolean equalss = true;
        if (this == o) {
            return true;
        }
        if (!(o instanceof Order)) {
            return false;
        }

        Order order = (Order) o;

        if (paying != order.paying) {
            return false;
        }
        if (!date.equals(order.date)) {
            return false;
        }
        if (!getUid().equals(order.getUid())) {
            try {
                throw new Exception("UID");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
        if (!getDate().equals(order.getDate())) {
            try {
                throw new Exception("DATE");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
        if (!getUidGroup().equals(order.getUidGroup())) {
            try {
                throw new Exception("GROUP");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
        if (!getFoodOrders().equals(order.getFoodOrders())) {
            try {
                throw new Exception("ORDER");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
        return true;

    }

    @Override
    public int hashCode() {
        int result = date.hashCode();
        result = 31 * result + (paying ? 1 : 0);
        result = 31 * result + uidUser.hashCode();
        return result;
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
