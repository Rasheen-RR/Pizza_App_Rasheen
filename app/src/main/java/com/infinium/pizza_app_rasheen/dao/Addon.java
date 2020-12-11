package com.infinium.pizza_app_rasheen.dao;

import java.io.Serializable;

public class Addon  implements Serializable {

    private String name;
    private String price;


    public Addon() {
    }

    public Addon(String name, String price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
