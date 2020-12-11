package com.infinium.pizza_app_rasheen.dao;

import java.io.Serializable;
import java.util.List;

public class OrderPizza implements Serializable {

    private List<Cart> cartPizzaList;

    public OrderPizza() {
    }

    public OrderPizza(List<Cart> cartPizzaList) {
        this.cartPizzaList = cartPizzaList;
    }

    public List<Cart> getCartPizzaList() {
        return cartPizzaList;
    }

    public void setCartPizzaList(List<Cart> cartPizzaList) {
        this.cartPizzaList = cartPizzaList;
    }
}
