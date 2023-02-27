package com.example.myapplication.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class OrderItemModel implements Parcelable {
    int idItem;
    int product_idProduct;
    int store_id;
    float itemPrice;
    int itemQuantity;
    int order_idOrder;
    String productName;

    public OrderItemModel(int idItem, int product_idProduct, int store_id, float itemPrice, int itemQuantity, int order_idOrder, String productName) {
        this.idItem = idItem;
        this.product_idProduct = product_idProduct;
        this.store_id = store_id;
        this.itemPrice = itemPrice;
        this.itemQuantity = itemQuantity;
        this.order_idOrder = order_idOrder;
        this.productName = productName;
    }

    public OrderItemModel(int product_idProduct, int store_id, float itemPrice, int itemQuantity, String productName) {
        this.product_idProduct = product_idProduct;
        this.store_id = store_id;
        this.itemPrice = itemPrice;
        this.itemQuantity = itemQuantity;
        this.productName = productName;
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

    protected OrderItemModel(Parcel in) {
//        product_name = in.readString();
//        quantity = in.readInt();
//        total_price = in.readFloat();
        idItem = in.readInt();
        product_idProduct = in.readInt();
        itemPrice = in.readFloat();
        itemQuantity = in.readInt();
        order_idOrder = in.readInt();
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
        dest.writeFloat(itemPrice);
        dest.writeInt(itemQuantity);
        dest.writeInt(order_idOrder);
    }
}