package com.example.myapplication.activities.models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Base64;

import androidx.annotation.NonNull;

public class SearchModel implements Parcelable {
    String searchImage;
    String searchName;
    String searchTag;

    public SearchModel(String searchImage, String searchName, String searchTag) {
        this.searchImage = searchImage;
        this.searchName = searchName;
        this.searchTag = searchTag;
    }

    protected SearchModel(Parcel in) {
        searchImage = in.readString();
        searchName = in.readString();
        searchTag = in.readString();
    }

    public static final Creator<SearchModel> CREATOR = new Creator<SearchModel>() {
        @Override
        public SearchModel createFromParcel(Parcel in) {
            return new SearchModel(in);
        }

        @Override
        public SearchModel[] newArray(int size) {
            return new SearchModel[size];
        }
    };

    public String getSearchImage() {
        return searchImage;
    }

    public void setSearchImage(String searchImage) {
        this.searchImage = searchImage;
    }

    public String getSearchName() {
        return searchName;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }

    public String getSearchTag() {
        return searchTag;
    }

    public void setSearchTag(String searchTag) {
        this.searchTag = searchTag;
    }

    public Bitmap getBitmapImage(){
        byte[] byteArray = Base64.decode(searchImage, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0 , byteArray.length);
        return bitmap;
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(searchImage);
        dest.writeString(searchName);
        dest.writeString(searchTag);

    }
}
