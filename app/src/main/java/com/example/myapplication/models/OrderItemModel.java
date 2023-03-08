package com.example.myapplication.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class OrderItemModel implements Parcelable {
    int idProduct;
    int idStore;
    int idUser;
    int idOrder;
    String productName;
    float itemPrice;
    int itemQuantity;
    float totalPrice;


    public OrderItemModel(int idProduct, int idStore, int idUser, int idOrder, String productName, float itemPrice, int itemQuantity, float totalPrice) {
        this.idProduct = idProduct;
        this.idStore = idStore;
        this.idUser = idUser;
        this.idOrder = idOrder;
        this.productName = productName;
        this.itemPrice = itemPrice;
        this.itemQuantity = itemQuantity;
        this.totalPrice = totalPrice;
    }

    public OrderItemModel(int idProduct, int idStore, int idUser, String productName, float itemPrice, int itemQuantity, float totalPrice) {
        this.idProduct = idProduct;
        this.idStore = idStore;
        this.idUser = idUser;
        this.productName = productName;
        this.itemPrice = itemPrice;
        this.itemQuantity = itemQuantity;
        this.totalPrice = totalPrice;
    }

//    public OrderItemModel(String product_name, int quantity, float total_price) {
//        this.product_name = product_name;
//        this.quantity = quantity;
//        this.total_price = total_price;
//    }


    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public int getIdStore() {
        return idStore;
    }

    public void setIdStore(int idStore) {
        this.idStore = idStore;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }


    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
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

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(int idOrder) {
        this.idOrder = idOrder;
    }

    protected OrderItemModel(Parcel in) {
        idProduct = in.readInt();
        idStore = in.readInt();
        idUser = in.readInt();
        productName = in.readString();
        itemPrice = in.readFloat();
        itemQuantity = in.readInt();
        idOrder = in.readInt();
        totalPrice = in.readFloat();
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

        dest.writeInt(idProduct);
        dest.writeInt(idUser);
        dest.writeInt(idStore);
        dest.writeString(productName);
        dest.writeFloat(itemPrice);
        dest.writeInt(itemQuantity);
        dest.writeInt(idOrder);
        dest.writeFloat(totalPrice);
    }
}