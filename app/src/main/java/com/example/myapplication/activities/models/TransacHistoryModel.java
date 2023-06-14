package com.example.myapplication.activities.models;

public class TransacHistoryModel {
    String transac_type;
    String transac_date;
    double transac_amount;

    public TransacHistoryModel(String transac_type, String transac_date, double transac_amount) {
        this.transac_type = transac_type;
        this.transac_date = transac_date;
        this.transac_amount = transac_amount;
    }

    public String getTransac_type() {
        return transac_type;
    }

    public void setTransac_type(String transac_type) {
        this.transac_type = transac_type;
    }

    public String getTransac_date() {
        return transac_date;
    }

    public void setTransac_date(String transac_date) {
        this.transac_date = transac_date;
    }

    public double getTransac_amount() {
        return transac_amount;
    }

    public void setTransac_amount(double transac_amount) {
        this.transac_amount = transac_amount;
    }
}
