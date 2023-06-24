package com.example.myapplication.activities.models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.nfc.Tag;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Base64;

import androidx.annotation.NonNull;

import java.util.List;

public class SearchModel implements Parcelable {
    String searchImage;
    String searchName;

    String searchCategory;
    List<TagModel> tagModelList;

    public SearchModel(String searchImage, String searchName) {
        this.searchImage = searchImage;
        this.searchName = searchName;
    }

    public SearchModel(String searchImage, String searchName, String searchCategory) {
        this.searchImage = searchImage;
        this.searchName = searchName;
        this.searchCategory = searchCategory;
    }

    protected SearchModel(Parcel in) {
        searchImage = in.readString();
        searchName = in.readString();
        searchCategory = in.readString();
        tagModelList = in.createTypedArrayList(TagModel.CREATOR);
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

    public List<TagModel> getTagModelList() {
        return tagModelList;
    }

    public void setTagModelList(List<TagModel> tagModelList) {
        this.tagModelList = tagModelList;
    }

    public String getSearchCategory() {
        return searchCategory;
    }

    public void setSearchCategory(String searchCategory) {
        this.searchCategory = searchCategory;
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
        dest.writeString(searchCategory);
        dest.writeTypedList(tagModelList);

    }
}
