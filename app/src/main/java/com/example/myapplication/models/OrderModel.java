package com.example.myapplication.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.List;

public class OrderModel implements Parcelable {
    int idOrder;
    float orderItemTotalPrice;
    String orderStatus;
    int store_idstore;
    String store_image;
    String store_name;
    int users_id;
    List<OrderItemModel> orderItem_list;

    public OrderModel(int idOrder, float orderItemTotalPrice, String orderStatus, int store_idstore, String store_image, String store_name, int users_id, List<OrderItemModel> orderItem_list) {
        this.idOrder = idOrder;
        this.orderItemTotalPrice = orderItemTotalPrice;
        this.orderStatus = orderStatus;
        this.store_idstore = store_idstore;
        this.store_image = store_image;
        this.store_name = store_name;
        this.users_id = users_id;
        this.orderItem_list = orderItem_list;
    }

    //    public OrderModel(long order_id, String name, String address, String time, String distance, List<OrderItemModel> orderItem_list, int item_count, float total) {
//        this.order_id = order_id;
//        this.name = name;
//        this.address = address;
//        this.time = time;
//        this.distance = distance;
//        this.orderItem_list = orderItem_list;
//        this.item_count = item_count;
//        this.total = total;
//    }

//    public OrderModel(String name, String address, String time, String distance, List<OrderItemModel> orderItem_list, int item_count, float total) {
//        this.name = name;
//        this.address = address;
//        this.time = time;
//        this.distance = distance;
//        this.orderItem_list = orderItem_list;
//        this.item_count = item_count;
//        this.total = total;
//    }

    public OrderModel() {
    }

    protected OrderModel(Parcel in) {
        idOrder = in.readInt();
        orderItemTotalPrice = in.readFloat();
        orderStatus = in.readString();
        store_idstore = in.readInt();
        users_id = in.readInt();
        orderItem_list = in.createTypedArrayList(OrderItemModel.CREATOR);
    }

    public static final Creator<OrderModel> CREATOR = new Creator<OrderModel>() {
        @Override
        public OrderModel createFromParcel(Parcel in) {
            return new OrderModel(in);
        }

        @Override
        public OrderModel[] newArray(int size) {
            return new OrderModel[size];
        }
    };

    public int getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(int idOrder) {
        this.idOrder = idOrder;
    }

    public float getOrderItemTotalPrice() {
        return orderItemTotalPrice;
    }

    public void setOrderItemTotalPrice(float orderItemTotalPrice) {
        this.orderItemTotalPrice = orderItemTotalPrice;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public int getStore_idstore() {
        return store_idstore;
    }

    public void setStore_idstore(int store_idstore) {
        this.store_idstore = store_idstore;
    }

    public int getUsers_id() {
        return users_id;
    }

    public void setUsers_id(int users_id) {
        this.users_id = users_id;
    }

    public List<OrderItemModel> getOrderItem_list() {
        return orderItem_list;
    }

    public void setOrderItem_list(List<OrderItemModel> orderItem_list) {
        this.orderItem_list = orderItem_list;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(idOrder);
        dest.writeFloat(orderItemTotalPrice);
        dest.writeString(orderStatus);
        dest.writeInt(store_idstore);
        dest.writeString(store_name);
        dest.writeString(store_image);
        dest.writeInt(users_id);
        dest.writeTypedList(orderItem_list);
    }
}