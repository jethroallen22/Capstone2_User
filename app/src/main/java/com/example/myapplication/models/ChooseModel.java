package com.example.myapplication.models;

public class ChooseModel {

    String choose_name;
    Float choose_price;

    public ChooseModel(String choose_name, Float choose_price) {
        this.choose_name = choose_name;
        this.choose_price = choose_price;
    }

    public String getChoose_name() {
        return choose_name;
    }

    public void setChoose_name(String choose_name) {
        this.choose_name = choose_name;
    }

    public Float getChoose_price() {
        return choose_price;
    }

    public void setChoose_price(Float choose_price) {
        this.choose_price = choose_price;
    }
}
