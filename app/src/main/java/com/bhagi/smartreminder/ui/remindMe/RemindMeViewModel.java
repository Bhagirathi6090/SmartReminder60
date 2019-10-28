package com.bhagi.smartreminder.ui.remindMe;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RemindMeViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public RemindMeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is RemindMe fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}