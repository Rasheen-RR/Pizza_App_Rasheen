package com.infinium.pizza_app_rasheen.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Outline;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.infinium.pizza_app_rasheen.MainActivity;
import com.infinium.pizza_app_rasheen.R;
import com.infinium.pizza_app_rasheen.dao.Cart;
import com.infinium.pizza_app_rasheen.dao.CartPizza;
import com.infinium.pizza_app_rasheen.dao.OrderPizza;
import com.infinium.pizza_app_rasheen.dao.Pizza;
import com.infinium.pizza_app_rasheen.dao.Toppings;
import com.infinium.pizza_app_rasheen.ui.pizza_details.PizzaDetailViewModel;
import com.infinium.pizza_app_rasheen.ui.pizza_details.PizzaDetailsActivity;
import com.infinium.pizza_app_rasheen.ui.splash_screen.CartListner;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class PizzaListAdapter extends RecyclerView.Adapter<PizzaListAdapter.ViewHolder> {

    private Context context;
    private List<Pizza> items; //data source of the list adapter
    Cart cart;
    PizzaDetailViewModel pizzaDetailViewModel;

    public static final String PREFS_NAME = "CartPref";

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView textViewItemName, textViewItemDesc, textViewItemRating, textViewItemPrice;

        private final LinearLayout listItemLayout;

        CardView card ;

        private final ImageView im;

        private final Button addToCart;

        public ViewHolder(View view) {
            super(view);
            textViewItemName = (TextView) view.findViewById(R.id.pizza_name);
            textViewItemDesc = (TextView) view.findViewById(R.id.pizza_description);
            textViewItemRating = (TextView) view.findViewById(R.id.pizza_rating);
            textViewItemPrice = (TextView) view.findViewById(R.id.pizza_price);
            im = (ImageView) view.findViewById(R.id.pizza_image);
            addToCart = (Button) view.findViewById(R.id.add_to_cart);
            card = view.findViewById(R.id.pizza_card);
            listItemLayout = view.findViewById(R.id.list_item_layout);
        }

        public TextView getTextViewItemName() {
            return textViewItemName;
        }
        public TextView getTextViewItemDesc() {
            return textViewItemDesc;
        }
        public TextView getTextViewItemRating() {
            return textViewItemRating;
        }
        public TextView getTextViewItemPrice() {
            return textViewItemPrice;
        }
        public ImageView getItemImage() {
            return im;
        }
        public Button getButtonAddToCart() {
            return addToCart;
        }
        public CardView getCard() {
            return card;
        }
        public LinearLayout getListItemLinearLayout() {
            return listItemLayout;
        }

    }

    public PizzaListAdapter(List<Pizza> dataSet, Context context, PizzaDetailViewModel pizzaDetailViewModel) {
        items = dataSet;
        this.context = context;
        this.pizzaDetailViewModel = pizzaDetailViewModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.pizza_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        // Update the list items with the pizza detail

        holder.getCard().setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 50, view.getWidth(), (view.getHeight()), (float) 50);
            }
        });
        holder.getCard().setClipToOutline(true);


        holder.getTextViewItemName().setText(items.get(position).getName());
        holder.getTextViewItemDesc().setText(items.get(position).getDescription());
        holder.getTextViewItemPrice().setText(String.format("%.2f CAD", items.get(position).getPrice().get("small")));
        holder.getTextViewItemRating().setText(items.get(position).getRating());


        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .build();


        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(((MainActivity)context)));;
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(items.get(position).getImage(),holder.getItemImage(),options);

        holder.getListItemLinearLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PizzaDetailsActivity.class);
                intent.putExtra("pizza", items.get(position));
                context.startActivity(intent);
            }
        });

        holder.getButtonAddToCart().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                // Create a new gson object and get the cart string from shared preference
                Gson gson = new Gson();
                SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                String cartString = prefs.getString("cart", "{}");

                // Convert the string to a Cart object
                cart = gson.fromJson(cartString, Cart.class);

                List<CartPizza> pizzaList;
                boolean added = false;

                // Check if cart has a list if not create a new list
                if(cart.getPizzaList() == null){
                    pizzaList = new ArrayList<>();
                }else{
                    pizzaList = cart.getPizzaList();
                }


                // Create a new cart object
                List<Toppings> toppingsList = new ArrayList<>();
                List<Toppings> nonVegeToppingsList = new ArrayList<>();
                CartPizza cartPizza = new CartPizza();
                cartPizza.setId(items.get(position).getId());
                cartPizza.setName(items.get(position).getName());
                cartPizza.setImage(items.get(position).getImage());
                cartPizza.setNonVegeToppings(nonVegeToppingsList);
                cartPizza.setVegeToppings(toppingsList);
                cartPizza.setQuantity(1);
                cartPizza.setCheese(false);
                cartPizza.setPrice(items.get(position).getPrice().get("small"));


                cartPizza.setTotal(cartPizza.getPrice() * 1);


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

                            // Check if the arrays match and set vege as true
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
                cart.setTotal(cart.getTotal() + cartPizza.getTotal());

                pizzaDetailViewModel.updateCart(cart, new CartListner() {
                    @Override
                    public void onSuccess(Cart cart) {

                    }

                    @Override
                    public void successAdded(Cart cart) {
                        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
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



    @Override
    public int getItemCount() {
        return items.size();
    }


}
