package com.example.myapplication.models;

public class HomeStoreRecModel {
    int Image;
    String store_name;
    String store_location;
    String store_category;
    Float store_rating;

    public HomeStoreRecModel(int image, String store_name, String store_location, String store_category, Float store_rating) {
        Image = image;
        this.store_name = store_name;
        this.store_location = store_location;
        this.store_category = store_category;
        this.store_rating = store_rating;
    }

    public int getImage() {
        return Image;
    }

    public void setImage(int image) {
        Image = image;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getStore_location() {
        return store_location;
    }

    public void setStore_location(String store_location) {
        this.store_location = store_location;
    }

    public String getStore_category() {
        return store_category;
    }

    public void setStore_category(String store_category) {
        this.store_category = store_category;
    }

    public Float getStore_rating() {
        return store_rating;
    }

    public void setStore_rating(Float store_rating) {
        this.store_rating = store_rating;
    }
}
