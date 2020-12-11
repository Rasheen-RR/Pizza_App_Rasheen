package com.infinium.pizza_app_rasheen.ui.pizza_details;

import com.infinium.pizza_app_rasheen.dao.Toppings;

public interface ToppingListner {

    void onSelect(Toppings toppings);
    void onDeSelect(Toppings toppings);

}
