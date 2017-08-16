package com.zimny.socialfood.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Address {
    private String nameStreet;
    private int numberHouse;
    private int numberBuilding;
    private String city;
    private String postalCode;


    public String getNameStreet() {
        return nameStreet;
    }

    public void setNameStreet(String nameStreet) {
        this.nameStreet = nameStreet;
    }

    public int getNumberHouse() {
        return numberHouse;
    }

    public void setNumberHouse(int numberHouse) {
        this.numberHouse = numberHouse;
    }

    public int getNumberBuilding() {
        return numberBuilding;
    }

    public void setNumberBuilding(int numberBuilding) {
        this.numberBuilding = numberBuilding;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public Address() {
    }

    public Address(String nameStreet, int numberHouse, int numberBuilding, String city, String postalCode) {
        this.nameStreet = nameStreet;
        this.numberHouse = numberHouse;
        this.numberBuilding = numberBuilding;
        this.city = city;
        this.postalCode = postalCode;
    }

    public Address(String nameStreet, int numberHouse, String city, String postalCode) {
        this.nameStreet = nameStreet;
        this.numberHouse = numberHouse;
        this.city = city;
        this.postalCode = postalCode;
    }

    @Override
    public String toString() {
        if (numberBuilding>0){
            return String.format("%s %d/%d %s %s",nameStreet,numberHouse,numberBuilding, postalCode ,city);
        }
        else{
            return String.format("%s %d %s %s",nameStreet,numberHouse, postalCode ,city);
        }
            
    }
}
