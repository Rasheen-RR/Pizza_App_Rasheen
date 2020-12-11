package com.infinium.pizza_app_rasheen.ui.cart;

import android.content.Context;
import android.content.Intent;
import android.graphics.Outline;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.infinium.pizza_app_rasheen.MainActivity;
import com.infinium.pizza_app_rasheen.R;
import com.infinium.pizza_app_rasheen.dao.Cart;
import com.infinium.pizza_app_rasheen.dao.CartPizza;
import com.infinium.pizza_app_rasheen.dao.OrderPizza;
import com.infinium.pizza_app_rasheen.ui.splash_screen.CartListner;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {


    private Context context;
    Cart cart;
    private List<CartPizza> pizzaList; //data source of the list adapter
    private CartViewModel cartViewModel;
    TextView totalPrice;
    TextView itemQuantity;


    // Initialize the view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView textViewItemName,textViewItemDesc, textViewItemPrice;

        private final Button pizzaQuantity,cartRemove, cartDelete, cartAdd;

        CardView card ;

        private final ImageView image;

        public ViewHolder(View view) {
            super(view);
            textViewItemName = (TextView) view.findViewById(R.id.pizza_name);
            textViewItemDesc = (TextView) view.findViewById(R.id.pizza_description);
            textViewItemPrice = (TextView) view.findViewById(R.id.pizza_price);
            pizzaQuantity = (Button) view.findViewById(R.id.pizza_quantity);
            cartRemove = (Button) view.findViewById(R.id.cart_remove);
            cartDelete = (Button) view.findViewById(R.id.cart_delete);
            cartAdd = (Button) view.findViewById(R.id.cart_add);
            image = (ImageView) view.findViewById(R.id.pizza_image);
            card = view.findViewById(R.id.pizza_card);
        }

        public TextView getTextViewItemName() {
            return textViewItemName;
        }
        public TextView getTextViewItemDesc() {
            return textViewItemDesc;
        }
        public Button getPizzaQuantity() {
            return pizzaQuantity;
        }
        public TextView getTextViewItemPrice() {
            return textViewItemPrice;
        }
        public ImageView getItemImage() {
            return image;
        }
        public Button getCartRemove() {
            return cartRemove;
        }
        public Button getCartDelete() {
            return cartDelete;
        }
        public Button getCartAdd() {
            return cartAdd;
        }
        public CardView getCard() {
            return card;
        }

    }

    public CartAdapter(Cart dataSet, Context context, CartViewModel cartViewModel, TextView totalPrice, TextView itemQuantity) {
        pizzaList = dataSet.getPizzaList();
        cart = dataSet;
        this.context = context;
        this.cartViewModel = cartViewModel;
        this.totalPrice = totalPrice;
        this.itemQuantity = itemQuantity;
    }



    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_list_item, parent, false);
        return new CartAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, int position) {
        holder.getCard().setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 50, view.getWidth(), (view.getHeight()), (float) 50);
            }
        });
        holder.getCard().setClipToOutline(true);



        StringBuilder toppingString = new StringBuilder();
        // Check if vege topping list is null
        if(pizzaList.get(position).getVegeToppings() != null) {
            // Iterate through the list and append the toppings to the string
            for (int i = 0; i < pizzaList.get(position).getVegeToppings().size(); i++) {
                toppingString.append(pizzaList.get(position).getVegeToppings().get(i).getName() + ", ");
            }
        }


        // Check if non-vege topping list is null
        if(pizzaList.get(position).getNonVegeToppings() != null){
            // Iterate through the list and append the toppings to the string
            for (int i = 0; i < pizzaList.get(position).getNonVegeToppings().size(); i++) {
                toppingString.append(pizzaList.get(position).getNonVegeToppings().get(i).getName() + ", ");
            }
        }

        holder.getTextViewItemDesc().setText(toppingString.toString());



        // Check if the cart quantity is 1 or below
        if(pizzaList.get(position).getQuantity() > 1){
            // Set the button to minus instead of delete
            holder.getCartDelete().setVisibility(View.GONE);
            holder.getCartRemove().setVisibility(View.VISIBLE);
        }else{
            // Set the button to delete instead of minus
            holder.getCartDelete().setVisibility(View.VISIBLE);
            holder.getCartRemove().setVisibility(View.GONE);
        }

        CartPizza pizza = pizzaList.get(position);

        holder.getCartAdd().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cart.setTotal(cart.getTotal() - pizza.getTotal());

                pizza.setQuantity(pizza.getQuantity() +1);
                pizza.setTotal(pizza.getTotal() + pizza.getPrice());
                pizzaList.set(position,pizza);
                cart.setTotal(cart.getTotal() + pizza.getTotal());


                cart.setPizzaList(pizzaList);
                cartViewModel.addCart(cart, new CartListner() {
                    @Override
                    public void onSuccess(Cart cart) {

                    }

                    @Override
                    public void successAdded(Cart cart) {
                        pizzaList = cart.getPizzaList();
                        notifyDataSetChanged();
                        totalPrice.setText(String.format("%.2f", cart.getTotal()));
                        setCartItemTotal();
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
                },position);
            }
        });

        holder.getCartRemove().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Remove the pizza total from the cart
                cart.setTotal(cart.getTotal() - pizza.getTotal());

                // Reduce the quantity from the pizza
                pizza.setQuantity(pizza.getQuantity() -1);
                // Set the new pizza total
                pizza.setTotal(pizza.getTotal() - pizza.getPrice());
                // add the nnew pizza to the list
                pizzaList.set(position,pizza);
                // Re update the cart total
                cart.setTotal(cart.getTotal() + pizza.getTotal());


                // Add a listner to listen for the callback
                cart.setPizzaList(pizzaList);
                cartViewModel.removeCart(cart, new CartListner() {
                    @Override
                    public void onSuccess(Cart cart) {

                    }

                    @Override
                    public void successAdded(Cart cart) {

                    }

                    @Override
                    public void successRemoved(Cart cart) {

                        // Check if the list is null
                        if(cart.getPizzaList() == null){
                            context.startActivity(new Intent(context,MainActivity.class));
                        }else{
                            // Update the pizza list and notify the data set change to update the list
                            pizzaList = cart.getPizzaList();
                            notifyDataSetChanged();
                            totalPrice.setText(String.format("%.2f", cart.getTotal()));
                            setCartItemTotal();
                            Snackbar.make(view, "Item removed from cart!", Snackbar.LENGTH_SHORT).setAction("Action", null).show();;

                        }


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
                },position);
            }
        });


        holder.getCartDelete().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Update the cart total
                cart.setTotal(cart.getTotal() - pizzaList.get(position).getTotal());
                // Remove the pizza from the cart
                pizzaList.remove(position);
                // Update the cat
                cart.setPizzaList(pizzaList);
                cartViewModel.deleteCart(new CartListner() {
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

                        // Check if the cart is null
                        if(cart.getPizzaList() == null){
                            context.startActivity(new Intent(context,MainActivity.class));
                        }else{
                            pizzaList = cart.getPizzaList();
                            notifyDataSetChanged();
                            totalPrice.setText(String.format("%.2f", cart.getTotal()));
                            setCartItemTotal();
                            Snackbar.make(view, "Item deleted from cart!", Snackbar.LENGTH_SHORT).setAction("Action", null).show();;

                        }
                    }

                    @Override
                    public void placeOrder() {

                    }

                    @Override
                    public void getOrders(OrderPizza orders) {

                    }
                },cart);
            }
        });


        holder.getTextViewItemName().setText(pizzaList.get(position).getName());
        holder.getTextViewItemPrice().setText(String.format("%.2f CAD", pizzaList.get(position).getTotal()));
        holder.getPizzaQuantity().setText(String.valueOf(pizzaList.get(position).getQuantity()));



        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .build();


        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(((CartActivity)context)));;
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(pizzaList.get(position).getImage(),holder.getItemImage(),options);

    }

    @Override
    public int getItemCount() {

        if(pizzaList == null){
            return  0;
        }
        return pizzaList.size();
    }


    // Update the cart item total
    public void setCartItemTotal(){
        int itemCountTotal = 0;
        if(!(cart.getPizzaList() == null)){
            for(CartPizza cartPizza: cart.getPizzaList() ){
                itemCountTotal = itemCountTotal + cartPizza.getQuantity();
            }
        }
        itemQuantity.setText(String.format("%d Item(s)", itemCountTotal));
    }
}
