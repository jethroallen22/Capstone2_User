package com.example.myapplication.activities.models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Base64;

import androidx.annotation.NonNull;

public class DealsModel implements Parcelable {
    int dealsId;
    int storeId;
    int percentage;
    String storeImage;
    String storeName;

    String storeCategory;

    float distance;

    public DealsModel(int dealsId, int storeId, int percentage, String storeImage, String storeName, String storeCategory){
        this.dealsId = dealsId;
        this.storeId = storeId;
        this.percentage = percentage;
        this.storeImage = storeImage;
        this.storeName = storeName;
        this.storeCategory = storeCategory;
    }

    protected DealsModel(Parcel in) {
        dealsId = in.readInt();
        storeId = in.readInt();
        percentage = in.readInt();
        storeImage = in.readString();
        storeName = in.readString();
        storeCategory = in.readString();
    }

    public static final Creator<DealsModel> CREATOR = new Creator<DealsModel>() {
        @Override
        public DealsModel createFromParcel(Parcel in) {
            return new DealsModel(in);
        }

        @Override
        public DealsModel[] newArray(int size) {
            return new DealsModel[size];
        }
    };

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

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }



    public Bitmap getBitmapImage(){
        byte[] byteArray = Base64.decode(storeImage, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0 , byteArray.length);
        return bitmap;
    };

    public String getStoreCategory() {
        return storeCategory;
    }

    public void setStoreCategory(String storeCategory) {
        this.storeCategory = storeCategory;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(dealsId);
        dest.writeInt(storeId);
        dest.writeInt(percentage);
        dest.writeString(storeImage);
        dest.writeString(storeName);
        dest.writeString(storeCategory);
    }
}

