package com.example.myapplication.activities.models;

public class WalletModel {
    int userId;
    double wallet;

    public WalletModel(int userId, double wallet) {
        this.userId = userId;
        this.wallet = wallet;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getWallet() {
        return wallet;
    }

    public void setWallet(double wallet) {
        this.wallet = wallet;
    }
}
