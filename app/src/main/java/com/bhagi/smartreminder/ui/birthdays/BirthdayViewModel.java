package com.bhagi.smartreminder.ui.birthdays;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BirthdayViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public BirthdayViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is send fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}