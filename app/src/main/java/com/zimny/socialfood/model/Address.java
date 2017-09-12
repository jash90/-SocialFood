package com.zimny.socialfood.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Address {
    private String nameStreet;
    private String numberHouse;
    private String numberBuilding;
    private String city;
    private String postalCode;


    public Address() {
        this.nameStreet = "";
        this.numberHouse = "";
        this.numberBuilding = "";
        this.city = "";
        this.postalCode = "";
    }

    public Address(String nameStreet, String numberHouse, String numberBuilding, String city, String postalCode) {
        this.nameStreet = nameStreet;
        this.numberHouse = numberHouse;
        this.numberBuilding = numberBuilding;
        this.city = city;
        this.postalCode = postalCode;
    }

    public Address(String nameStreet, String numberHouse, String city, String postalCode) {
        this.nameStreet = nameStreet;
        this.numberHouse = numberHouse;
        this.city = city;
        this.postalCode = postalCode;
    }

    public String getNameStreet() {
        return nameStreet;
    }

    public void setNameStreet(String nameStreet) {
        this.nameStreet = nameStreet;
    }

    public String getNumberHouse() {
        return numberHouse;
    }

    public void setNumberHouse(String numberHouse) {
        this.numberHouse = numberHouse;
    }

    public String getNumberBuilding() {
        return numberBuilding;
    }

    public void setNumberBuilding(String numberBuilding) {
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

    @Override
    public String toString() {
        if (numberBuilding != null && !numberBuilding.isEmpty() ) {
            return String.format("%s, %s %s/%s", city, nameStreet, numberHouse, numberBuilding);
        } else {
            return String.format("%s %s %s", city, nameStreet, numberHouse);
        }

    }

}
