package com.example.myapplication.activities.models;

public class HomeFoodForYouModel {
    int Image;
    String product_name;
    String store_name;
    Float product_price;
    int product_calories;

    public HomeFoodForYouModel(int image, String product_name, String store_name, Float product_price, int product_calories) {
        Image = image;
        this.product_name = product_name;
        this.store_name = store_name;
        this.product_price = product_price;
        this.product_calories = product_calories;
    }

    public int getImage() {
        return Image;
    }

    public void setImage(int image) {
        Image = image;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public Float getProduct_price() {
        return product_price;
    }

    public void setProduct_price(Float product_price) {
        this.product_price = product_price;
    }

    public int getProduct_calories() {
        return product_calories;
    }

    public void setProduct_calories(int product_calories) {
        this.product_calories = product_calories;
    }
}
