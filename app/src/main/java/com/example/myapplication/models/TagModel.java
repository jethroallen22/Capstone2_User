package com.example.myapplication.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class TagModel implements Parcelable {
    int idTag, idProduct, idStore;
    boolean isMatch;
    String tagname;

    public TagModel(int idTag, int idProduct, int idStore, String tagname) {
        this.idTag = idTag;
        this.idProduct = idProduct;
        this.idStore = idStore;
        this.tagname = tagname;
    }
    public TagModel(int idProduct, int idStore, String tagname) {
        this.idProduct = idProduct;
        this.idStore = idStore;
        this.tagname = tagname;
    }

    protected TagModel(Parcel in) {
        idTag = in.readInt();
        idProduct = in.readInt();
        idStore = in.readInt();
        tagname = in.readString();
    }

    public static final Creator<TagModel> CREATOR = new Creator<TagModel>() {
        @Override
        public TagModel createFromParcel(Parcel in) {
            return new TagModel(in);
        }

        @Override
        public TagModel[] newArray(int size) {
            return new TagModel[size];
        }
    };

    public int getIdTag() {
        return idTag;
    }

    public void setIdTag(int idTag) {
        this.idTag = idTag;
    }

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public int getIdStore() {
        return idStore;
    }

    public void setIdStore(int idStore) {
        this.idStore = idStore;
    }

    public String getTagname() {
        return tagname;
    }

    public void setTagname(String tagname) {
        this.tagname = tagname;
    }

    public boolean isMatch() {
        return isMatch;
    }

    public void setMatch(boolean match) {
        isMatch = match;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(idTag);
        dest.writeInt(idProduct);
        dest.writeInt(idStore);
        dest.writeString(tagname);
    }
}
