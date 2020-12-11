package com.infinium.pizza_app_rasheen.dao;

import java.io.Serializable;

public class Toppings implements Serializable {

    private String name;
    private String image;

    public Toppings() {
    }

    public Toppings(String name, String image) {
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) return true; // Both objects have the same reference -> the objects are equal
        if((obj == null) || (obj.getClass() != this.getClass())) return false; // Classes are different -> objects are different
        Toppings p = (Toppings) obj; // Cast obj into Product
        if((this.getName().equals(p.getName())) ) return true; // Price and name are the same -> both products are the same
        return false; // At this point the two objects can't be equal
    }
}
