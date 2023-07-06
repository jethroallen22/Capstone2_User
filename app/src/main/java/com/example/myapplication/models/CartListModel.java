package com.example.myapplication.models;

import java.util.List;

public class CartListModel  {

    List<OrderModel> order_list;

    public CartListModel(List<OrderModel> order_list) {
        this.order_list = order_list;
    }

    public List<OrderModel> getOrder_list() {
        return order_list;
    }

    public void setOrder_list(List<OrderModel> order_list) {
        this.order_list = order_list;
    }
}
