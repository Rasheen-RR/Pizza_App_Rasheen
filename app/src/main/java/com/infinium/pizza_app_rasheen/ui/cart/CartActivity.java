package com.infinium.pizza_app_rasheen.ui.cart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.infinium.pizza_app_rasheen.MainActivity;
import com.infinium.pizza_app_rasheen.R;
import com.infinium.pizza_app_rasheen.dao.Cart;
import com.infinium.pizza_app_rasheen.dao.CartPizza;
import com.infinium.pizza_app_rasheen.dao.OrderPizza;
import com.infinium.pizza_app_rasheen.ui.splash_screen.CartListner;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CartActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    public static final String PREFS_NAME = "CartPref";
    public static final String PREFS_NAME_ORDER = "OrderPref";

    Cart cart;
    CartViewModel cartViewModel;

    RelativeLayout cartHolder;
    ConstraintLayout emptyCart;
    private Button placeOrder;
    OrderPizza orderPizza;

    TextView totalPrice, itemCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        cartViewModel = ViewModelProviders.of(this).get(CartViewModel.class);

        recyclerView = findViewById(R.id.list_view_cart);

        placeOrder = findViewById(R.id.place_order);
        totalPrice = findViewById(R.id.total_price);
        itemCount = findViewById(R.id.item_count);

        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        cartHolder = findViewById(R.id.cart_list_holder);
        emptyCart = findViewById(R.id.cart_no_items);

        Gson gson =new Gson();
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String cartString = prefs.getString("cart", "{}");
        cart = gson.fromJson(cartString, Cart.class);

        SharedPreferences orderPrefs = getSharedPreferences(PREFS_NAME_ORDER, MODE_PRIVATE);
        String orderString = orderPrefs.getString("orders", "{}");
        orderPizza = gson.fromJson(orderString, OrderPizza.class);


        int itemCountTotal = 0;

        // Check if pizza list in cart is null if not null get the total
        if(!(cart.getPizzaList() == null)){
            for(CartPizza cartPizza: cart.getPizzaList() ){
                itemCountTotal = itemCountTotal + cartPizza.getQuantity();
            }
        }


        totalPrice.setText(String.format("%.2f CAD", cart.getTotal()));
        itemCount.setText(String.format("%d Item(s)", itemCountTotal));

        if(cart.getPizzaList() == null){
            emptyCart.setVisibility(View.VISIBLE);
            cartHolder.setVisibility(View.GONE);

        }else{
            emptyCart.setVisibility(View.GONE);
            cartHolder.setVisibility(View.VISIBLE);

            RecyclerView rvContacts = (RecyclerView) findViewById(R.id.list_view_cart);

            CartAdapter adapter = new CartAdapter(cart,this,cartViewModel,totalPrice, itemCount);
            rvContacts.setAdapter(adapter);
            rvContacts.setLayoutManager(new LinearLayoutManager(this));
        }




        // set an on click listner to placeOrder bbutton
        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                List<Cart> list = new ArrayList<>();
                // Check if order list is null
                if(orderPizza == null){
                    orderPizza = new OrderPizza();
                }else{
                    // Set the list from cart to the normal list.
                    list  = orderPizza.getCartPizzaList();
                }

                list.add(0,cart);
                orderPizza.setCartPizzaList(list);

                // Creat a random uuid to set as order id
                UUID uuid = UUID.randomUUID();

                cart.setId(uuid.toString());

                // Set an listner to listen for the callback on success
                cartViewModel.placeOrder(new CartListner() {
                    @Override
                    public void onSuccess(Cart cart) {

                    }

                    @Override
                    public void successAdded(Cart cart) {

                    }

                    @Override
                    public void successRemoved(Cart cart) {

                    }

                    @Override
                    public void successDeleted(Cart cart, int pos) {

                    }

                    @Override
                    public void placeOrder() {
                        startActivity(new Intent(CartActivity.this, MainActivity.class));
                        Snackbar.make(view, "Order placed!", Snackbar.LENGTH_SHORT).setAction("Action", null).show();;
                        finish();
                    }

                    @Override
                    public void getOrders(OrderPizza orders) {

                    }
                },orderPizza);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}