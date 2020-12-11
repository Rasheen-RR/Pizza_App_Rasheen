package com.infinium.pizza_app_rasheen.ui.orders;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class OrderViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<String> mBtn;
    private FirebaseAuth mAuth;
    FirebaseUser logUser = null;

    public OrderViewModel() {
        mAuth = FirebaseAuth.getInstance();

        logUser = mAuth.getCurrentUser();

        mText = new MutableLiveData<>();
        mBtn = new MutableLiveData<>();

        System.out.println("asssss");
        assert logUser != null;
        System.out.println(logUser.getDisplayName());
        mText.setValue(logUser.getDisplayName());
        mBtn.setValue("Lo");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<String> getBtnText() {
        return mBtn;
    }

}
