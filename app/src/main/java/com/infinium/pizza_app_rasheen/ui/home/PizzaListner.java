package com.infinium.pizza_app_rasheen.ui.home;

import com.google.firebase.auth.FirebaseUser;
import com.infinium.pizza_app_rasheen.dao.Pizza;

import java.util.List;

public interface PizzaListner {

    void onPizzaResult(List<Pizza> result);
    void onStart();
    void onFailure();

}
