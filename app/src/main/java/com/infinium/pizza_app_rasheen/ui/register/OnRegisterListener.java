package com.infinium.pizza_app_rasheen.ui.register;

import com.google.firebase.auth.FirebaseUser;

public interface OnRegisterListener {
    void onLoginResult(FirebaseUser result);
    void onStart();
    void onFailure();
}
