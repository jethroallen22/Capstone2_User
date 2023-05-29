package com.example.myapplication.models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

public class DealsModel {
    int dealsId;
    int storeId;
    String type;
    int percentage;
    String convFee;
    String storeImage;
    String storeName;

    public DealsModel(int dealsId, int storeId, String type, int percentage,
                      String convFee, String storeImage, String storeName){
        this.dealsId = dealsId;
        this.storeId = storeId;
        this.type = type;
        this.percentage = percentage;
        this.convFee = convFee;
        this.storeImage = storeImage;
        this.storeName = storeName;
    }

    public int getDealsId() {
        return dealsId;
    }

    public void setDealsId(int dealsId) {
        this.dealsId = dealsId;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    public String getConvFee() {
        return convFee;
    }

    public void setConvFee(String convFee) {
        this.convFee = convFee;
    }
    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public Bitmap getBitmapImage(){
        byte[] byteArray = Base64.decode(storeImage, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0 , byteArray.length);
        return bitmap;
    };
}

