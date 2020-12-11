package com.infinium.pizza_app_rasheen.ui.register;

/**
 * Class exposing authenticated user details to the UI.
 */
public class RegisterUserView {
    private String displayName;

    public RegisterUserView(String displayName) {
        this.displayName = displayName;
    }

    String getDisplayName() {
        return displayName;
    }
}