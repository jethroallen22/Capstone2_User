package com.example.myapplication.models;

import java.sql.Time;
import java.util.List;

public class CartModel  {

    String restoName;
    String restoImage;
    int orderQuantity;

    public CartModel(String restoName, int orderQuantity) {
        this.restoName = restoName;
       // this.restoImage = restoImage;
        this.orderQuantity = orderQuantity;
    }

    public String getRestoName() {
        return restoName;
    }

    public void setRestoName(String restoName) {
        this.restoName = restoName;
    }

    public String getRestoImage() {
        return restoImage;
    }

    public void setRestoImage(String restoImage) {
        this.restoImage = restoImage;
    }

    public int getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(int orderQuantity) {
        this.orderQuantity = orderQuantity;
    }
}
