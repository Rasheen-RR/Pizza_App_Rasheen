package com.infinium.pizza_app_rasheen.ui.order_details;

import android.content.Context;
import android.graphics.Outline;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.infinium.pizza_app_rasheen.R;
import com.infinium.pizza_app_rasheen.dao.Cart;
import com.infinium.pizza_app_rasheen.dao.CartPizza;
import com.infinium.pizza_app_rasheen.ui.cart.CartViewModel;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.List;

public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.ViewHolder> {

    private Context context;
    Cart cart;
    private List<CartPizza> items; //data source of the list adapter
    private CartViewModel cartViewModel;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView textViewItemName, textViewItemDesc,textViewItemPrice;

        CardView card ;

        private final ImageView image;

        public ViewHolder(View view) {
            super(view);
            textViewItemName = (TextView) view.findViewById(R.id.order_pizza_name);
            textViewItemDesc = (TextView) view.findViewById(R.id.order_pizza_description);
            textViewItemPrice = (TextView) view.findViewById(R.id.order_pizza_price);
            card = (CardView) view.findViewById(R.id.details_pizza_card);
            image = (ImageView) view.findViewById(R.id.order_image);
        }

        public TextView getTextViewItemName() {
            return textViewItemName;
        }

        public TextView getTextViewItemDesc() {
            return textViewItemDesc;
        }
        public TextView getTextViewItemPrice() {
            return textViewItemPrice;
        }
        public ImageView getItemImage() {
            return image;
        }
        public CardView getCard() {
            return card;
        }

    }

    public OrderDetailsAdapter(Cart dataSet, Context context) {
        items = dataSet.getPizzaList();
        cart = dataSet;
        this.context = context;
    }



    @NonNull
    @Override
    public OrderDetailsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_details_item, parent, false);
        return new OrderDetailsAdapter.ViewHolder(view);
    }


    // Update the list item with the order details
    @Override
    public void onBindViewHolder(@NonNull OrderDetailsAdapter.ViewHolder holder, int position) {
        holder.getCard().setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 50, view.getWidth(), (view.getHeight()), (float) 50);
            }
        });
        holder.getCard().setClipToOutline(true);


        CartPizza pizza = items.get(position);

        StringBuilder toppingString = new StringBuilder();
        if(items.get(position).getVegeToppings() != null) {
            for (int i = 0; i < items.get(position).getVegeToppings().size(); i++) {
                toppingString.append(items.get(position).getVegeToppings().get(i).getName() + ", ");
            }
        }



        if(items.get(position).getNonVegeToppings() != null){
            for (int i = 0; i < items.get(position).getNonVegeToppings().size(); i++) {
                toppingString.append(items.get(position).getNonVegeToppings().get(i).getName() + ", ");
            }
        }

        holder.getTextViewItemDesc().setText(toppingString.toString());

        holder.getTextViewItemName().setText(items.get(position).getName());
        holder.getTextViewItemPrice().setText(String.format("%.2f CAD", items.get(position).getTotal()));
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .build();


        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(((OrderDetailsActivity)context)));;
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(items.get(position).getImage(),holder.getItemImage(),options);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
