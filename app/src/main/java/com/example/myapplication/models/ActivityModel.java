package com.example.myapplication.models;

public class ActivityModel {
    String store_name;
    String store_address;
    Float price;
    String date_time;

    public ActivityModel(String store_name, String store_address, Float price, String date_time) {
        this.store_name = store_name;
        this.store_address = store_address;
        this.price = price;
        this.date_time = date_time;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getStore_address() {
        return store_address;
    }

    public void setStore_address(String store_address) {
        this.store_address = store_address;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }
}
