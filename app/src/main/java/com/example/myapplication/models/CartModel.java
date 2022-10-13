package com.example.myapplication.models;

public class CartModel {
    int image;
    String name;
    String price;
    String rating;

    public CartModel(int image, String name, String price, String rating){
        this.image = image;
        this.name= name;
        this.price = price;
        this.rating = rating;
    }

    public int getImage(){
        return image;
    }

    public void setImage(int image){
        this.image = image;
    }

    public String getName(){
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getRating() {
        return rating;
    }
}
