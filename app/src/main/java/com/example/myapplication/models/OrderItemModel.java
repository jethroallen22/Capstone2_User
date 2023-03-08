package com.example.myapplication.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class OrderItemModel implements Parcelable {
    int idItem;
    int product_idProduct;
    int store_id;
    int userId;
    float itemPrice;
    int itemQuantity;
    int order_idOrder;
    float totalPrice;
    String productName;

    public OrderItemModel(int idItem, int product_idProduct, int store_id, int userId, float itemPrice, int itemQuantity, int order_idOrder, String productName, Float totalPrice) {
        this.idItem = idItem;
        this.product_idProduct = product_idProduct;
        this.store_id = store_id;
        this.userId = userId;
        this.itemPrice = itemPrice;
        this.itemQuantity = itemQuantity;
        this.order_idOrder = order_idOrder;
        this.productName = productName;
        this.totalPrice = totalPrice;
    }

    public OrderItemModel(int product_idProduct, int store_id, int userId, float itemPrice, int itemQuantity, String productName, Float totalPrice) {
        this.product_idProduct = product_idProduct;
        this.store_id = store_id;
        this.userId = userId;
        this.itemPrice = itemPrice;
        this.itemQuantity = itemQuantity;
        this.productName = productName;
        this.totalPrice = totalPrice;
    }

//    public OrderItemModel(String product_name, int quantity, float total_price) {
//        this.product_name = product_name;
//        this.quantity = quantity;
//        this.total_price = total_price;
//    }


    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getIdItem() {
        return idItem;
    }

    public void setIdItem(int idItem) {
        this.idItem = idItem;
    }

    public int getProduct_idProduct() {
        return product_idProduct;
    }

    public void setProduct_idProduct(int product_idProduct) {
        this.product_idProduct = product_idProduct;
    }

    public int getStore_id(){
        return store_id;
    }

    public void setStore_id(int store_id){
        this.store_id = store_id;
    }

    public float getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(float itemPrice) {
        this.itemPrice = itemPrice;
    }

    public int getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(int itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public int getOrder_idOrder() {
        return order_idOrder;
    }

    public void setOrder_idOrder(int order_idOrder) {
        this.order_idOrder = order_idOrder;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    protected OrderItemModel(Parcel in) {
        productName = in.readString();
//        quantity = in.readInt();
//        total_price = in.readFloat();
        idItem = in.readInt();
        product_idProduct = in.readInt();
        itemPrice = in.readFloat();
        itemQuantity = in.readInt();
        order_idOrder = in.readInt();
        totalPrice = in.readFloat();
        userId = in.readInt();
        store_id = in.readInt();

    }

    public static final Creator<OrderItemModel> CREATOR = new Creator<OrderItemModel>() {
        @Override
        public OrderItemModel createFromParcel(Parcel in) {
            return new OrderItemModel(in);
        }

        @Override
        public OrderItemModel[] newArray(int size) {
            return new OrderItemModel[size];
        }
    };



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
//        dest.writeString(product_name);
//        dest.writeInt(quantity);
//        dest.writeFloat(total_price);
        dest.writeInt(idItem);
        dest.writeInt(product_idProduct);
        dest.writeInt(userId);
        dest.writeInt(store_id);
        dest.writeFloat(itemPrice);
        dest.writeInt(itemQuantity);
        dest.writeInt(order_idOrder);
        dest.writeFloat(totalPrice);
    }
}