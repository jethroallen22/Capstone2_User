package com.example.myapplication.ui.payment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PaymentViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private final MutableLiveData<String> mText;

    public PaymentViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is profile fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}