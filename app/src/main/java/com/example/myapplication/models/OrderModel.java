package com.example.myapplication.models;

import java.util.List;

public class OrderModel {
    long order_id;
    String name;
    String address;
    List<OrderItemModel> orderItem_list;
    float total;

    public OrderModel(long order_id, String name, String address, List<OrderItemModel> orderItem_list, float total) {
        this.order_id = order_id;
        this.name = name;
        this.address = address;
        this.orderItem_list = orderItem_list;
        this.total = total;
    }

    public void setOrder_id(long order_id) {
        this.order_id = order_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setOrderItem_list(List<OrderItemModel> orderItem_list) {
        this.orderItem_list = orderItem_list;
    }

    public void setTotal(float total) {
        this.total = total;
    }
}
