package com.infinium.pizza_app_rasheen.dao;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class Pizza implements Serializable {

    private int id;
    private String name;
    private String description;
    private String rating;
    private List<Toppings> vegeToppings;
    private List<Toppings> nonVegeToppings;
    private String image;
    private HashMap<String , Double> price;

    public Pizza() {
    }

    public Pizza(int id, String name, String description, String rating, List<Toppings> vegeToppings, List<Toppings> nonVegeToppings, String image, HashMap<String, Double> price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.rating = rating;
        this.vegeToppings = vegeToppings;
        this.nonVegeToppings = nonVegeToppings;
        this.image = image;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public List<Toppings> getVegeToppings() {
        return vegeToppings;
    }

    public void setVegeToppings(List<Toppings> vegeToppings) {
        this.vegeToppings = vegeToppings;
    }

    public List<Toppings> getNonVegeToppings() {
        return nonVegeToppings;
    }

    public void setNonVegeToppings(List<Toppings> nonVegeToppings) {
        this.nonVegeToppings = nonVegeToppings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public HashMap<String, Double> getPrice() {
        return price;
    }

    public void setPrice(HashMap<String, Double> price) {
        this.price = price;
    }
}
