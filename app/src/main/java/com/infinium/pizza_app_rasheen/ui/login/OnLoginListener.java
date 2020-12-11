package com.infinium.pizza_app_rasheen.ui.login;

import com.google.firebase.auth.FirebaseUser;
import com.infinium.pizza_app_rasheen.data.Result;

public interface OnLoginListener {
    void onLoginResult(FirebaseUser result);
    void onStart();
    void onFailure();
}
