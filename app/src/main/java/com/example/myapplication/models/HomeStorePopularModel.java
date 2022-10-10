package com.example.myapplication.models;

public class HomeStorePopularModel {
    int Image;
    String store_name;
    String store_category;

    public HomeStorePopularModel(int image, String store_name, String store_category) {
        Image = image;
        this.store_name = store_name;
        this.store_category = store_category;
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

    public String getStore_category() {
        return store_category;
    }

    public void setStore_category(String store_category) {
        this.store_category = store_category;
    }
}
