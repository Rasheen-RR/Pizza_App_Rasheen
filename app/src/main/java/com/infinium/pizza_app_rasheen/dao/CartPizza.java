package com.infinium.pizza_app_rasheen.dao;

import java.io.Serializable;
import java.util.List;

public class CartPizza implements Serializable{
    private int quantity;
    private String name;
    private int id;
    private List<Toppings> vegeToppings;
    private List<Toppings> nonVegeToppings;
    private boolean cheese;
    private String image;
    private double price;
    private double total;

    public CartPizza() {
    }

    public CartPizza(int quantity, String name, int id, List<Toppings> vegeToppings, List<Toppings> nonVegeToppings, boolean cheese, String image, double price, double total) {
        this.quantity = quantity;
        this.name = name;
        this.id = id;
        this.vegeToppings = vegeToppings;
        this.nonVegeToppings = nonVegeToppings;
        this.cheese = cheese;
        this.image = image;
        this.price = price;
        this.total = total;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public boolean isCheese() {
        return cheese;
    }

    public void setCheese(boolean cheese) {
        this.cheese = cheese;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
