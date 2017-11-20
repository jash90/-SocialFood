package com.zimny.socialfood.model;


import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.Date;


@IgnoreExtraProperties
public class User extends Info {

    private String uid;
    private String username;
    private String firstname;
    private String lastname;
    private Date birthday;
    private ArrayList<Restaurant> restaurants;
    private ArrayList<Food> foods;
    private ArrayList<Group> groups;
    private ArrayList<Order> orders;
    private Order shoppingBasket;
    private ArrayList<Relationship> relationships;
    private String imageUpload;

    public User() {
        uid = "";
        username = "";
        firstname = "";
        lastname = "";
        super.setAddress(new Address());
        birthday = null;
        restaurants = new ArrayList<>();
        foods = new ArrayList<>();
        groups = new ArrayList<>();
        orders = new ArrayList<>();
        shoppingBasket = new Order();
        relationships = new ArrayList<>();
        imageUpload = "";
    }

    public User(String username) {
        this.username = username;
    }

    public User(String username, String firstname, String lastname, String city) {
        super(city);
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public User(String uid, String username, String firstname, String lastname, Address address, Date birthday, ArrayList<Restaurant> restaurants, ArrayList<Food> foods, ArrayList<Group> groups, ArrayList<Order> orders, Order shoppingBasket, ArrayList<Relationship> relationships, String imageUpload, int phone) {
        this.uid = uid;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        super.setAddress(address);
        super.setPhone(phone);
        this.birthday = birthday;
        this.restaurants = restaurants;
        this.foods = foods;
        this.groups = groups;
        this.orders = orders;
        this.shoppingBasket = shoppingBasket;
        this.relationships = relationships;
        this.imageUpload = imageUpload;
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

    @Exclude
    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public ArrayList<Restaurant> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(ArrayList<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }

    public ArrayList<Food> getFoods() {
        return foods;
    }

    public void setFoods(ArrayList<Food> foods) {
        this.foods = foods;
    }

    public ArrayList<Group> getGroups() {
        return groups;
    }

    public void setGroups(ArrayList<Group> groups) {
        this.groups = groups;
    }

    public ArrayList<Order> getOrders() {
        return orders;
    }

    public void setOrders(ArrayList<Order> orders) {
        this.orders = orders;
    }

    public Order getShoppingBasket() {
        return shoppingBasket;
    }

    public void setShoppingBasket(Order shoppingBasket) {
        this.shoppingBasket = shoppingBasket;
    }

    public ArrayList<Relationship> getRelationships() {
        return relationships;
    }

    public void setRelationships(ArrayList<Relationship> relationships) {
        this.relationships = relationships;
    }

    public String getImageUpload() {
        return imageUpload;
    }

    public void setImageUpload(String imageUpload) {
        this.imageUpload = imageUpload;
    }


    public String alltoString() {
        return "User{" +
                "uid='" + uid + '\'' +
                ", username='" + username + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", birthday=" + birthday +
                ", restaurants=" + restaurants +
                ", foods=" + foods +
                ", groups=" + groups +
                ", orders=" + orders +
                ", shoppingBasket=" + shoppingBasket +
                ", relationships=" + relationships +
                ", imageUpload='" + imageUpload + '\'' +
                '}';
    }

    @Override
    public String toString() {
        return String.format("%s %s, %s", firstname, lastname, getAddress().getCity());
    }
}
