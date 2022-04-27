package ru.mirea.panin.mireaproject.ui.browser;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BrowserViewModel extends ViewModel {
    private final MutableLiveData<String> mLink;

    public BrowserViewModel() {
        mLink = new MutableLiveData<>();
        mLink.setValue("https://www.mirea.ru");
    }

    public LiveData<String> getLink() {
        return mLink;
    }
}