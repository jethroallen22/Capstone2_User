package com.example.myapplication.models;

public class AddressModel {
    String address_name;
    String address_address;

    public AddressModel(String address_name, String address_address) {
        this.address_name = address_name;
        this.address_address = address_address;
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
}
