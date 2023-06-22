package com.example.myapplication.activities.models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.nfc.Tag;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Base64;

import androidx.annotation.NonNull;

import java.util.List;

public class ProductModel implements Parcelable {

    int idProduct;
    int store_idStore;
    String productName;
    String productDescription;
    float productPrice;
    String productImage;
    String productServingSize;
    int productPrepTime;
    String productRestoName;
    String productRestoImage;
    String productRestoCategory;
    String weather;

    String productTag;
    int percentage;

    List<TagModel> tags_list;




    //    public ProductModel(Long product_id, String product_image, String product_name, String product_description, String store_name, Float product_price, int product_calories) {
//        this.product_id = product_id;
//        this.product_image = product_image;
//        this.product_name = product_name;
//        this.product_description = product_description;
//        this.store_name = store_name;
//        this.product_price = product_price;
//        this.product_calories = product_calories;
//    }
//
//    public ProductModel(String product_image, String product_name, String product_description, String store_name, Float product_price, int product_calories) {
//        this.product_image = product_image;
//        this.product_name = product_name;
//        this.product_description = product_description;
//        this.store_name = store_name;
//        this.product_price = product_price;
//        this.product_calories = product_calories;
//    }
    public ProductModel(int idProduct, int store_idStore, String productName, String productDescription, float productPrice, String productImage, String productServingSize, String productTag, int productPrepTime, String productRestoName, String productRestoImage, String weather) {
        this.idProduct = idProduct;
        this.store_idStore = store_idStore;
        this.productName = productName;
        this.productDescription = productDescription;
        this.productPrice = productPrice;
        this.productImage = productImage;
        this.productServingSize = productServingSize;
        this.productTag = productTag;
        this.productPrepTime = productPrepTime;
        this.productRestoName = productRestoName;
        this.productRestoImage = productRestoImage;
        this.weather = weather;
    }

    public ProductModel(int idProduct, int store_idStore, String productName, String productDescription, float productPrice, String productImage, String productServingSize, int productPrepTime, String productRestoName, String productRestoImage, String weather, List<TagModel> tags_list) {
        this.idProduct = idProduct;
        this.store_idStore = store_idStore;
        this.productName = productName;
        this.productDescription = productDescription;
        this.productPrice = productPrice;
        this.productImage = productImage;
        this.productServingSize = productServingSize;
        this.productPrepTime = productPrepTime;
        this.productRestoName = productRestoName;
        this.productRestoImage = productRestoImage;
        this.weather = weather;
        this.tags_list = tags_list;
    }

    public ProductModel(){}

    protected ProductModel(Parcel in) {
        idProduct = in.readInt();
        store_idStore = in.readInt();
        productName = in.readString();
        productDescription = in.readString();
        productPrice = in.readFloat();
        productImage = in.readString();
        productServingSize = in.readString();
        productTag = in.readString();
        productPrepTime = in.readInt();
        productRestoName = in.readString();
        productRestoImage = in.readString();
        productRestoCategory = in.readString();
        weather = in.readString();
        tags_list = in.createTypedArrayList(TagModel.CREATOR);
    }

    public static final Creator<ProductModel> CREATOR = new Creator<ProductModel>() {
        @Override
        public ProductModel createFromParcel(Parcel in) {
            return new ProductModel(in);
        }

        @Override
        public ProductModel[] newArray(int size) {
            return new ProductModel[size];
        }
    };

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public int getStore_idStore() {
        return store_idStore;
    }

    public void setStore_idStore(int store_idStore) {
        this.store_idStore = store_idStore;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public float getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Float productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductServingSize() {
        return productServingSize;
    }

    public void setProductServingSize(String productServingSize) {
        this.productServingSize = productServingSize;
    }

    public int getProductPrepTime() {
        return productPrepTime;
    }

    public void setProductPrepTime(int productPrepTime) {
        this.productPrepTime = productPrepTime;
    }

    public String getProductRestoName() {
        return productRestoName;
    }

    public void setProductRestoName(String productRestoName) {
        this.productRestoName = productRestoName;
    }

    public String getProductRestoImage() {
        return productRestoImage;
    }

    public void setProductRestoImage(String productRestoImage) {
        this.productRestoImage = productRestoImage;
    }

    public String getProductRestoCategory() {
        return productRestoCategory;
    }

    public void setProductRestoCategory(String productRestoCategory) {
        this.productRestoCategory = productRestoCategory;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public int getPercentage(){return percentage;}

    public void setPercentage(int percentage){ this.percentage = percentage;}

    public List<TagModel> getTags_list() {
        return tags_list;
    }

    public void setTags_list(List<TagModel> tags_list) {
        this.tags_list = tags_list;
    }

    public void setProductPrice(float productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductTag() {
        return productTag;
    }

    public void setProductTag(String productTag) {
        this.productTag = productTag;
    }

    public Bitmap getBitmapImage(){
        byte[] byteArray = Base64.decode(productImage, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0 , byteArray.length);
        return bitmap;
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeInt(idProduct);
        parcel.writeInt(store_idStore);
        parcel.writeString(productName);
        parcel.writeString(productDescription);
        parcel.writeFloat(productPrice);
        parcel.writeString(productImage);
        parcel.writeString(productServingSize);
        parcel.writeString(productTag);
        parcel.writeInt(productPrepTime);
        parcel.writeString(productRestoName);
        parcel.writeString(productRestoImage);
        parcel.writeString(productRestoCategory);
        parcel.writeString(weather);
        parcel.writeTypedList(tags_list);
    }
}