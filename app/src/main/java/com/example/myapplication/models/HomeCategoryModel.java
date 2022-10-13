package com.example.myapplication.models;

public class HomeCategoryModel {
    String categ_image;
    String categ_name;

    public HomeCategoryModel(String categ_image, String categ_name) {
        this.categ_image = categ_image;
        this.categ_name = categ_name;
    }

    public String getCateg_image() {
        return categ_image;
    }

    public void setCateg_image(String categ_image) {
        this.categ_image = categ_image;
    }

    public String getCateg_name() {
        return categ_name;
    }

    public void setCateg_name(String categ_name) {
        this.categ_name = categ_name;
    }
}
