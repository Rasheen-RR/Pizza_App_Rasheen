package com.infinium.pizza_app_rasheen.ui.pizza_details;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.infinium.pizza_app_rasheen.R;
import com.infinium.pizza_app_rasheen.dao.Cart;
import com.infinium.pizza_app_rasheen.dao.CartPizza;
import com.infinium.pizza_app_rasheen.dao.OrderPizza;
import com.infinium.pizza_app_rasheen.dao.Pizza;
import com.infinium.pizza_app_rasheen.dao.Toppings;
import com.infinium.pizza_app_rasheen.ui.splash_screen.CartListner;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class PizzaDetailsActivity extends AppCompatActivity {

    Pizza pizza = null;
    List<Toppings> toppingsList = new ArrayList<>();
    List<Toppings> nonVegeToppingsList = new ArrayList<>();
    public static final String PREFS_NAME = "CartPref";
    public double itemPrice;
    private FirebaseAuth mAuth;
    FirebaseUser logUser = null;
    Cart cart;
    PizzaDetailViewModel pizzaDetailViewModel;
    private boolean cheese = false;
    String size = "small";
    TextView totalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pizza_details);
        pizzaDetailViewModel = ViewModelProviders.of(this).get(PizzaDetailViewModel.class);
        mAuth = FirebaseAuth.getInstance();
        logUser = mAuth.getCurrentUser();


        // Get the cart from the shared preferences
        Gson gson =new Gson();
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String cartString = prefs.getString("cart", "{}");

        cart = gson.fromJson(cartString, Cart.class);

        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();


        final TextView price = findViewById(R.id.price_text_view);
        RadioGroup sizeRadio = findViewById(R.id.size_radio_group);

        totalPrice = findViewById(R.id.total_price);

        RecyclerView rvContacts = (RecyclerView) findViewById(R.id.vege_list);
        RecyclerView nonVegeList = (RecyclerView) findViewById(R.id.non_vege_list);
        Button addToCart = (Button) findViewById(R.id.add_to_cart);
        CheckBox addCheese = findViewById(R.id.pizzaCheeseCheckBox);


        addCheese.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                cheese = b;

                if(b){
                    itemPrice = itemPrice + 1;
                    totalPrice.setText(String.format("%.2f CAD", (itemPrice)));
                }else{
                    itemPrice = itemPrice - 1;
                    totalPrice.setText(String.format("%.2f CAD", (itemPrice)));
                }
            }
        });

        ImageView pizzaImage = (ImageView) findViewById(R.id.pizza_image);


        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .build();

        pizza = (Pizza)intent.getSerializableExtra("pizza");
        price.setText(String.format("$ %.2f",pizza.getPrice().get("small")));
        itemPrice = pizza.getPrice().get("small");
        getSupportActionBar().setTitle(pizza.getName());

        totalPrice.setText(String.format("%.2f CAD", itemPrice));



        ToppingAdapter adapter = new ToppingAdapter(pizza.getVegeToppings(), this, new ToppingListner() {
            // If the topping is selecetd add the topping to the list
            @Override
            public void onSelect(Toppings toppings) {
                toppingsList.add(toppings);
                itemPrice = itemPrice + 1;
                totalPrice.setText(String.format("%.2f CAD", (itemPrice)));
            }

            // If the topping is de selected remove the topping to the list
            @Override
            public void onDeSelect(final Toppings toppings) {
                toppingsList.remove(toppings);
                for (Iterator<Toppings> iterator = toppingsList.iterator(); iterator.hasNext();) {
                    Toppings obj= iterator.next();
                    if (obj.getName().equals(toppings.getName())) {
                        iterator.remove();
                    }
                }
                itemPrice = itemPrice - 1;
                totalPrice.setText(String.format("%.2f CAD", itemPrice));
                System.out.println(toppingsList.size());
            }
        });
        rvContacts.setAdapter(adapter);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvContacts.setLayoutManager(layoutManager);



        ToppingAdapter nonVegedapter = new ToppingAdapter(pizza.getNonVegeToppings(), this, new ToppingListner() {
            @Override
            public void onSelect(Toppings toppings) {
                nonVegeToppingsList.add(toppings);
                itemPrice = itemPrice + 1;
                totalPrice.setText(String.format("%.2f CAD", (itemPrice)));


            }

            @Override
            public void onDeSelect(final Toppings toppings) {
                nonVegeToppingsList.remove(toppings);
                for (Iterator<Toppings> iterator = nonVegeToppingsList.iterator(); iterator.hasNext();) {
                    Toppings obj= iterator.next();
                    if (obj.getName().equals(toppings.getName())) {
                        iterator.remove();
                    }
                }
                itemPrice = itemPrice - 1;
                totalPrice.setText(String.format("%.2f CAD", itemPrice));
                System.out.println(nonVegeToppingsList.size());
            }
        });

        // Set the adapters to the vege and non vege list
        nonVegeList.setAdapter(nonVegedapter);

        LinearLayoutManager layoutManagerNonVege
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        nonVegeList.setLayoutManager(layoutManagerNonVege);

        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault((this)));;
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(pizza.getImage(),pizzaImage,options);

        sizeRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                setPrice(price,i);
            }
        });




        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean added = false;

                List<CartPizza> pizzaList;
                if(cart.getPizzaList() == null){
                    pizzaList = new ArrayList<>();
                }else{
                    pizzaList = cart.getPizzaList();
                }

                // Create a new pizza object

                CartPizza cartPizza = new CartPizza();
                cartPizza.setId(pizza.getId());
                cartPizza.setName(pizza.getName());
                cartPizza.setImage(pizza.getImage());
                cartPizza.setNonVegeToppings(nonVegeToppingsList);
                cartPizza.setVegeToppings(toppingsList);
                cartPizza.setQuantity(1);
                cartPizza.setCheese(cheese);


                double total = cart.getTotal() + (itemPrice * 1);

                // Create a new iterator for the pizza in cart
                for (Iterator<CartPizza> iterator = pizzaList.iterator(); iterator.hasNext();) {

                    boolean vege = false;
                    boolean nonVege = false;
                    boolean chees = false;

                    CartPizza obj= iterator.next();

                    // Check if the current pizza id matched with any of the pizza in the cart
                    if (obj.getId() == cartPizza.getId()) {
                        // If matches check if obj toppings and cart toppings are not null
                        if(obj.getVegeToppings() != null && cartPizza.getVegeToppings() != null){

                            // Convert the topping to an array
                            Toppings[] intArray = new Toppings[obj.getVegeToppings().size()];
                            intArray = obj.getVegeToppings().toArray(intArray);

                            // Convert the topping to an array
                            Toppings[] intArray2 = new Toppings[cartPizza.getVegeToppings().size()];
                            intArray2 = cartPizza.getVegeToppings().toArray(intArray2);

                            // Check if the arrays match and set vege as true
                            if(Arrays.equals(intArray,intArray2)){
                                vege = true;
                            }
                        }else if(obj.getVegeToppings() == null && (cartPizza.getVegeToppings() == null || cartPizza.getVegeToppings().size() == 0)){
                            vege = true;
                        }

                        // If matches check if obj toppings and cart toppings are not null
                        if(obj.getNonVegeToppings() != null && cartPizza.getNonVegeToppings() != null){

                            // Convert the topping to an array
                            Toppings[] intArray3 = new Toppings[obj.getNonVegeToppings().size()];
                            intArray3 = obj.getNonVegeToppings().toArray(intArray3);

                            // Convert the topping to an array
                            Toppings[] intArray4 = new Toppings[cartPizza.getNonVegeToppings().size()];
                            intArray4 = cartPizza.getNonVegeToppings().toArray(intArray4);


                            // Check if the arrays match and set nonvege as true
                            if(Arrays.equals(intArray3,intArray4)){
                                nonVege = true;
                            }
                        }else if(obj.getNonVegeToppings() == null && (cartPizza.getNonVegeToppings() == null || cartPizza.getNonVegeToppings().size() == 0)){
                            nonVege = true;
                        }

                        if(obj.isCheese() && cartPizza.isCheese()){
                            chees = true;
                        }else  if(!obj.isCheese() && !cartPizza.isCheese()){
                            chees = true;
                        }


                        // Check of all is set to true,  if then add the quantity to the existing product
                        if(vege && nonVege && chees){
                            obj.setQuantity(obj.getQuantity() + 1);
                            obj.setTotal(obj.getTotal() + (obj.getPrice() * 1));
                            added = true;
                            break;
                        }

                    }
                }


                // Check if pizza is added if not add the pizza to the list
                if(!added){
                    pizzaList.add(cartPizza);
                }

                cart.setPizzaList(pizzaList);


                cartPizza.setTotal(itemPrice * 1);
                cart.setTotal(total);
                cartPizza.setPrice(itemPrice);

               pizzaDetailViewModel.updateCart(cart, new CartListner() {
                   @Override
                   public void onSuccess(Cart cart) {

                   }

                   @Override
                   public void successAdded(Cart cart) {
                       SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME,MODE_PRIVATE).edit();
                       Gson gson = new Gson();

                       String cartString = gson.toJson(cart);
                       editor.putString("cart", cartString);
                       editor.apply();

                       Snackbar.make(view, "Item added to cart!", Snackbar.LENGTH_SHORT).setAction("Action", null).show();;
                   }

                   @Override
                   public void successRemoved(Cart cart) {

                   }

                   @Override
                   public void successDeleted(Cart cart, int pos) {

                   }

                   @Override
                   public void placeOrder() {

                   }

                   @Override
                   public void getOrders(OrderPizza orders) {

                   }
               });
            }
        });
    }



    // Update the price of the item when the sizes of the pizza is changed
    private void setPrice(TextView textView, int id){
        switch (id) {
            case R.id.radio_small:
                itemPrice = itemPrice - pizza.getPrice().get(size);
                itemPrice = itemPrice + pizza.getPrice().get("small");
                size = "small";
                textView.setText(String.format("$ %.2f CAD",pizza.getPrice().get("small")));
                totalPrice.setText(String.format("%.2f CAD", itemPrice));
                break;
            case R.id.radio_medium:
                itemPrice = itemPrice - pizza.getPrice().get(size);
                itemPrice = itemPrice + pizza.getPrice().get("medium");
                size = "medium";
                textView.setText(String.format("$ %.2f CAD",pizza.getPrice().get("medium")));
                totalPrice.setText(String.format("%.2f CAD", itemPrice));
                break;
            case R.id.radio_large:
                itemPrice = itemPrice - pizza.getPrice().get(size);
                itemPrice = itemPrice + pizza.getPrice().get("large");
                size = "large";
                textView.setText(String.format("$ %.2f CAD",pizza.getPrice().get("large")));
                totalPrice.setText(String.format("%.2f CAD", itemPrice));
                break;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}
