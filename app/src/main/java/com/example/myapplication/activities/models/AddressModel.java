package com.example.myapplication.activities.models;

public class AddressModel {
    long address_id;
    String address_name;
    String address_address;
    long user_id;

    public AddressModel(long address_id, String address_name, String address_address, long user_id) {
        this.address_id = address_id;
        this.address_name = address_name;
        this.address_address = address_address;
        this.user_id = user_id;
    }

    public long getAddress_id() {
        return address_id;
    }

    public void setAddress_id(long address_id) {
        this.address_id = address_id;
    }

    public String getAddress_name() {
        return address_name;
    }

    public void setAddress_name(String address_name) {
        this.address_name = address_name;
    }

    public String getAddress_address() {
        return address_address;
    }

    public void setAddress_address(String address_address) {
        this.address_address = address_address;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }
}
