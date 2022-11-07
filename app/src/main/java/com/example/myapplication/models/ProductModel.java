package com.example.myapplication.models;

public class ProductModel {

    String product_image;
    String product_name;
    String product_description;
    String store_name;
    Float product_price;
    int product_calories;

    public ProductModel(String product_image, String product_name, String product_description, String store_name, Float product_price, int product_calories) {
        this.product_image = product_image;
        this.product_name = product_name;
        this.product_description = product_description;
        this.store_name = store_name;
        this.product_price = product_price;
        this.product_calories = product_calories;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_description() {
        return product_description;
    }

    public void setProduct_description(String product_description) {
        this.product_description = product_description;
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
