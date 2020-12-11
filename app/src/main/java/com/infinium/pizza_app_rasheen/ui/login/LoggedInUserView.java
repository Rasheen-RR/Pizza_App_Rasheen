package com.infinium.pizza_app_rasheen.ui.login;

import com.google.firebase.auth.FirebaseUser;

/**
 * Class exposing authenticated user details to the UI.
 */
public class LoggedInUserView {
    private FirebaseUser displayName;

    public LoggedInUserView(FirebaseUser displayName) {
        this.displayName = displayName;
    }

    FirebaseUser getDisplayName() {
        return displayName;
    }
}