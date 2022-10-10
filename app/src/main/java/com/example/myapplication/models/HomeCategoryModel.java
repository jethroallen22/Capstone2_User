package com.example.myapplication.models;

public class HomeCategoryModel {
    int Image;
    String categ_name;

    public HomeCategoryModel(int image, String categ_name) {
        Image = image;
        this.categ_name = categ_name;
    }

    public int getImage() {
        return Image;
    }

    public void setImage(int image) {
        Image = image;
    }

    public String getCateg_name() {
        return categ_name;
    }

    public void setCateg_name(String categ_name) {
        this.categ_name = categ_name;
    }
}
