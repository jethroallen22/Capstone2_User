package com.example.myapplication.models;

import java.util.List;

public class ProductCategModel {
    String categ;
    List<ProductModel> list;

    public ProductCategModel(String categ, List<ProductModel> list) {
        this.categ = categ;
        this.list = list;
    }

    public String getCateg() {
        return categ;
    }

    public void setCateg(String categ) {
        this.categ = categ;
    }

    public List<ProductModel> getList() {
        return list;
    }

    public void setList(List<ProductModel> list) {
        this.list = list;
    }
}
