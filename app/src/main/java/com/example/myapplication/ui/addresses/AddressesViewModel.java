package com.example.myapplication.ui.addresses;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AddressesViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private final MutableLiveData<String> mText;

    public AddressesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is profile fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}