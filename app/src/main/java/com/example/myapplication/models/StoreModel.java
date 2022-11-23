package com.example.myapplication.models;

public class StoreModel {
    long store_id;
    long merchant_id;
    String store_image;
    String store_name;
    String store_description;
    String store_location;
    String store_category;
    Float store_rating;
    int store_popularity;
    String store_open;
    String store_closing;
    String store_tag;

    public StoreModel(long store_id,long merchant_id, String store_image, String store_name, String store_description,
                      String store_location, String store_category, Float store_rating, int store_popularity,String store_open,
                      String store_closing, String store_tag) {
        this.store_id = store_id;
        this.merchant_id = merchant_id;
        this.store_image = store_image;
        this.store_name = store_name;
        this.store_description = store_description;
        this.store_location = store_location;
        this.store_category = store_category;
        this.store_rating = store_rating;
        this.store_popularity = store_popularity;
        this.store_open = store_open;
        this.store_closing = store_closing;
        this.store_tag = store_tag;
    }

    public long getStore_id() {
        return store_id;
    }

    public void setStore_id(long store_id) {
        this.store_id = store_id;
    }

    public long getMerchant_id() {
        return merchant_id;
    }

    public void setMerchant_id(long merchant_id) {
        this.merchant_id = merchant_id;
    }

    public String getStore_image() {
        return store_image;
    }

    public void setStore_image(String store_image) {
        this.store_image = store_image;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getStore_description() {
        return store_description;
    }

    public void setStore_description(String store_description) {
        this.store_description = store_description;
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

    public int getStore_popularity() {
        return store_popularity;
    }

    public void setStore_popularity(int store_popularity) {
        this.store_popularity = store_popularity;
    }

    public String getStore_open() {
        return store_open;
    }

    public void setStore_open(String store_open) {
        this.store_open = store_open;
    }

    public String getStore_closing() {
        return store_closing;
    }

    public void setStore_closing(String store_closing) {
        this.store_closing = store_closing;
    }

    public String getStore_tag() {
        return store_tag;
    }

    public void setStore_tag(String store_tag) {
        this.store_tag = store_tag;
    }
}
