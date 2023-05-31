package com.example.myapplication.models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Base64;

import androidx.annotation.NonNull;

public class DealsModel implements Parcelable {
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

    protected DealsModel(Parcel in) {
        dealsId = in.readInt();
        storeId = in.readInt();
        type = in.readString();
        percentage = in.readInt();
        convFee = in.readString();
        storeImage = in.readString();
        storeName = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(dealsId);
        dest.writeInt(storeId);
        dest.writeString(type);
        dest.writeInt(percentage);
        dest.writeString(convFee);
        dest.writeString(storeImage);
        dest.writeString(storeName);
    }
}

