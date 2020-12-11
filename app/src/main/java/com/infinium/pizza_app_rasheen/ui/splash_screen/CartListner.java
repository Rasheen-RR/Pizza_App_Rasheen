package com.infinium.pizza_app_rasheen.ui.splash_screen;

import com.infinium.pizza_app_rasheen.dao.Cart;
import com.infinium.pizza_app_rasheen.dao.OrderPizza;

public interface CartListner {
    void onSuccess(Cart cart);
    void successAdded(Cart cart);
    void successRemoved(Cart cart);
    void successDeleted(Cart cart, int pos);
    void placeOrder();
    void getOrders(OrderPizza orders);
}
