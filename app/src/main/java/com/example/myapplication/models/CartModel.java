package com.example.myapplication.models;

import java.sql.Time;
import java.util.List;

public class CartModel  {
    List<OrderModel> order_list;
    List<OrderItemModel> orderItem_list;

    public CartModel(List<OrderModel> order_list, List<OrderItemModel> orderItem_list) {
        this.order_list = order_list;
        this.orderItem_list = orderItem_list;
    }

    public List<OrderModel> getOrder_list() {
        return order_list;
    }

    public void setOrder_list(List<OrderModel> order_list) {
        this.order_list = order_list;
    }

    public List<OrderItemModel> getOrderItem_list() {
        return orderItem_list;
    }

    public void setOrderItem_list(List<OrderItemModel> orderItem_list) {
        this.orderItem_list = orderItem_list;
    }
}
