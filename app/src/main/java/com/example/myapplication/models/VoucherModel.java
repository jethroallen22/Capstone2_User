package com.example.myapplication.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class VoucherModel implements Parcelable {

    int voucherId;
    String voucherName;
    int storeId;
    int voucherAmount;
    int voucherMin;

    public VoucherModel(int voucherId, String voucherName, int storeId, int voucherAmount, int voucherMin){
        this.voucherId = voucherId;
        this.voucherName = voucherName;
        this.storeId = storeId;
        this.voucherAmount = voucherAmount;
        this.voucherMin = voucherMin;
    }

    protected VoucherModel(Parcel in) {
    }

    public static final Creator<VoucherModel> CREATOR = new Creator<VoucherModel>() {
        @Override
        public VoucherModel createFromParcel(Parcel in) {
            return new VoucherModel(in);
        }

        @Override
        public VoucherModel[] newArray(int size) {
            return new VoucherModel[size];
        }
    };

    public int getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(int voucherId) {
        this.voucherId = voucherId;
    }

    public String getVoucherName() {
        return voucherName;
    }

    public void setVoucherName(String voucherName) {
        this.voucherName = voucherName;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public int getVoucherAmount() {
        return voucherAmount;
    }

    public void setVoucherAmount(int voucherAmount) {
        this.voucherAmount = voucherAmount;
    }

    public int getVoucherMin() {
        return voucherMin;
    }

    public void setVoucherMin(int voucherMin) {
        this.voucherMin = voucherMin;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(voucherId);
        dest.writeString(voucherName);
        dest.writeInt(storeId);
        dest.writeInt(voucherAmount);
        dest.writeInt(voucherMin);
    }
}
