package com.example.myapplication.ui.cart;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CartViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private final MutableLiveData<String> mText;

    public CartViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is profile fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}