package com.zimny.socialfood.model;


import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@IgnoreExtraProperties
public class User {

    private String uid;
    private String username;
    private String firstname;
    private String lastname;
    private Address address;
    private Date birthday;
    private List<Restaurant> restaurants;
    private List<Food> foods;
    private List<Group> groups;
    private List<Order> orders;
    private Order shoppingBasket;
    private List<Relationship> relationships;

    public User() {
        uid = "";
        username = "";
        firstname = "";
        lastname = "";
        address = new Address();
        birthday = null;
        restaurants = new ArrayList<>();
        foods = new ArrayList<>();
        groups = new ArrayList<>();
        orders = new ArrayList<>();
        shoppingBasket = new Order();
        relationships = new ArrayList<>();
    }

    public User(String username) {
        this.username = username;
    }

    public User(String uid, String username, String firstname, String lastname, Address address, Date birthday, List<Restaurant> restaurants, List<Food> foods, List<Group> groups, List<Order> orders, Order shoppingBasket, List<Relationship> relationships) {
        this.uid = uid;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.address = address;
        this.birthday = birthday;
        this.restaurants = restaurants;
        this.foods = foods;
        this.groups = groups;
        this.orders = orders;
        this.shoppingBasket = shoppingBasket;
        this.relationships = relationships;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Exclude
    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public List<Restaurant> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(List<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }

    public List<Food> getFoods() {
        return foods;
    }

    public void setFoods(List<Food> foods) {
        this.foods = foods;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public Order getShoppingBasket() {
        return shoppingBasket;
    }

    public void setShoppingBasket(Order shoppingBasket) {
        this.shoppingBasket = shoppingBasket;
    }

    public List<Relationship> getRelationships() {
        return relationships;
    }

    public void setRelationships(List<Relationship> relationships) {
        this.relationships = relationships;
    }

    @Override
    public String toString() {
        return String.format("%s %s, %s", firstname, lastname, address.getCity());
    }
}
