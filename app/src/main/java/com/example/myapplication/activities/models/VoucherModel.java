package com.example.myapplication.activities.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Date;

public class VoucherModel implements Parcelable {

    int voucherId;
    String voucherName;
    int voucherAmount;
    int voucherMin;

    Date startDate;

    Date endDate;

    public VoucherModel(int voucherId, String voucherName,int voucherAmount, int voucherMin, Date startDate, Date endDate){
        this.voucherId = voucherId;
        this.voucherName = voucherName;
        this.voucherAmount = voucherAmount;
        this.voucherMin = voucherMin;
        this.startDate = startDate;
        this.endDate = endDate;
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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(voucherId);
        dest.writeString(voucherName);
        dest.writeInt(voucherAmount);
        dest.writeInt(voucherMin);
        dest.writeSerializable(startDate);
        dest.writeSerializable(endDate);
    }
}
