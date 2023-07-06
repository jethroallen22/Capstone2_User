package com.example.myapplication.models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.util.List;

public class CartModel  {

    String restoName;
    String restoImage;
    int orderQuantity;
    List<OrderItemModel> list;

    public CartModel(String restoName, int orderQuantity, List<OrderItemModel> list) {
        this.restoName = restoName;
        this.restoImage = restoImage;
        this.orderQuantity = orderQuantity;
        this.list = list;
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

    public List<OrderItemModel> getList() {
        return list;
    }

    public void setList(List<OrderItemModel> list) {
        this.list = list;
    }

    public Bitmap getBitmapImage(){
        byte[] byteArray = Base64.decode(restoImage, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0 , byteArray.length);
        return bitmap;
    };
}
