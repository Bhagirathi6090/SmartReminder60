package com.bhagi.smartreminder.ui.toDo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AddListViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AddListViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is AddList fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}