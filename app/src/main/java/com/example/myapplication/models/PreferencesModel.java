package com.example.myapplication.models;

public class PreferencesModel {
    int idUser;
    String tag;

    public PreferencesModel(int idUser, String tag) {
        this.idUser = idUser;
        this.tag = tag;
    }

    public int getidUser() {
        return idUser;
    }

    public void setidUser(int idUser) {
        this.idUser = idUser;
    }

    public String gettag() {
        return tag;
    }

    public void settag(String tag) {
        this.tag = tag;
    }
}
