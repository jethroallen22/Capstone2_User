package com.example.myapplication.activities.models;

public class AddonModel {
    String addon_name;
    Float addon_price;

    public AddonModel(String addon_name, Float addon_price) {
        this.addon_name = addon_name;
        this.addon_price = addon_price;
    }

    public String getAddon_name() {
        return addon_name;
    }

    public void setAddon_name(String addon_name) {
        this.addon_name = addon_name;
    }

    public Float getAddon_price() {
        return addon_price;
    }

    public void setAddon_price(Float addon_price) {
        this.addon_price = addon_price;
    }
}
