package com.example.myapplication.ui.messages;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MessagesViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private final MutableLiveData<String> mText;

    public MessagesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is profile fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}