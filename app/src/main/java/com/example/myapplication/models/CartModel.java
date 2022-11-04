package com.example.myapplication.models;

import java.sql.Time;

public class CartModel {
    int store_image;
    String store_name;
    int item_count;
    String time;
    String distance;

    public CartModel(int store_image, String store_name, int item_count, String time, String distance) {
        this.store_image = store_image;
        this.store_name = store_name;
        this.item_count = item_count;
        this.time = time;
        this.distance = distance;
    }

    public int getStore_image() {
        return store_image;
    }

    public void setStore_image(int store_image) {
        this.store_image = store_image;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public int getItem_count() {
        return item_count;
    }

    public void setItem_count(int item_count) {
        this.item_count = item_count;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}
