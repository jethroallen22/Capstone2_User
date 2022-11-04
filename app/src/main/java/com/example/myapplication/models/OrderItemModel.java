package com.example.myapplication.models;

public class OrderItemModel {
    String product_name;
    int quantity;
    float total_price;

    public OrderItemModel(String product_name, int quantity, float total_price) {
        this.product_name = product_name;
        this.quantity = quantity;
        this.total_price = total_price;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getTotal_price() {
        return total_price;
    }

    public void setTotal_price(float total_price) {
        this.total_price = total_price;
    }
}
