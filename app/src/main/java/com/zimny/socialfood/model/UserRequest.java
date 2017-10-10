package com.zimny.socialfood.model;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by ideo7 on 10.10.2017.
 */

public class UserRequest extends User{
    private boolean request;

    public boolean isRequest() {
        return request;
    }

    public void setRequest(boolean request) {
        this.request = request;
    }

    public UserRequest() {
    }

    public UserRequest(String uid, String username, String firstname, String lastname, Address address, Date birthday, ArrayList<Restaurant> restaurants, ArrayList<Food> foods, ArrayList<Group> groups, ArrayList<Order> orders, Order shoppingBasket, ArrayList<Relationship> relationships, String imageUpload, int phone, boolean request) {
        super(uid, username, firstname, lastname, address, birthday, restaurants, foods, groups, orders, shoppingBasket, relationships, imageUpload, phone);
        this.request = request;
    }
    public UserRequest(User user, boolean request){
        super(user.getUid(), user.getUsername(), user.getFirstname(), user.getLastname(), user.getAddress(), user.getBirthday(), user.getRestaurants(), user.getFoods(), user.getGroups(), user.getOrders(), user.getShoppingBasket(), user.getRelationships(), user.getImageUpload(), user.getPhone());
        this.request = request;
    }

    @Override
    public String toString() {
        return "UserRequest{" +super.toString()+
                "request=" + request +
                '}';
    }
}
