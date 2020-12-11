package com.infinium.pizza_app_rasheen.dao;

import java.io.Serializable;
import java.util.List;

public class Cart implements Serializable {

    String id;
    double total;
    List<CartPizza> pizzaList;

    public Cart() {
    }

    public Cart(String id, double total, List<CartPizza> pizzaList) {
        this.id = id;
        this.total = total;
        this.pizzaList = pizzaList;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public List<CartPizza> getPizzaList() {
        return pizzaList;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPizzaList(List<CartPizza> pizzaList) {
        this.pizzaList = pizzaList;
    }
}
