package com.zimny.socialfood.model;

/**
 * Created by ideo7 on 12.09.2017.
 */

public class Info {
    private Address address;
    private int phone;

    public Info() {
    }

    public Info(Address address, int phone) {
        this.address = address;
        this.phone = phone;
    }

    public Info(String city){
        this.address=new Address("","",city,"");
    }
    public Address getAddress() {
        return address;
    }


    public void setAddress(Address address) {
        this.address = address;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Info{" +
                "address=" + address +
                ", phone=" + phone +
                '}';
    }
}
