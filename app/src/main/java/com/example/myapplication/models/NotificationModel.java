package com.example.myapplication.models;

import java.util.Date;

public class NotificationModel {
    String title, description, type;
    Date date;

    public NotificationModel(String title, String description, String type, Date date) {
        this.title = title;
        this.description = description;
        this.type = type;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
